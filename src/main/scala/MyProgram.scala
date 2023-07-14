import scala.io.Source
import com.opencsv.CSVReader
import scala.collection.mutable
import scala.jdk.CollectionConverters._

object MyProgram{


  //case class for each country gdp input
  case class CountryGDP(country: String, year: Int, gdpPerCapital: Double)

  //parametric function to get country from CountryGDP object
  def getCountry(countryGDP: CountryGDP): String = {
    countryGDP.country
  }


  //function for reading csv file into a mutable buffer
  private def readFile(filename: String): mutable.Buffer[CountryGDP] = {

    //initialize reader
    val reader = new CSVReader(Source.fromResource(filename).reader())

    //read each line of data and add into mutable buffer
    val data = reader.readAll().asScala.drop(841).flatMap { line =>
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


  /*
  Task 1:
  Finding the country with the highest GDP per capital
  */
  
  // Function to filter the data and keep only the latest year for each country
  private def filterLatestYearData(data: mutable.Buffer[CountryGDP]): mutable.Buffer[CountryGDP] = {

    //get latest year of each country
    val latestYearData = data.groupBy((countryGDP: CountryGDP) => getCountry(countryGDP))
      .view.mapValues((groupedData: mutable.Buffer[CountryGDP]) => groupedData.maxBy(_.year))

    //return CountryGDP objects with each country one input of their latest year
    latestYearData.values.toBuffer
  }

  // Function to find the maximum element in a collection based on a given property
  private def findMaxBy[A, B](data: mutable.Buffer[A])(property: A => B)(implicit ord: Ordering[B]): A = {
    
    //find CountryGDP object with highest GDP per capital
    data.maxBy(property)
  }


  /*
  Task 2:
  Finding the average GDP per capital(US dollars) for Malaysia in the provided data
  */


  /*
  Task 3:
  Finding the five countries with the lowest average GDP per capital(US dollars) in the provided data
  */


  //main function to run main code (implementation)
  def main(args: Array[String]): Unit = {

    // Read the dataset from a CSV file (can be a txt file too)
    val filename = "gdpdata.csv"
    val data = readFile(filename)

    //Task 1
    // Filter the data to keep only the latest year for each country
    val filteredData = filterLatestYearData(data)

    // Find the country with the highest GDP per capital
    val countryWithHighestGDPPerCapital = findMaxBy(filteredData)(_.gdpPerCapital)

    println("\n========================================")
    println("COUNTRY WITH THE HIGHEST GDP PER CAPITAL")
    println("========================================")
    println(s"Country: ${countryWithHighestGDPPerCapital.country}\nYear: ${countryWithHighestGDPPerCapital.year}\nGDP per capital: ${countryWithHighestGDPPerCapital.gdpPerCapital.formatted("%.2f")} US Dollars")
  }
}