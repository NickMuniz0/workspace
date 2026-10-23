from carteira import CARTEIRA
import requests
import pandas as pd
from tabulate import tabulate
import subprocess
import sys
import time
from pathlib import Path


def get_json(url, headers):
    response = requests.get(url, headers=headers, timeout=(5, 10))
    response.raise_for_status()
    return response.json()


class Ativo:
    def __init__(self,anos):
            self.lista_de_itens  = []
            self.item            = {}
            self.df              = None
            self.id              = None
            self.company_id      = None
            self.anos            = anos
            self.acao            = None
            self.HEADERS         = {'user-agent':'Mozilla/5.0'}
            self.CARTEIRA        = CARTEIRA
            # self.CARTEIRA        = ["VALE3"]
            self.start()

    def start(self):

        self.show()

    def show(self):
        self.empresas_com_preco_teto_valido()


    def empresas_com_preco_teto_valido(self):
        inicio = time.perf_counter()
        try:
            # tickers = ",".join(self.df['ticket'].unique().tolist())
            tickers = ",".join(self.CARTEIRA)

            # Executa o script externo
            subprocess.run(
                [sys.executable, str(Path(__file__).with_name("lucroXcotacao.py")), tickers],
                cwd=Path(__file__).parent,
                check=True
            )

            # Carrega o CSV gerado pelo lucroXcotacao.py
            dados_consolidados = pd.read_csv("dados_consolidados.csv")

            # Gera relatório refinado com base nos dados consolidados
            self.gerar_tabela_semaforo(dados_consolidados)
        finally:
            duracao = time.perf_counter() - inicio
            print(f"Tempo de execução de empresas_com_preco_teto_valido: {duracao:.2f} segundos")

    def avaliar_dividendos(self, dividendo_medio):
        resultado =""
        if dividendo_medio >= 3 :
            resultado = "Dividendos alto"
        elif 1 < dividendo_medio <3:
            resultado = "Dividendos medio"
        else:
            resultado = "Dividendos fracos"

        return resultado



    def avaliar_desconto(self, preco_atual, preco_medio, preco_max):
        desvalorizacao_media = ((preco_atual - preco_medio) / preco_medio) * 100
        desvalorizacao_max = ((preco_atual - preco_max) / preco_max) * 100
        resultado = ""
        if desvalorizacao_media > 0:
            resultado = ""
        elif desvalorizacao_media <= -20 or desvalorizacao_max <= -30:
            resultado = "Forte Desconto"
        elif desvalorizacao_media <= -10 or desvalorizacao_max <= -15:
            resultado = "Moderado Desconto"
        else:
            resultado = "Leve Desconto"

        return resultado


    def gerar_relatorio(self, dados_consolidados):
        relatorio = []
        for ticker, grupo in dados_consolidados.groupby("Ticker"):
            grupo = grupo.sort_values("Ano").tail(self.anos)
            lucro_series = grupo["Lucro Líquido (bi R$)"].tolist()
            payout_series = grupo["Payout"].tolist()
            dividendo_series = grupo["dividendo"].tolist()
            preco_series = grupo["Preco"].tolist()

            # Valores atuais
            lucro_atual = round(lucro_series[-1],2)
            payout_atual = payout_series[-1]
            dividendo_atual = dividendo_series[-1]
            dividendo_medio = round(grupo["dividendo"].mean(),2)
            preco_atual = round(preco_series[-1],2)

            # Médias históricas
            lucro_medio = round(grupo["Lucro Líquido (bi R$)"].mean(),2)
            preco_medio = round(grupo["Preco"].mean(),2)
            preco_max = grupo["Preco"].max()

            # Tendência de lucro
            tendencia_lucro = "Crescente" if lucro_series[-1] > lucro_series[-3] else "Decrescente"



            # 1. Dividendos
            dividendos_status = self.avaliar_dividendos(dividendo_medio)

            # 3. Desconto consolidado
            desconto_status = self.avaliar_desconto(preco_atual, preco_medio, preco_max)
            qtd = (20000/preco_atual)
            relatorio.append({
                "Ticker": ticker,
                "GANHO": qtd*dividendo_medio,
                "QTD": qtd,
                "Preço Atual": preco_atual,
                "Preço Médio": preco_medio,
                "Lucro Atual": lucro_atual,
                "Lucro Médio": lucro_medio,
                "Preco Teto": round(dividendo_medio/0.06,2),
                "Lucro": tendencia_lucro,
                "Dividendos":dividendo_medio,
                "Desconto":desconto_status,
                "Payout":payout_atual,
                "Margem": (1- (preco_atual /preco_medio)) * 100,
            })

        df = pd.DataFrame(relatorio)
        df = df.sort_values(by="GANHO", ascending=False)
        return df

    def gerar_tabela_semaforo(self, dados_consolidados):
        df = self.gerar_relatorio(dados_consolidados)

        sinal_compra = []

        for _, row in df.iterrows():
            desconto = row["Desconto"]

            if "Forte Desconto" in desconto :
                sinal_compra.append("🟢 Compra")
            elif "Moderado Desconto" in desconto :
                sinal_compra.append("🟡 Observação")
            else:
                sinal_compra.append("🔴 Evitar")

        df["Sinal de Compra"] = sinal_compra

        print("\n=== Ranking de Atratividade (Tabela com Semáforo + Sinal de Compra) ===\n")
        print(tabulate(df, headers="keys", tablefmt="github"))

        return df




