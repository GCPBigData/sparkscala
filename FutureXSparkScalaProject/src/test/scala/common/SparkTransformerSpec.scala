package common

import org.apache.spark.sql.{DataFrame, SparkSession}

class SparkTransformerSpec extends FutureXBase {
  val spark = SparkSession.builder.appName("HelloSpark")
    .config("spark.master", "local").enableHiveSupport()
    .getOrCreate()

  behavior of "Spark Transformer"

  it should "replace null value with unknown" in {
    val df : DataFrame = spark.read
      .option("header", "true")
      .option("inferSchema","true")
      .csv("mock_course_data.csv")
    df.show()
    val transformedDF = SparkTraformer.replaceNullValues(df)
    transformedDF.show()
    val authors = transformedDF
      .filter(transformedDF("course_id")
        .equalTo("2"))
      .select("author_name")
      .collectAsList()

    val author = authors.get(0)(0)
    println("transformed author "+author)
    assert("Unknown" == author)
  }

}
