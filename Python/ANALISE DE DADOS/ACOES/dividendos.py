import pandas as pd 
import yfinance as yf
from pprint import pprint
# https://github.com/ranaroussi/yfinance
# https://pypi.org/project/yfinance/
pd.set_option('display.max_rows', None)

aapl = yf.Ticker(f"VALE3.SA")
# dividends=aapl.dividends
# annual_income = pd.Series(dividends.resample('YE').sum()).tail(5).mean()
# history = aapl.history(period='max')['Close']
# print(annual_income)
pprint(aapl.get_shares_full())






##########################################################




# from yahooquery import Ticker
# aapl = Ticker('VALE3.SA')
# aaplInfo = aapl.key_stats
# aaplInfo = aapl.summary_detail
# aaplInfo = aapl.esg_scores
# aaplInfo = aapl.quotes
# aaplInfo = aapl.all_modules
# pprint(aaplInfo)

