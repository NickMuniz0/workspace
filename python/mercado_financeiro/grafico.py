
#pip install pandas yfinance numpy matplotlib IPython requests  weasyprint openpyxl
import pandas as pd
import yfinance as yf
from datetime import datetime
import math
# from tqdm import tqdm
import numpy as np
import matplotlib.pyplot as plt
from IPython.display import HTML, display
import requests
from base64 import b64encode
from pprint import pprint
import re
import warnings
from dateutil.relativedelta import relativedelta
from carteira import CARTEIRA

warnings.simplefilter(action='ignore', category=FutureWarning)

valor_aplicado = 1000

tickers = requests.get('https://investidor10.com.br/api/get-tickers/', headers={'user-agent':'Mozilla/5.0'}).json()['data']
df = pd.DataFrame(tickers)

###############################################################################################################################
###############################################################################################################################
###############################################################################################################################
###############################################################################################################################
def format_bilhao_milhao(x):
    if abs(x) >= 1_000_000_000:
        return f'{x / 1_000_000_000:.2f} B'
    elif abs(x) >= 1_000_000:
        return f'{x / 1_000_000:.2f} M'
    else:
        return f'{x:.2f}'

def parse_percentage_to_float(value):
    if isinstance(value, str):
        clean_value = value.replace('%', '').replace(',', '.').strip()
        try:
            return float(clean_value)
        except ValueError:
            return np.nan
    return float(value)

def parse_bilhao_milhao_to_float(value_str):
    if value_str is None or not isinstance(value_str, str) or value_str.strip() == '-':
        return 0.0

    clean_str = value_str.strip().upper()

    # Remove 'R$', 'R$ ' prefixes
    clean_str = clean_str.replace('R$', '').strip()

    multiplier = 1.0
    if clean_str.endswith('B'):
        multiplier = 1_000_000_000.0
        clean_str = clean_str[:-1] # Remove 'B'
    elif clean_str.endswith('M'):
        multiplier = 1_000_000.0
        clean_str = clean_str[:-1] # Remove 'M'

    try:
        # Replace comma with dot for decimal conversion and remove spaces and dots for thousands separator
        num_part = clean_str.replace('.', '').replace(',', '.').replace(' ', '')
        if not num_part: # Handle cases where only unit was present, e.g., 'B'
            return 0.0
        return float(num_part) * multiplier
    except ValueError:
        return 0.0

def get_historical_price_profit_data(acao):
    """
    Busca dados históricos de cotação e lucro líquido para uma ação.
    Retorna um DataFrame com as colunas 'year', 'net_profit', 'quotation'.
    """
    try:
        cotacao_lucro_data = requests.get(f'https://investidor10.com.br/api/cotacao-lucro/{acao}/', headers={'user-agent':'Mozilla/5.0'}).json()
        df_history = pd.DataFrame(cotacao_lucro_data).astype(float).T
        df_history = df_history.reset_index().rename(columns={'index': 'year'})

        # Convert 'year' to numeric, coercing errors to NaN, then drop NaNs
        df_history['year'] = pd.to_numeric(df_history['year'], errors='coerce')
        df_history.dropna(subset=['year'], inplace=True)
        df_history['year'] = df_history['year'].astype(int) # Garante que o ano é um inteiro

        return df_history[['year', 'net_profit', 'quotation']].sort_values(by='year')
    except Exception as e:
        print(f"Erro ao buscar dados históricos de preço/lucro para {acao}: {e}")
        return pd.DataFrame()