Ativo(anos=4)
# print("iniciando")
# dividendos = requests.get(f'https://investidor10.com.br/api/cotacoes/acao/chart/PETR4/', headers={'user-agent':'Mozilla/5.0'}).json()['real']
# print(dividendos)
'''
    P/L < 10                                        | Quanto paga pelo lucro
        Preço/Lucro : cara ou barata
    P/VP < 1                                        | Quanto paga pelo patrimônio líquido
        Preço/Valor Patrimonial : cara ou barata

    P/RECEITA (PSR) <= 1 ou abaixo do mercado       | Quanto se paga por cada unidade de venda

    EV/EBITDA <= 10                                 | Valor de mercado (preco x número de ações) + dívida líquida / EBITDA (lucro antes de juros, impostos, depreciação e amortização)

    ROE > 15%                                       | Capacidade de gerar lucro com o patrimônio líquido investido
    (lucro liquido / patrimonio liquido)*100

    ROA  5% - 10 %  (Exceto :algumas empresas)      | Retorno sobre o ativo
            
    DIVIDEND YIELD > 6%                             | Quanto se recebe de dividendos em relação ao preço da ação
    (dividendos por ação / preço da ação)*100

    LPA > 1                                         | Lucro por ação
    (lucro liquido / número de ações)
    
    Revenue per share  ~15–20% é atrativa;
                       para empresas maduras, 3–7% pode ser aceitável.                            | Receita por ação
    (receita total / número de ações)

    raiz de 22.5 * LPA*VPA
    
    RATIO > 1 mais ativos correntes ,cobre dividas a curto prazo

    Margem liguida = o que sobra como lucro da receita  apos dedução de custos e despesa
                    serviço         20-30
                    comercio/varejo 10-20
                    industria       7 -12
                    e-comerce       7 -10


    - Current Ratio (Liquidez Corrente): inclui estoques nos ativos circulantes.
    - Quick Ratio (Liquidez Seca): mais conservador, exclui estoques.
    - Cash Ratio (Liquidez Imediata): considera apenas caixa e equivalentes de caixa.


    - Debt to Equity → mede alavancagem (quanto a empresa depende de dívida).
    - Revenue per Share → mede eficiência de geração de receita por ação
            - Um valor 9,05029 significa que, em média, cada ação representa R$ 9,05 de receita anual.

    - FCO fluxo_caixa_operacional → mede a capacidade de gerar caixa com as operações.
        - Se o FCO é positivo e crescente ao longo dos anos, significa que o negócio principal é saudável.
        - Se o FCO é negativo ou instável, pode indicar problemas na operação ou forte dependência de financiamento externo

        - Compare os valores ano a ano.
        - Crescimento consistente do FCO e FCL → empresa sólida.
        - FCO crescente mas FCL negativo → empresa investindo pesado (expansão).
        - FCO e FCL ambos negativos → alerta de risco


    - FCL fluxo_caixa_livre → mede o caixa que sobra depois dos investimentos necessários.
        - Se o FCL é positivo, a empresa tem dinheiro livre para pagar dividendos, reduzir dívidas ou reinvestir.
        - Se o FCL é negativo, pode indicar que os investimentos estão consumindo mais caixa do que o gerado — o que não é necessariamente ruim se for para expansão, mas exige atenção.

        - Debt to Equity → se a empresa tem muita dívida, um FCL negativo pode ser perigoso.
        - Margens (EBITDA, Operacional) → ajudam a entender se os lucros estão se traduzindo em caixa.
        - Dividendos pagos → só são sustentáveis se o FCL for positivo.


    - FCO mostra se o negócio gera caixa.
    - FCL mostra se sobra dinheiro depois dos investimentos.


    
    - ROE e ROA elevados → empresa eficiente e rentável.
    - Liquidez seca altíssima → segurança financeira.
    - FCO e FCL positivos → geração de caixa saudável.
    - Baixa alavancagem → pouco risco de endividamento.
https://investidor10.com.br/api/cotacoes/acao/chart/BRAP4/

                                                            if (quotationVariable) {
                                                                return {
                                                                    firstPriceCotation: {
                                                                        price: Number(selectedData[0][quotationVariable])
                                                                    },
                                                                    lastPriceCotation: {
                                                                        price: Number(selectedData[selectedData.length - 1][quotationVariable])
                                                                    }
                                                                };
((precoFinal - precoInicial) / precoInicial) * 100;

    '''
