import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Stream extends App {
 val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Stream")
 val sc: SparkContext = new SparkContext(sparkConf)
 val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))


 val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)



 lines.print()
ssc.start()
 ssc.awaitTermination()


}
