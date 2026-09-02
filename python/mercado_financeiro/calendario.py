from carteira import CARTEIRA
import requests
import pandas as pd
from tabulate import tabulate
from bs4 import BeautifulSoup

# historico = requests.get(f'https://investidor10.com.br/api/balancos/balancoresultados/chart/2/5/yearly/', headers={'user-agent':'Mozilla/5.0'}).json()
# print(historico)
# for i in range(2, len(historico)):
#     print(historico[i][0])

class Calendario:
    def __init__(self,anos=5):
            self.lista_de_itens  = []
            self.item            = {}
            self.df              = None
            self.id              = None
            self.company_id      = None
            self.anos            = anos
            self.acao            = None
            self.HEADERS         = {'user-agent':'Mozilla/5.0'}
            self.CARTEIRA        = CARTEIRA
            self.start()

    def start(self):
        for acao in CARTEIRA:
            self.item = {}
            self.acao = acao
            self.controle()
            self.lista_de_itens.append(self.item)

        self.cria_dataframe()
        self.show()
            
    def show(self):

        print(tabulate(self.df, headers="keys", tablefmt="github")  )

    def controle(self):
        self.tickers()
        self.calendario_dividendos()

    def cria_dataframe(self):
        self.df = pd.DataFrame(self.lista_de_itens)

    def tickers(self):
        tickers = requests.get('https://investidor10.com.br/api/get-tickers/', headers=self.HEADERS).json()['data']
        df = pd.DataFrame(tickers)

        # crie uma coluna e adicione o nome da ação a cada linha do DataFrame
        # Usar a lista de tickers para selecionar os ativos do DataFrame
        ativos = df[df['name'].isin(self.CARTEIRA)][['id', 'company_id', 'name']]
        dado = ativos[ativos["name"] == self.acao][['id', 'company_id']]
        self.id = dado['id'].values[0]
        self.company_id = dado['company_id'].values[0]
        self.item['ticket'] = self.acao

    def calendario_dividendos(self):
        dividendos = requests.get(f'https://investidor10.com.br/api/acoes/dividends-map?filter=companies={self.company_id}', headers=self.HEADERS).json()
        html = dividendos['dividendsTable']
        soup = BeautifulSoup(html, 'html.parser')
        months_with_dividends = []
        for td in soup.find_all('td', class_='td'):
            if td.find('div', class_='dividend-icon-container'):
                tooltip = td.find('span', class_='tooltiptext-percentage')
                if tooltip:
                    month_id = tooltip.get('id')
                    if month_id:
                        month_num = int(month_id.split('-')[1])
                        percentage_text = tooltip.find('h5').text
                        percentage = int(percentage_text.strip('%'))
                        months_with_dividends.append({'month': month_num, 'probability': percentage})
        high_prob_months = [d['month'] for d in months_with_dividends if d['probability'] > 70]
        month_names = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
        data = {'ticket': self.acao}
        for i, name in enumerate(month_names, 1):
            data[name] = 'x' if i in high_prob_months else ''
        self.item = data


Calendario(anos=3)