def get_historical_costs(company_id):
    """
    Fetches historical cost data for a given company_id from the Investidor10 API.
    Extracts 'custos' time series, converts string values to floats, and returns
    a pandas DataFrame with 'year' and 'cost_value' columns.
    """
    try:
        # Step 2: Make a GET request to the API
        url = f'https://investidor10.com.br/api/balancos/balancoresultados/chart/{company_id}/5/yearly/'
        headers = {'user-agent': 'Mozilla/5.0'}
        response = requests.get(url, headers=headers)
        response.raise_for_status()  # Raise an exception for HTTP errors
        historico = response.json()

        # Check if historico has enough data and required indices
        if not historico or len(historico) < 3:
            print(f"Warning: Insufficient data in 'historico' for company_id {company_id}.")
            return pd.DataFrame(columns=['year', 'cost_value'])

        year_headers = historico[0]
        costs_data = historico[2]

        extracted_costs = []
        # Iterate through the indices of year_headers to find actual years
        # and match with corresponding cost values in costs_data.
        # Start from index 2 to skip '#' and 'ÚLT. 12M' which are not historical years.
        for i in range(2, len(year_headers)):
            year_candidate = year_headers[i]
            cost_candidate = costs_data[i]

            # Check if it's a valid year string (e.g., '2024')
            # and if the corresponding cost_candidate is a list (actual value) of length >= 2
            if isinstance(year_candidate, str) and year_candidate.isdigit():
                if isinstance(cost_candidate, list) and len(cost_candidate) >= 2:
                    year = int(year_candidate)
                    cost_str = cost_candidate[1] # The raw value is at index 1 of the inner list
                    cost_value = parse_bilhao_milhao_to_float(cost_str)
                    extracted_costs.append({'year': year, 'cost_value': cost_value})

        # Step 7: Create a pandas DataFrame
        costs_df = pd.DataFrame(extracted_costs)

        # Ensure 'year' is int and 'cost_value' is float
        if not costs_df.empty:
            costs_df['year'] = costs_df['year'].astype(int)
            costs_df['cost_value'] = costs_df['cost_value'].astype(float)
            costs_df = costs_df.sort_values(by='year').reset_index(drop=True)

        # Step 8: Return the DataFrame
        return costs_df

    except requests.exceptions.RequestException as e:
        print(f"Error fetching data for company_id {company_id}: {e}")
        return pd.DataFrame(columns=['year', 'cost_value'])
    except Exception as e:
        print(f"An unexpected error occurred for company_id {company_id}: {e}")
        return pd.DataFrame(columns=['year', 'cost_value'])

def get_historical_ebitda(company_id):
    """
    Fetches historical EBITDA data for a given company_id from the Investidor10 API.
    Extracts 'EBITDA' time series, converts string values to floats, and returns
    a pandas DataFrame with 'year' and 'ebitda_value' columns.
    """
    try:
        url = f'https://investidor10.com.br/api/balancos/balancoresultados/chart/{company_id}/5/yearly/'
        headers = {'user-agent': 'Mozilla/5.0'}
        response = requests.get(url, headers=headers)
        response.raise_for_status()  # Raise an exception for HTTP errors
        historico = response.json()

        if not historico or len(historico) < 6: # EBITDA is typically at index 5
            print(f"Warning: Insufficient data in 'historico' for company_id {company_id} to get EBITDA.")
            return pd.DataFrame(columns=['year', 'ebitda_value'])

        year_headers = historico[0]
        ebitda_data = historico[5] # EBITDA is at index 5 based on inspection

        extracted_ebitda = []
        for i in range(2, len(year_headers)):
            year_candidate = year_headers[i]
            ebitda_candidate = ebitda_data[i]

            if isinstance(year_candidate, str) and year_candidate.isdigit():
                if isinstance(ebitda_candidate, list) and len(ebitda_candidate) >= 2:
                    year = int(year_candidate)
                    ebitda_str = ebitda_candidate[1] # The raw value is at index 1 of the inner list
                    ebitda_value = parse_bilhao_milhao_to_float(ebitda_str)
                    extracted_ebitda.append({'year': year, 'ebitda_value': ebitda_value})

        ebitda_df = pd.DataFrame(extracted_ebitda)

        if not ebitda_df.empty:
            ebitda_df['year'] = ebitda_df['year'].astype(int)
            ebitda_df['ebitda_value'] = ebitda_df['ebitda_value'].astype(float)
            ebitda_df = ebitda_df.sort_values(by='year').reset_index(drop=True)

        return ebitda_df

    except requests.exceptions.RequestException as e:
        print(f"Error fetching EBITDA data for company_id {company_id}: {e}")
        return pd.DataFrame(columns=['year', 'ebitda_value'])
    except Exception as e:
        print(f"An unexpected error occurred for company_id {company_id} while fetching EBITDA: {e}")
        return pd.DataFrame(columns=['year', 'ebitda_value'])

