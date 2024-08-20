import pandas as pd
import fastparquet
import os

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
path = f"s3://{bucket_name}/{folders}/"
df =pd.DataFrame(lista)

try:
    df.to_parquet(path=path, partition_on = partitions, engine='fastparquet',storage_options={"client_kwargs":{"region_name": os.environ.get("AWS_REGION")}})
except Exception as e:
      print(e)

