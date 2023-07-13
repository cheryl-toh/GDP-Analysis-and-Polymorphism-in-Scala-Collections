import scala.io.Source
import com.opencsv.CSVReader

import scala.collection.mutable
import scala.jdk.CollectionConverters._

//case class for each country gdp input
case class CountryGDP(country: String, year: Int, gdpPerCapital: Double)

//main object
object Main{

  //function for reading csv file into a mutable buffer
  def readFile(filename: String): mutable.Buffer[CountryGDP] = {

    //initialize reader
    val reader = new CSVReader(Source.fromResource(filename).reader())

    //read each line of data and add into mutable buffer
    val data = reader.readAll().asScala.drop(2).flatMap { line =>
      val country = line(1).replaceAll("\"", "")
      val year = line(2).toInt
      val gdpPerCapital = line(4).replaceAll("\"", "").replaceAll(",", "").toDouble

      if (country.nonEmpty)
        Some(CountryGDP(country, year, gdpPerCapital))
      else
        None
    }
    //close reader
    reader.close()

    //return mutable buffer
    data
  }

  //main function to run main code (implementation)
  def main(args: Array[String]): Unit = {

      // Read the dataset from a CSV file (can be a txt file too)
      val filename = "gdpdata.csv"
      val data = readFile(filename)

    //print data inputs
      data.foreach(println)


  }
}