def get_historico_indicador(id):
  historico_indicador =requests.get(f'https://investidor10.com.br/api/historico-indicadores/{id}/10?v=2', headers={'user-agent':'Mozilla/5.0'}).json()
  return historico_indicador

def get_historical_net_debt(company_id):
    """
    Fetches historical Net Debt data for a given company_id from the Investidor10 API.
    Extracts 'Dívida Líquida' time series, converts string values to floats, and returns
    a pandas DataFrame with 'year' and 'net_debt_value' columns.
    """
    try:
        url = f'https://investidor10.com.br/api/balancos/balancoresultados/chart/{company_id}/5/yearly/'
        headers = {'user-agent': 'Mozilla/5.0'}
        response = requests.get(url, headers=headers)
        response.raise_for_status()  # Raise an exception for HTTP errors
        historico = response.json()

        if not historico or len(historico) < 10: # Dívida Líquida is typically at index 9
            print(f"Warning: Insufficient data in 'historico' for company_id {company_id} to get Net Debt.")
            return pd.DataFrame(columns=['year', 'net_debt_value'])

        year_headers = historico[0]
        net_debt_data = historico[9] # Dívida Líquida is at index 9 based on inspection

        extracted_net_debt = []
        for i in range(2, len(year_headers)):
            year_candidate = year_headers[i]
            net_debt_candidate = net_debt_data[i]

            if isinstance(year_candidate, str) and year_candidate.isdigit():
                if isinstance(net_debt_candidate, list) and len(net_debt_candidate) >= 2:
                    year = int(year_candidate)
                    net_debt_str = net_debt_candidate[1] # The raw value is at index 1 of the inner list
                    net_debt_value = parse_bilhao_milhao_to_float(net_debt_str)
                    extracted_net_debt.append({'year': year, 'net_debt_value': net_debt_value})

        net_debt_df = pd.DataFrame(extracted_net_debt)

        if not net_debt_df.empty:
            net_debt_df['year'] = net_debt_df['year'].astype(int)
            net_debt_df['net_debt_value'] = net_debt_df['net_debt_value'].astype(float)
            net_debt_df = net_debt_df.sort_values(by='year').reset_index(drop=True)

        return net_debt_df

    except requests.exceptions.RequestException as e:
        print(f"Error fetching Net Debt data for company_id {company_id}: {e}")
        return pd.DataFrame(columns=['year', 'net_debt_value'])
    except Exception as e:
        print(f"An unexpected error occurred for company_id {company_id} while fetching Net Debt: {e}")
        return pd.DataFrame(columns=['year', 'net_debt_value'])

# Usa a variável CARTEIRA para a análise
selected_stocks = CARTEIRA
print(f"Usando ações da carteira para análise: {selected_stocks}")
historical_data_for_prediction = {} # This remains here, as it's part of the subsequent processing

