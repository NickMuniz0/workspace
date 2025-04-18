import pandas as pd 

df = pd.DataFrame([
    {"ID":"00000","SITU":"EMITIDO","SEGMENTO":"PF","TIPO_VINCULO":"CARTAO","VLR":2000,"ORIGEM":"LEGADO","CODIGO":"PIX06","VERSAO":"01","SIGLA":"AB7"},
    {"ID":"00001","SITU":"EMITIDO","SEGMENTO":"PJ","TIPO_VINCULO":"CARTAO","VLR":3000,"ORIGEM":"","CODIGO":"PIX02","VERSAO":"01","SIGLA":"CK"},
    {"ID":"00002","SITU":"EMITIDO","SEGMENTO":"PJ","TIPO_VINCULO":"CONTA","VLR":4000,"ORIGEM":"","CODIGO":"PIX06","VERSAO":"01","SIGLA":"AB7"},
    {"ID":"00004","SITU":"EMITIDO","SEGMENTO":"PF","TIPO_VINCULO":"CONTA","VLR":5000,"ORIGEM":"LEGADO","CODIGO":"PIX06","VERSAO":"01","SIGLA":"CK"},
    {"ID":"00005","SITU":"EMITIDO","SEGMENTO":"PF","TIPO_VINCULO":"CONTA","VLR":6000,"ORIGEM":"","CODIGO":"PIX06","VERSAO":"01","SIGLA":"AB7"},
    {"ID":"00006","SITU":"EMITIDO","SEGMENTO":"PF","TIPO_VINCULO":"CONTA","VLR":7000,"ORIGEM":"LEGADO","CODIGO":"PIX06","VERSAO":"01","SIGLA":"AB7"},

])
lista_colunas = ['SIGLA','CODIGO',"VERSAO","SEGMENTO","ORIGEM"]
df1 = df.groupby(lista_colunas)[lista_colunas].value_counts().reset_index().rename(columns={'count': 'QUANTIDADE'})
# print(df1)

df1['ORIGEM'] =df1['ORIGEM'].str.contains('LEGADO').map({True: 'LEGADO',False: 'MODERNIZADO'})
# print(df1)

############################## LEGADO|MODERNIZADO

lista_colunas =['ORIGEM']
df2 = df1.groupby(lista_colunas)[lista_colunas].value_counts().reset_index().rename(columns={'count': 'QUANTIDADE',"ORIGEM":"CLASSIFICACAO"})
# print(df2)


############################## PF|PJ
lista_colunas =['SEGMENTO']
df3 = df1.groupby(lista_colunas)[lista_colunas].value_counts().reset_index().rename(columns={'count': 'QUANTIDADE',"SEGMENTO":"CLASSIFICACAO"})
# print(df3)

############################## CONTROL TOWER
df_tower =pd.concat([df2,df3])
df_tower['PERCENTAGEM'] = df_tower['QUANTIDADE'].apply(lambda x : round(x/ df_tower['QUANTIDADE'].sum(),2)*100 )
df_tower =df_tower.reset_index(drop=True)
print(df_tower)