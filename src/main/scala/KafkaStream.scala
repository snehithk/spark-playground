import DataFrame.spark
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

object KafkaStream extends App {
  val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Stream")
  val sc: SparkContext = new SparkContext(sparkConf)
  val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
  val kafkaParams = Map[String, String] (
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "172.16.129.58:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
    ConsumerConfig.GROUP_ID_CONFIG -> "test",
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
  )


  val topic ="test1"


  // Create the stream
  val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Array(topic), kafkaParams)
  )
  case class Stop(trip_id:Int,arrival_time:String,departure_time:String,stop_id:String,stop_sequence:String)

  val x: DStream[String] = stream.map(record => record.value())
  x.foreachRDD(microRdd => {
    // microRDD is the RDD created by DStream at each 5 seconds time interval from the input stream
    import spark.implicits._
    val Stopdf = microRdd
      .map(_.split(","))
      .map(x => Stop(x(0).toInt, x(1), x(2), x(3),x(4)))
      .toDF

    Stopdf.show()
  })

  ssc.start()
  ssc.awaitTermination()








}