for stock in selected_stocks:
    print(f"Coletando dados históricos para {stock}...")
    # Obter histórico de preço e lucro líquido
    price_profit_df = get_historical_price_profit_data(stock)

    # Ensure the stock exists in the df before proceeding
    if stock not in df['name'].values:
        print(f"Aviso: '{stock}' não encontrado na lista de tickers. Pulando.")
        continue

    stock_info = df[df['name']==stock]
    stock_id = stock_info['id'].values[0]
    company_id = stock_info['company_id'].values[0] # Ensure company_id is available here
    historico_indicador = get_historico_indicador(stock_id)

    # Função auxiliar para processar e mesclar indicadores
    def process_and_merge_indicator(current_df, indicator_key, new_col_name):
        if indicator_key in historico_indicador:
            indicator_df = pd.DataFrame(historico_indicador[indicator_key])
            indicator_df = indicator_df[indicator_df['year'] != 'Atual'] # Filtrar 'Atual' antes da conversão
            indicator_df['year'] = pd.to_numeric(indicator_df['year'], errors='coerce')
            indicator_df.dropna(subset=['year'], inplace=True)
            indicator_df['year'] = indicator_df['year'].astype(int)

            # Apply specific parsing for percentage values, otherwise convert to float
            if indicator_key in ['MARGEM LÍQUIDA', 'MARGEM BRUTA', 'MARGEM EBIT', 'ROE', 'ROIC', 'DIVIDEND YIELD (DY)']:
                indicator_df['value'] = indicator_df['value'].apply(parse_percentage_to_float)
            else:
                indicator_df['value'] = indicator_df['value'].replace('-', np.nan)
                indicator_df['value'] = pd.to_numeric(indicator_df['value'], errors='coerce')

            indicator_df = indicator_df.rename(columns={'value': new_col_name})[['year', new_col_name]]
            return pd.merge(current_df, indicator_df, on='year', how='outer')
        else:
            print(f"Aviso: Indicador '{indicator_key}' não encontrado para {stock}.")
            return current_df # Retorna o DF sem modificações se o indicador não for encontrado

    # Unir todos os dados históricos para a ação
    if not price_profit_df.empty:
        # Calculate VARIACAO_PRECO before merging other indicators
        price_profit_df['VARIACAO_PRECO'] = price_profit_df['quotation'].pct_change() * 100
        price_profit_df['VARIACAO_PRECO'] = price_profit_df['VARIACAO_PRECO'].replace([np.inf, -np.inf], np.nan)

        combined_stock_data = price_profit_df.copy() # Start with price and profit
        combined_stock_data = process_and_merge_indicator(combined_stock_data, 'P/L', 'P/L')
        combined_stock_data = process_and_merge_indicator(combined_stock_data, 'MARGEM LÍQUIDA', 'MARGEM LÍQUIDA')
        combined_stock_data = process_and_merge_indicator(combined_stock_data, 'GIRO ATIVOS', 'GIRO_ATIVOS') # Add Giro Ativos
        combined_stock_data = process_and_merge_indicator(combined_stock_data, 'DIVIDEND YIELD (DY)', 'DIVIDEND YIELD (DY)') # Add Dividend Yield

        # Call the new get_historical_costs function
        historical_costs_df = get_historical_costs(company_id)
        # Merge historical costs into combined_stock_data
        if not historical_costs_df.empty:
            combined_stock_data = pd.merge(combined_stock_data, historical_costs_df.rename(columns={'cost_value': 'CUSTO_HISTORICO'}), on='year', how='outer')

        # Call the new get_historical_ebitda function
        historical_ebitda_df = get_historical_ebitda(company_id)
        # Merge historical EBITDA into combined_stock_data
        if not historical_ebitda_df.empty:
            combined_stock_data = pd.merge(combined_stock_data, historical_ebitda_df.rename(columns={'ebitda_value': 'EBITDA_HISTORICO'}), on='year', how='outer')

        # Call the new get_historical_net_debt function
        historical_net_debt_df = get_historical_net_debt(company_id)
        if not historical_net_debt_df.empty:
            combined_stock_data = pd.merge(combined_stock_data, historical_net_debt_df, on='year', how='outer')
            # Calculate Net Debt / EBITDA ratio
            if 'net_debt_value' in combined_stock_data.columns and 'EBITDA_HISTORICO' in combined_stock_data.columns:
                combined_stock_data['NET_DEBT_EBITDA_RATIO'] = combined_stock_data['net_debt_value'] / combined_stock_data['EBITDA_HISTORICO']

        combined_stock_data = combined_stock_data.sort_values(by='year').set_index('year')
        historical_data_for_prediction[stock] = combined_stock_data
    else:
        print(f"Pulando a combinação para {stock} devido a dados históricos de preço/lucro vazios.")


