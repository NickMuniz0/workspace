import pandas as pd
import os
from pathlib import Path
from fastparquet import write
import re
import s3fs  # PARA AWS
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
    s3=s3fs.S3FileSystem()## ALTERAR PARA AWS
    myopen=s3.open## ALTERAR PARA AWS
    if  Path(path).exists():## ALTERAR PARA AWS
        file_path = get_file_path() ## ALTERAR PARA AWS
        if os.path.getsize(file_path)<2300: ## ALTERAR PARA AWS
                print("appendou")
                df1 = pd.read_parquet(file_path,engine="fastparquet") 
                df2 = pd.concat([df,df1])
                write(file_path, df2, partition_on = partitions) 
                #write(file_path, df2, partition_on = partitions,open_with=myopen) ## ALTERAR PARA AWS

        else:
                print("criou de novo")
                namefile= re.search(r'([parquet]+[-]+[0-9]{0,})',file_path)
                name_file = namefile.group(0)
                index_file = name_file[namefile.group(0).index("-")+1:]
                index_file_novo = int(index_file)+1
                write(f'{path}/parquet-{index_file_novo}.parquet', df, partition_on = partitions)
                #write(f'{path}/parquet-{index_file_novo}.parquet', df, partition_on = partitions,open_with=myopen) ## ALTERAR PARA AWS

    else:
        os.makedirs(path)## ALTERAR PARA AWS
        write(f'{path}/parquet-0.parquet', df, partition_on = partitions)
        #write(f'{path}/parquet-0.parquet', df, partition_on = partitions,open_with=myopen) ## ALTERAR PARA AWS

except Exception as e:
      print(e)

print(pd.read_parquet(get_file_path()))
# print(pd.read_parquet(f'{path}/part.0.parquet'))
# print(pd.read_parquet(f'{path}/part.1.parquet'))
# print(pd.read_parquet(f'{path}/part.2.parquet'))

