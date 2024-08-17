import pandas as pd
import awswrangler as wr
import re
class Teste:
    nome:str
    id:str

objeto = Teste()
objeto.ano =2024
objeto.mes=6
objeto.dia=20
objeto.hor=4
objeto.nome="NICK"
objeto.id="2"

teste =objeto.__dict__
lista = list()
lista.append(teste)
partitions=['ano','mes','dia','hor']
path_0 = "./temp_emissao"
path =f"{path_0}ano={objeto.ano}/mes={objeto.mes}/dia={objeto.dia}/hor={objeto.hor}"
df =pd.DataFrame(lista)

try:
    wr.s3.to_parquet(path=path_0,df=df, partition_cols = partitions, dataset=True,mode='overwrite')

except Exception as e:
      print(e)

