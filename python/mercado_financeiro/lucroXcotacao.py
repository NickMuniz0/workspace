import argparse
from concurrent.futures import ThreadPoolExecutor
import re

import pandas as pd
import requests

# Configurações do Pandas
pd.set_option("display.max_columns", None)
pd.set_option("display.max_rows", None)
pd.set_option("display.width", 200)

ANOS = ["2021", "2022", "2023", "2024", "2025", "Atual"]
headers = {"user-agent": "Mozilla/5.0"}


def get_json(url):
    response = requests.get(url, headers=headers, timeout=(5, 20))
    response.raise_for_status()
    return response.json()


def normalizar_tickers(tickers):
    """Normaliza a lista de tickers recebida por parâmetro."""
    lista = []
    for item in tickers:
        for ticker in item.replace(" ", "").split(","):
            if ticker:
                lista.append(ticker.upper())
    return lista

# -----------------------------
# Funções utilitárias
# -----------------------------
def carregar_tickers_disponiveis():
    """Carrega uma vez os identificadores de todos os tickers."""
    url = "https://investidor10.com.br/api/get-tickers/"
    return {
        ativo["name"]: (int(ativo["company_id"]), int(ativo["id"]))
        for ativo in get_json(url)["data"]
    }


def processar_ticker(ticker: str, mapa_tickers: dict) -> pd.DataFrame:
    """Carrega e consolida os dados de um ticker."""
    try:
        company_id, ticker_id = mapa_tickers[ticker]
    except KeyError as erro:
        raise ValueError(f"Ticker não encontrado: {ticker}") from erro

    df = carregar_balanco(company_id)
    monetarios, percentuais = separar_tabelas(df)
    cotacoes = carregar_cotacoes(ticker)
    preco_atual = carregar_preco_atual(ticker_id)
    indicadores = carregar_indicadores_completos(ticker_id)
    dividendo = dividendos_medio(ticker)

    return juntar_dados(monetarios, cotacoes, preco_atual, indicadores, ticker, dividendo)

def limpar_valor(x):
    """Limpa valores monetários e converte para float."""
    if isinstance(x, list):
        x = x[1]
    if isinstance(x, str):
        x = x.replace("R$", "").replace(" ", "")
        x = re.sub(r"(?<=\d)\.(?=\d{3})", "", x)
        x = x.replace(",", ".")
        try:
            return float(x)
        except ValueError:
            return None
    return x

def limpar_percentual(x):
    """Limpa valores percentuais e converte para float."""
    if isinstance(x, str):
        x = x.replace("%", "").replace(",", ".").strip()
        try:
            return float(x)
        except ValueError:
            return None
    return x

# -----------------------------
# Funções de carregamento
# -----------------------------
def carregar_balanco(company_id: int) -> pd.DataFrame:
    """Carrega balanço anual da empresa."""
    url = f"https://investidor10.com.br/api/balancos/balancoresultados/chart/{company_id}/5/yearly/"
    historico = get_json(url)
    df = pd.DataFrame(historico)
    df = df.rename(columns={
        df.columns[0]: "Indicador",
        df.columns[1]: "Atual",
        df.columns[2]: "2025",
        df.columns[4]: "2024",
        df.columns[7]: "2023",
        df.columns[10]: "2022",
        df.columns[13]: "2021"
    })
    return df.set_index("Indicador")

def separar_tabelas(df: pd.DataFrame):
    """Separa indicadores monetários e percentuais."""
    # Monetários
    monetarios = df.loc[df.index.str.contains(r"\(R\$\)"), ANOS]
    for col in ANOS:
        monetarios[col] = monetarios[col].apply(limpar_valor)
    monetarios = monetarios.apply(pd.to_numeric, errors="coerce").fillna(0)
    monetarios_bilhoes = monetarios / 1e9

    # Percentuais
    percentuais = df.loc[df.index.str.contains("%"), ANOS]
    for col in ANOS:
        percentuais[col] = percentuais[col].apply(limpar_percentual)

    return monetarios_bilhoes.round(2), percentuais.round(2)


