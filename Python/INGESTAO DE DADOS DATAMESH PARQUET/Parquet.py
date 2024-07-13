import pandas as pd
import os
from pathlib import Path
from fastparquet import write

class Teste:
    nome:str
    id:str

objeto = Teste()
objeto.ano =2024
objeto.mes=6
objeto.dia=7
objeto.hor=4
objeto.nome="NICK"
objeto.id="15"

teste =objeto.__dict__
lista = list()
lista.append(teste)
partitions=['ano','mes','dia','hor']
path_0 = "./temp_emissao/"
path =f"{path_0}ano={objeto.ano}/mes={objeto.mes}/dia={objeto.dia}/hor={objeto.hor}"
df =pd.DataFrame(lista)

def get_file_path():            
            try:                    
                diretorio = Path(path)
                files = diretorio.glob('*.parquet')
                arquivos = sorted(files, key=lambda f: f.stat().st_mtime,reverse=True)
                file_name=f"{arquivos[0].name}"
                file_path = f"{path}/{file_name}" 
                return file_path
            except TypeError :
                  return path_0
            except IndexError:
                  return path_0

try:
    if  Path(path).exists():
        file_path = get_file_path()
        if os.path.getsize(file_path)<2300: 
                print("appendou")
                df1 = pd.read_parquet(file_path,engine="fastparquet") 
                df2 = pd.concat([df,df1])
                write(file_path, df2, partition_on = partitions) 
        else:
                print("criou de novo")
                df.to_parquet(path_0,partition_cols= partitions,engine="fastparquet",file_scheme="hive",append=True) 
                file_path = get_file_path()
                print(file_path)
                write(file_path, df, partition_on = partitions)
    else:
        df.to_parquet(path_0,partition_cols= partitions,engine="fastparquet",file_scheme="hive") 
        file_path = get_file_path()
        print("criou")
        write(file_path, df, partition_on = partitions)

except Exception as e:
      print(e)

print(pd.read_parquet(get_file_path()))
# print(pd.read_parquet(f'{path}/part.0.parquet'))
# print(pd.read_parquet(f'{path}/part.1.parquet'))
# print(pd.read_parquet(f'{path}/part.2.parquet'))