from matplotlib.backends.backend_pdf import PdfPages
import matplotlib.pyplot as plt
from datetime import datetime
from pathlib import Path

# Get current date and time to include in the filename
now = datetime.now()
timestamp = datetime.now().strftime("%Y%m%d_%H%M%S") # YYYYMMDD_HHMMSS

# Define the output directory and PDF file path with dynamic timestamp (cross-platform)
output_dir = Path.cwd() / 'output'
output_dir.mkdir(parents=True, exist_ok=True)
pdf_file_name = str(output_dir / f'analise_preditiva_{timestamp}.pdf')

with PdfPages(pdf_file_name) as pdf:
    for stock, data in historical_data_for_prediction.items():
        if data.empty: # Check if data is empty for the current stock
            print(f"Não há dados suficientes para plotar para {stock}.")
            continue

        # Determine the number of subplots dynamically based on available data
        num_subplots = 1 # For Price and Net Profit
        # if 'P/L' in data.columns: num_subplots += 1
        # # ROE and ROA removed as per user request
        # if 'MARGEM LÍQUIDA' in data.columns: num_subplots += 1
        # if 'VARIACAO_PRECO' in data.columns: num_subplots += 1
        # if 'CUSTO_HISTORICO' in data.columns: num_subplots += 1
        # if 'EBITDA_HISTORICO' in data.columns: num_subplots += 1 # Added EBITDA
        # if 'DIVIDEND YIELD (DY)' in data.columns: num_subplots += 1
        # # GORDON_MODEL_PRICE plot removed as per user request
        # if 'NET_DEBT_EBITDA_RATIO' in data.columns: num_subplots += 1 # Added Net Debt/EBITDA Ratio

        fig, axes = plt.subplots(num_subplots, 1, figsize=(15, 6 * num_subplots), sharex=True)

        # Ensure axes is always an array, even if num_subplots is 1
        if num_subplots == 1:
            axes = [axes]

        fig.suptitle(f'Análise de Tendências Históricas para {stock}', fontsize=20, y=0.98)

        plot_idx = 0
        # Plot do Preço da Ação e Lucro Líquido (cotacao x lucro)
        ax1 = axes[plot_idx]
        ax1.plot(data.index, data['quotation'], marker='o', linestyle='-', color='blue', label='Preço (R$)')
        ax1.set_title('Preço da Ação e Lucro Líquido')
        ax1.set_ylabel('Preço (R$)', color='blue')
        ax1.tick_params(axis='y', labelcolor='blue')
        ax1.grid(True)

        ax2 = ax1.twinx() # Eixo Y secundário para o lucro
        ax2.plot(data.index, data['net_profit'], marker='x', linestyle='--', color='purple', label='Lucro Líquido')
        ax2.set_ylabel('Lucro Líquido (R$)', color='purple')
        ax2.tick_params(axis='y', labelcolor='purple')
        ax1.legend(loc='upper left')
        ax2.legend(loc='upper right')
        plot_idx += 1

        # # Plot do P/L (Preço/Lucro)
        # if 'P/L' in data.columns:
        #     axes[plot_idx].plot(data.index, data['P/L'], marker='o', linestyle='-', color='red')
        #     axes[plot_idx].set_title('P/L (Preço/Lucro)')
        #     axes[plot_idx].set_ylabel('P/L')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        # # ROE and ROA plots removed as per user request

        # # Plot da MARGEM LÍQUIDA
        # if 'MARGEM LÍQUIDA' in data.columns:
        #     axes[plot_idx].plot(data.index, data['MARGEM LÍQUIDA'], marker='o', linestyle='-', color='darkorange')
        #     axes[plot_idx].set_title('Margem Líquida')
        #     axes[plot_idx].set_ylabel('Margem Líquida (%)')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        # # Plot da VARIACAO_PRECO
        # if 'VARIACAO_PRECO' in data.columns:
        #     axes[plot_idx].plot(data.index, data['VARIACAO_PRECO'], marker='o', linestyle='-', color='brown')
        #     axes[plot_idx].set_title('Variação Percentual Anual do Preço')
        #     axes[plot_idx].set_xlabel('Ano')
        #     axes[plot_idx].set_ylabel('Variação (%)')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        # # New plot for 'net_profit' (lucro) and 'CUSTO_HISTORICO'
        # if 'CUSTO_HISTORICO' in data.columns:
        #     ax_profit_cost = axes[plot_idx]
        #     ax_profit_cost.plot(data.index, data['net_profit'], marker='o', linestyle='-', color='magenta', label='Lucro Líquido')
        #     ax_profit_cost.set_title('Lucro Líquido vs. Custo Histórico')
        #     ax_profit_cost.set_ylabel('Lucro Líquido (R$)', color='magenta')
        #     ax_profit_cost.tick_params(axis='y', labelcolor='magenta')
        #     ax_profit_cost.grid(True)

        #     ax_cost_twin = ax_profit_cost.twinx()
        #     ax_cost_twin.plot(data.index, data['CUSTO_HISTORICO'], marker='x', linestyle='--', color='teal', label='Custo Histórico')
        #     ax_cost_twin.set_ylabel('Custo Histórico (R$)', color='teal')
        #     ax_cost_twin.tick_params(axis='y', labelcolor='teal')
        #     ax_profit_cost.legend(loc='upper left')
        #     ax_cost_twin.legend(loc='upper right')
        #     plot_idx += 1

        # # New plot for 'EBITDA_HISTORICO'
        # if 'EBITDA_HISTORICO' in data.columns:
        #     axes[plot_idx].plot(data.index, data['EBITDA_HISTORICO'], marker='o', linestyle='-', color='blueviolet')
        #     axes[plot_idx].set_title('EBITDA Histórico')
        #     axes[plot_idx].set_ylabel('EBITDA (R$)')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        # # New plot for 'DIVIDEND YIELD (DY)'
        # if 'DIVIDEND YIELD (DY)' in data.columns:
        #     axes[plot_idx].plot(data.index, data['DIVIDEND YIELD (DY)'], marker='o', linestyle='-', color='darkcyan')
        #     axes[plot_idx].set_title('Dividend Yield (DY)')
        #     axes[plot_idx].set_ylabel('Dividend Yield (%)')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        # # Gordon Model Price plot removed as per user request

        # # New plot for Net Debt/EBITDA Ratio
        # if 'NET_DEBT_EBITDA_RATIO' in data.columns:
        #     axes[plot_idx].plot(data.index, data['NET_DEBT_EBITDA_RATIO'], marker='o', linestyle='-', color='gray')
        #     axes[plot_idx].set_title('Endividamento (Dívida Líquida / EBITDA)')
        #     axes[plot_idx].set_ylabel('Dívida Líquida / EBITDA')
        #     axes[plot_idx].set_xlabel('Ano')
        #     axes[plot_idx].grid(True)
        #     plot_idx += 1

        plt.tight_layout(rect=[0, 0.03, 1, 0.96]) # Ajusta o layout para evitar sobreposição
        pdf.savefig(fig) # Salva a figura atual no PDF
        plt.close(fig) # Fecha a figura para liberar memória

print(f"Todos os gráficos foram exportados para o PDF: {pdf_file_name}")
