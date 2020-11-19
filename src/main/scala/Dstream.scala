import org.apache.spark.sql.SparkSession

object Dstream extends App {

    val spark = SparkSession.builder().appName("df").master("local").getOrCreate()
    import spark.sqlContext.implicits._
    val trips = spark.read.format("csv").option("header", true)
      .load("hdfs://quickstart.cloudera:8020/user/fall2019/snehith/project4/trips/trips.txt")
    trips.schema
    trips.show(false)
    val fq = spark.read.format("csv").option("header", true)
      .load("hdfs://quickstart.cloudera:8020/user/fall2019/snehith/project4/frequencies/frequencies.txt")
    fq.show(10, false)
    val cd = spark.read.format("csv").option("header", true)
      .load("hdfs://quickstart.cloudera:8020/user/fall2019/snehith/project4/calendar_dates/calendar_dates.txt")
    cd.show(10, false)
    cd.printSchema()

}