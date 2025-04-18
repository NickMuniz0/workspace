from pyspark.sql import SparkSession
from pyspark.sql.functions import avg,count,when,lit
from pyspark.sql import functions as  F

from pyspark.sql.window import Window
from pyspark.sql.functions import row_number



spark = SparkSession.builder.appName("example").getOrCreate()

data = [("SD","LEGADO",'2024-10-01',1,0),
        ("FD","LEGADO",'2024-10-02',1,1),
        ("DD","NAO LEGADO",'2024-10-02',1,2),
        ("FD","TESTE1",'2024-10-03',"22",3),
        ("FD","TESTE2",'2024-10-03',"23",3)
        ]
colunas = ["DEPARTAMENTO","TIPO",'DATA',"qtd",'ID']
df = spark.createDataFrame(data,colunas)

# grouped_df = df.groupby(colunas).agg( #count(when( (F.col("TIPO")=="LEGADO") & (F.col("DEPARTAMENTO")=="FD"), F.lit("DEPARTAMENTO")  )).alias("Quantidades")  
#                 count(when( (F.col("TIPO")=="LEGADO") & (F.col("DEPARTAMENTO")=="FD"), F.lit("DEPARTAMENTO")  )).alias("Quantidades") 
# )
# ano =2024
# # filtred_df = grouped_df.filter("avg_salary > 20")
# grouped_df =grouped_df.withColumn("num_ano", lit(ano) )
# grouped_df.show()

df = df.withColumn("qtd", df["qtd"].cast("string"))
windowSpec = Window.partitionBy("ID").orderBy(F.col("qtd").desc())
df_with_row_number = df.withColumn("row_number", row_number().over(windowSpec))
max_value_df = df_with_row_number.filter(F.col("row_number") == 1).drop("row_number")


max_value_df.write.format('json').save("./teste.json")
