from carteira import CARTEIRA
import requests
import pandas as pd
import numpy as np
from tabulate import tabulate
from bs4 import BeautifulSoup


# (Set-ExecutionPolicy -Scope Process -ExecutionPolicy RemoteSigned) ; (& C:\Users\nickp\OneDrive\Documentos\GitHub\workspace\python\mercado_financeiro\.venv\Scripts\activate) ; cd .\python\mercado_financeiro\invest10\ ; python -m main     
# 
# 
#                                                                                                                 
# historico = requests.get(f'https://investidor10.com.br/api/balancos/balancoresultados/chart/2/5/yearly/', headers=self.HEADERS).json()
# print(historico)
# for i in range(2, len(historico)):
#     print(historico[i][0])

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
            self.qtd             = 1000
            self.reais           = 10000
            self.start()

    def start(self):
        for acao in self.CARTEIRA:
            self.item = {}
            self.acao = acao
            self.controle()
            self.lista_de_itens.append(self.item)

        # self.acao = "TAEE3"
        # self.controle()
        # self.lista_de_itens.append(self.item)

        self.cria_dataframe()
        self.preco_teto()
        self.sinal()
        self.show()
            
    def show(self):
        self.df = self.df.sort_values(by = ['quantidade'],ascending=False)
        self.df = self.df[['SINAL' ,'variacao','ticket','preco','preco_medio_max','preco_medio','preco_teto','dividendo_medio','quantidade','P/L','P/L_medio','P/VP','ROE']]
        self.empresas_com_preco_teto_valido()


    def controle(self):
        self.tickers()
        self.preco()
        self.variacao()
        self.preco_medio()
        self.dividendos_medio()
        self.indicadores()
        self.preco_dividendo()
        self.earning_yield()

    def cria_dataframe(self):
        self.df = pd.DataFrame(self.lista_de_itens)

    def earning_yield(self):
        self.item['EY'] = round( (self.item['LPA'] / self.item['preco'] )*100 , 2) if self.item['preco'] !=0 else 0

    def preco_dividendo(self):
        self.item['preco_div'] = round( (self.qtd / self.item['preco'] )* self.item['dividendo_medio'], 2) if self.item['preco'] !=0 else 0
        self.item['preco_div_per'] = round( ( self.item['dividendo_medio'] / self.item['preco'] )*100 , 2) if self.item['preco'] !=0 else 0
        self.item['quantidade'] = round( ((  self.reais/ self.item['preco'] ) * self.item['dividendo_medio'])/12 , 2) if self.item['preco'] !=0 else 0

    def empresas_com_preco_teto_valido(self):
        self.df = self.df[self.df['SINAL'] == 'medio']
        print(tabulate(self.df, headers="keys", tablefmt="github")  )



    def tickers(self):
        tickers = requests.get('https://investidor10.com.br/api/get-tickers/', headers=self.HEADERS).json()['data']
        df = pd.DataFrame(tickers)

        ativos = df[df['name'].isin(self.CARTEIRA)][['id', 'company_id', 'name']]
        dado = ativos[ativos["name"] == self.acao][['id', 'company_id']]
        self.id = dado['id'].values[0]
        self.company_id = dado['company_id'].values[0]
        self.item['ticket'] = self.acao

    def preco_medio(self):
        cotacao_lucro_data = requests.get(f'https://investidor10.com.br/api/cotacao-lucro/{self.acao}/', headers=self.HEADERS).json()
        df = pd.DataFrame(cotacao_lucro_data).T                     
        df = df[df.index.str.isdigit()]  
        df.index = df.index.astype(int)
        df['quotation'] = df['quotation'].astype(float)
        self.item['preco_medio'] = round(df.sort_index().tail(self.anos)['quotation'].mean(), 2)

    def preco(self):
        cotacao = requests.get(f'https://investidor10.com.br/api/cotacao/ticker/{self.id}', headers=self.HEADERS).json()
        self.item['preco'] = cotacao['price']

    def dividendos_medio(self):
        dividendos = requests.get(f'https://investidor10.com.br/api/dividendos/chart/{self.acao}/1825', headers=self.HEADERS).json()
        df = pd.DataFrame(dividendos)
        self.item['dividendo_medio'] = round(df.sort_index().tail(self.anos)['price'].mean(),2)

    def variacao(self):
        dividendos = requests.get(f'https://investidor10.com.br/api/cotacoes/acao/chart/{self.acao}/', headers=self.HEADERS).json()['real']
        df = pd.DataFrame(dividendos)
        #Primeiro Preco
        preco_inicial = df.iloc[0]['price']
        #Ultimo Preco
        preco_final = df.iloc[-1]['price']
        #Variacao percentual
        variacao_percentual = round(((preco_final - preco_inicial) / preco_inicial) * 100, 2)
        self.item['preco_inicial'] = round(preco_inicial,2)
        self.item['preco_final'] = round(df.max()['price'],2)

        self.item['preco_medio_max'] = round((self.item['preco_inicial'] + self.item['preco_final']) / 2, 2)
        self.item['variacao'] = variacao_percentual



    def indicador_dados(self,historico_indicador,indicador):

        if indicador not in historico_indicador:
            self.item[f'{indicador}'] = 0
            self.item[f'{indicador}_medio'] = 0
            return
        pl_df = pd.DataFrame(historico_indicador[indicador])[['value', 'year']]

        #garantir que "value" seja float; '-' vira NaN, vírgula vira ponto
        pl_df['value'] = (
            pl_df['value']
            .astype(str)
            .str.replace(',', '.', regex=False)      # se houver vírgulas
            .replace('-', 0)                    # valores faltantes
        )
        pl_df['value'] = pd.to_numeric(pl_df['value'], errors='coerce')

        # ano também pode ser útil como inteiro
        pl_df['year'] = pd.to_numeric(pl_df['year'], errors='coerce').astype('Int64')

        # pegar o primeiro registro como “atual” e a média dos seguintes três
        pl_atual = round(pl_df.iloc[0]['value'], 2)
        pl_medio = round(pl_df['value'].iloc[:abs(self.anos)].mean(), 2)

        self.item[f'{indicador}'] = pl_atual
        self.item[f'{indicador}_medio'] = pl_medio

    def indicadores(self):
        historico_indicador =requests.get(f'https://investidor10.com.br/api/historico-indicadores/{self.id}/10?v=2', headers=self.HEADERS).json()
        self.indicador_dados(historico_indicador,"P/L")
        self.indicador_dados(historico_indicador,"P/VP")
        self.indicador_dados(historico_indicador,"ROE")
        self.indicador_dados(historico_indicador,"ROA")
        self.indicador_dados(historico_indicador,"LPA")        
        self.indicador_dados(historico_indicador,"ROIC")
        self.indicador_dados(historico_indicador,"Dívida Líquida / Ebitda")

    def preco_teto(self):
        self.df['preco_teto'] = round((self.df['dividendo_medio']/0.09),2)

    def sinal(self):
            self.df['SINAL'] = np.where(
                (pd.to_numeric(self.df['ROE'], errors='coerce')
                                > 0)
                                &
                (pd.to_numeric(self.df['preco'], errors='coerce')
                 < pd.to_numeric(self.df['preco_medio_max'], errors='coerce'))
                 &
                 (pd.to_numeric(self.df['preco'], errors='coerce')
                                  < pd.to_numeric(self.df['preco_medio'], errors='coerce'))
                &
                (pd.to_numeric(self.df['preco'], errors='coerce')
                                 < pd.to_numeric(self.df['preco_teto'], errors='coerce'))
                                               
                ,'medio', ''
            )

anos=5
Ativo(anos=anos)

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