def carregar_cotacoes(ticker: str) -> pd.DataFrame:
    """Carrega histórico de cotações do ticker."""
    url = f"https://investidor10.com.br/api/cotacao-lucro/{ticker}/"
    cotacao_data = get_json(url)
    df_cotacao = pd.DataFrame(cotacao_data).T
    df_cotacao = df_cotacao[df_cotacao.index.str.isdigit()]
    df_cotacao.index = df_cotacao.index.astype(str)
    df_cotacao["quotation"] = df_cotacao["quotation"].astype(float)
    return df_cotacao

def carregar_preco_atual(id: int) -> float:
    """Carrega preço atual da ação."""
    url = f"https://investidor10.com.br/api/cotacao/ticker/{id}"
    cotacao = get_json(url)
    return float(cotacao["price"])

def carregar_indicadores_completos(id: int) -> pd.DataFrame:
    """Carrega histórico de indicadores financeiros e organiza em formato tabular."""
    url = f"https://investidor10.com.br/api/historico-indicadores/{id}/5/?v=2"
    indicadores = get_json(url)

    # Transformar cada indicador em linhas com ano e valor
    registros = []
    for nome_indicador, valores in indicadores.items():
        for v in valores:
            registros.append({
                "indicador": nome_indicador,
                "ano": v.get("year"),
                "valor": v.get("value")
            })
    df = pd.DataFrame(registros)
    # Pivotar para ter anos como linhas e indicadores como colunas
    df = df.pivot(index="ano", columns="indicador", values="valor")

    df =  df[['LPA','P/VP','ROA','Payout','Giro Ativos']]
    df = df.reset_index().rename(columns={"ano": "Ano"})
    return df

# -----------------------------
# Consolidação dos dados
# -----------------------------
def juntar_dados(monetarios, cotacoes, preco_atual, indicadores, ticker: str, dividendo: pd.DataFrame):
    """Junta lucro líquido, Preço, indicadores e dividendos em um único DataFrame."""
    lucro_liquido = monetarios.loc["Lucro Líquido - (R$)", ANOS]

    final_df = pd.DataFrame({
        "Ticker": ticker,
        "Ano": ANOS,
        "Lucro Líquido (bi R$)": lucro_liquido.values
    })

    # adicionar Preço
    for ano in cotacoes.index:
        if ano in final_df["Ano"].values:
            final_df.loc[final_df["Ano"] == ano, "Preco"] = cotacoes.loc[ano, "quotation"]

    final_df.loc[final_df["Ano"] == "Atual", "Preco"] = preco_atual

    # indicadores: alinhar pelo ano
    indicadores["Ticker"] = ticker
    final_df = pd.merge(final_df, indicadores, on=["Ano", "Ticker"], how="left")

    dividendo["Ticker"] = ticker
    final_df = pd.merge(final_df, dividendo, on=["Ano", "Ticker"], how="left")

    return final_df

def dividendos_medio(acao):
    dividendos = get_json(f'https://investidor10.com.br/api/dividendos/chart/{acao}/1825')
    df = pd.DataFrame(dividendos)
    df = df.rename(columns={"created_at":"Ano","price":"dividendo"})
    df["Ano"] = df["Ano"].astype(str) # NAO PODE RETIRAR DEVIDO A COLUNA DE ANO
    df["Ano"] = df["Ano"].replace("Últ. 12M", "Atual")
    return df

# -----------------------------
# Execução principal
# -----------------------------

def parse_args():
    """Lê os tickers por parâmetros da linha de comando."""
    parser = argparse.ArgumentParser(
        description="Consulta dados financeiros por ticker(s) no Investidor10."
    )
    parser.add_argument(
        "tickers",
        nargs="*",
        help="Ticker(s) para consultar. Ex.: python lucroXcotacao.py UNIP6 CMIG4 ou python lucroXcotacao.py UNIP6,CMIG4",
    )
    return parser.parse_args()


if __name__ == "__main__":
    args = parse_args()
    tickers = normalizar_tickers(args.tickers) if args.tickers else ["UNIP6", "CMIG4","CMIN3","VALE3"]

    tickers_disponiveis = carregar_tickers_disponiveis()
    max_workers = min(9, len(tickers))
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        resultados = list(
            executor.map(
                lambda ticker: processar_ticker(ticker, tickers_disponiveis),
                tickers
            )
        )

    df_final = pd.concat(resultados, ignore_index=True)

    print("\nDados consolidados:")
    # print(df_final)
    # Salvar em CSV
    df_final.to_csv("dados_consolidados.csv", index=False)
