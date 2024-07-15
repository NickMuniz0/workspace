import pandas as pd 
import yfinance as yf
from datetime import datetime
import math
from tqdm import tqdm
import numpy as np
import matplotlib.pyplot as plt  


# https://github.com/ranaroussi/yfinance
# https://pypi.org/project/yfinance/

class ACAO():
        
    def __init__(self,quantidade_de_acao_prospectada,ativos,CARTEIRA) -> None:
        self.ativos=ativos
        self.quantidade_de_acao_prospectada = quantidade_de_acao_prospectada
        self.carteira=CARTEIRA

    def start(self):
        df =pd.DataFrame([],columns=['VALOR_PROSPECTADO_DIV_MEDIO','VALOR_REAL_DIV_MEDIO','ACAO','VARIACAO','PRECO','VALOR_COMPRA','PRECOJUSTO','PRECOTETO_8','PRECOTETO_10','DIV.MEDIO'])
        print("VALOR A APLICAR:",self.quantidade_de_acao_prospectada)
        for x,y in enumerate(self.ativos):
            self.get_dados(x,y,df)
        df = df.sort_values(['VALOR_PROSPECTADO_DIV_MEDIO'], ascending=[False])

        # print(df)
        print('DIVIDENDO TOTAL ANO:',df['VALOR_REAL_DIV_MEDIO'].sum())
        
        print(df[df['VARIACAO']<0])

        # self.UP_DOWN(df)

    def get_dados(self,index,acao,df):
        aapl = yf.Ticker(f"{acao}.SA")
        VPA=aapl.info['bookValue']
        PL = aapl.info['trailingPE']
        LPA =aapl.info['trailingEps']
        num= (2.25* PL* VPA*LPA)
        preco_justo = math.pow(num, 1/2)
        dividendo_medio_5= pd.Series(aapl.dividends.resample('YE').sum()).tail(5).mean()
        preco_teto = round(dividendo_medio_5/0.06,2)
        df.loc[index,'ACAO']=acao
        df.loc[index,'PRECO']=aapl.info['currentPrice']
        df.loc[index,'VARIACAO']= round(aapl.info['52WeekChange']*100,2)
        df.loc[index,'PRECOJUSTO']= round(preco_justo,2)
        df.loc[index,'VALOR_COMPRA']= round(preco_justo*0.94,2)
        df.loc[index,'DIV.MEDIO']=float(dividendo_medio_5)
        df.loc[index,'VALOR_PROSPECTADO_DIV_MEDIO'] =   round((self.quantidade_de_acao_prospectada/aapl.info['currentPrice']) * dividendo_medio_5,2)
        df.loc[index,'VALOR_REAL_DIV_MEDIO']=round(self.carteira[index]*dividendo_medio_5,2)
        df.loc[index,'PRECOTETO_8']=(float(dividendo_medio_5)/0.08)
        df.loc[index,'PRECOTETO_10']=(float(dividendo_medio_5)/0.1)








    def lucro_por_preco(self,acao):
        aapl = yf.Ticker(f"{acao}.SA")
        dx= aapl.financials
        ########################################################################################
        lucro_liquido= dx[dx.index.isin(['Net Income'])]
        lucro_liquido =lucro_liquido.transpose()
        lucro_liquido=lucro_liquido.infer_objects(copy=False).fillna(0)# type: ignore
        lucro_liquido.index.name = 'Date'
        lucro_liquido.index= pd.to_datetime(lucro_liquido.index).date
        ########################################################################################
        dp=aapl.history(period="5y").reset_index()
        dp['Date'] = pd.to_datetime(dp['Date'])
        dp=dp.resample(on="Date", rule="ME").max().reset_index(drop=False)
        dp['year'] = dp['Date'].dt.year
        dp['month'] = dp['Date'].dt.month
        dp['Date'] =  dp['Date'].dt.date
        dp1 = dp[dp['month']==12]
        # dp1.loc[:, ['Close']] =dp1['Close'] * 10000000
        dp2 = dp1[['Date','Close']].set_index(['Date'])
        ########################################################################################
        dx=pd.concat([dp2,lucro_liquido],axis=1 ).reset_index()
        dx=dx.infer_objects(copy=False).fillna(0) # type: ignore
        dx.rename(columns={'index': 'Data', 'Close': 'Preco','Net Income':'Lucro'}, inplace=True)
        dx.sort_values(['Data'], ascending=[False])

        fig, ax1 = plt.subplots(1,1,num=acao)
        dx.Preco.plot(ax=ax1, color='blue', label='Preco')
        ax2 = ax1.twinx()
        dx.Lucro.plot(ax=ax2, color='green', label='Lucro')
        ax1.set_ylabel('Preco')
        ax2.set_ylabel('Lucro')
        ax1.legend(loc=3)
        ax2.legend(loc=0) # type: ignore
        ax1.set_xticklabels(dx.Data)
        ax1.set_xticks(np.arange(len(dx.Data)))
        # plt.savefig(f'{acao}.png')
        

    def UP_DOWN(self,dx):
        dx.plot(kind="bar",x="ACAO",y=['PRECO','VALOR_COMPRA'])
        plt.show()


CARTEIRA= [500,700,1500,600,0,0,0,0,0,0,0]
ATIVOS =  ['CXSE3','BBSE3','BBAS3','TAEE11','CSMG3','UNIP6','TRPL4','CMIN3','CSNA3','VALE3']
valor_prospectado = 17000
ACAO(valor_prospectado,ATIVOS,CARTEIRA).start()
# ACAO(valor_aplicado,ativos,CARTEIRA).lucro_por_preco("TRPL4")



