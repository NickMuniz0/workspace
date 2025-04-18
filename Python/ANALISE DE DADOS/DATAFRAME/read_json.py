from pyspark.sql import SparkSession
from pyspark.sql.functions import avg,count,when,lit,from_json, col,concat,struct,concat_ws
from pyspark.sql import functions as  F
from pyspark.sql.types import *

import boto3
from boto3.dynamodb.types import TypeDeserializer
# from pyspark.sql.functions import 


spark = SparkSession.builder.appName("example").getOrCreate()

# schemaJson = StructType([
#                     StructField("Item",
#                                     StructType([
#                                                 StructField("num_idef_cpvt", 
                                                
#                                                             StructType([StructField("S",
#                                                                                         StringType()
#                                                                                     ,True)
#                                                                         ])
#                                                             ,True)
#                                     ])
#                     ,True)
#         ])


# schemaJson = StructType([
#                     StructField("Item",
#                                     StructType([
#                                                 StructField("num_idef_cpvt", 
                                                
#                                                             StructType([StructField("S",
#                                                                                         StringType()
#                                                                                     ,True)
#                                                                         ])
#                                                             ,True)
#                                     ])
#                     ,True)
#         ])



# schemaJson = StructType([
#                                                 StructField("num_idef_cpvt", 
                                                
#                                                             StructType([StructField("S",
#                                                                                         StringType()
#                                                                                     ,True)
#                                                                         ])
#                                                             ,True)
#                                     ])


# resultado = spark.read.option("multiLine","true").json("./teste.json")#.printSchema()
# r = resultado.withColumn("num_idef_cpvt", resultado['Item']['num_idef_cpvt']["S"].cast("string"))
# r.show()
#########################################################################################################
#########################################################################################################
#########################################################################################################
#########################################################################################################
#########################################################################################################



Item=StructField("Item",
                                    StructType([
                                                StructField("num_idef_cpvt",  
                                                
                                                            StructType([StructField("S",
                                                                                        StringType()
                                                                                    ,True)
                                                                        ])
                                                            ,True)
                                    ])
                    ,True)


Item2=StructField("Item2",
                                    StructType([
                                                StructField("num_idef_cpvt",  
                                                
                                                            StructType([StructField("S",
                                                                                        StringType()
                                                                                    ,True)
                                                                        ])
                                                            ,True)
                                    ])
                    ,True)


schemaJson = StructType([
                    Item
        ])

df = spark.read.option("multiLine","false").schema(schemaJson).json("./teste.json")


from pyspark.sql.functions import coalesce

# Criar uma nova coluna substituindo valores nulos de 'col1' com valores de 'col2'
# df_filled = df.withColumn("col1_filled", coalesce(df["Item"], df["Item2"]))
df =df.na.drop()
df.show(truncate=True)

# if len(df.columns)>1:

#     # Cast columns to string and concatenate
#     df = df.withColumn("item", col("item").cast("string"))
#     df = df.withColumn("item2", col("item2").cast("string"))


#     # Concatenate columns with a delimiter (e.g., '-')
#     df = df.withColumn("Item3", concat_ws("", col("item"), col("item2")))
#     df = df.drop("item")
#     df = df.drop("item2")

# df = df.withColumn("item3", col("item3").cast(StructType()))

#########################################################################################################
#########################################################################################################
#########################################################################################################
#########################################################################################################
#########################################################################################################


# # Sample DataFrame
# data = [("1", "A"), ("2", "B"), ("3", "C")]
# columns = ["item", "item2"]
# df = spark.createDataFrame(data, columns)

# # Cast columns to string
# df = df.withColumn("item", col("item").cast("string"))
# df = df.withColumn("item2", col("item2").cast("string"))

# # Concatenate columns with a delimiter (e.g., '-')
# df = df.withColumn("item3", concat_ws("-", col("item"), col("item2")))

# # Drop original columns if needed
# df = df.drop("item").drop("item2")

# # Show the result
# df.show()

