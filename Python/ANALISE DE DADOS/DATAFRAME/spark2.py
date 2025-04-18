from pyspark.sql import SparkSession
from pyspark.sql.functions import avg,count,when,lit
from pyspark.sql import functions as  F
from pyspark.sql.window import Window
from pyspark.sql.functions import row_number
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, count, when

# Inicializando a SparkSession
spark = SparkSession.builder.appName("Repetidos").getOrCreate()

# Exemplo de DataFrame
data = [
        (1,"EMITIDO","LEGADO","PIX", "A"),
        (1,"EMITIDO","LEGADO","PIX", "A"),
        (1,"EMITIDO","LEGADO","PIX", "A"),
        (1,"EMITIDO","LEGADO","PIX", "A"),
####################################################################################################

        (5,"EMITIDO","LEGADO","PIX", "A"),


####################################################################################################
        (2,"EMITIDO","LEGADO","PIX", "B"),
        (2,"EMITIDO","LEGADO","PIX", "B"),
        (2,"EMITIDO","LEGADO","PIX", "B"),

####################################################################################################
        (3,""       ,"LEGADO","PIX", "C"),
####################################################################################################
        (4,"EMITIDO",""      ,"PIX", "D")
####################################################################################################

]

columns = ["id","SITUACAO","ORIGEM","COD","value"]

df = spark.createDataFrame(data, columns)

####################################################################################################
# Contando a repetição dos IDs
df_with_count = df.groupBy("id").agg(count("id").alias("count"))

# Juntando o DataFrame original com o DataFrame de contagem
df_final = df.join(df_with_count, on="id", how="left")

# Criando a coluna que indica se a linha é repetida
df_final = df_final.withColumn("is_repeated", when(col("count") > 1, 1).otherwise(0)     )



############################### COMPROVANTES REALMENTE REPETIDOS ###################################

coll = ["SITUACAO", "ORIGEM","COD","value"] 

df_final3 = df_final.groupby(coll).agg( 
                F.count(when(    (F.col("SITUACAO") == "EMITIDO" ) &  (( F.col("is_repeated") == 1) )     , lit(F.col("SITUACAO"))  )).alias("QTD_REPETIDOS")
)
df_final3.show()


############################### COMPROVANTES REALMENTE EMITIDOS ####################################

coll = ["SITUACAO", "ORIGEM","COD","value"] 

df_final5 = df_final.distinct().groupby(coll).agg( 
                F.count(when(    (F.col("SITUACAO") == "EMITIDO" )     , lit(F.col("SITUACAO"))  )).alias("QTD_REAL_EMITIDOS")
)
df_final5.show()

####################################################################################################
joined_df =df_final3.join(df_final5,  on=coll, how="inner"    )

joined_df.show()




############################### COMPROVANTES TOTAL DA BASE      ######################
# coll = ["SITUACAO", "ORIGEM","COD", "value"] 

# df_final2 = df.groupby(coll).agg( 
#                 F.count(when(F.col("SITUACAO") == "EMITIDO", lit(F.col("SITUACAO"))  )).alias("QTD")
# )
# df_final2.show()

# #######################################################################
# coll = ["SITUACAO", "ORIGEM","COD","is_repeated","value"] 

# df_final4 = df_final.groupby(coll).agg( 
#                 F.count(when(    (F.col("SITUACAO") == "EMITIDO" )     , lit(F.col("SITUACAO"))  )).alias("QTD")
# )
# df_final4.show()

