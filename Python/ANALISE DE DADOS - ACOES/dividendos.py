import pandas as pd 
import yfinance as yf
# https://github.com/ranaroussi/yfinance
# https://pypi.org/project/yfinance/
aapl = yf.Ticker(f"CXSE3.SA")
dividends=aapl.dividends
annual_income = pd.Series(dividends.resample('YE').sum()).tail(5).mean()
# history = aapl.history(period='max')['Close']
print(annual_income)