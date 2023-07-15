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
      val gdpValue = line(4).replaceAll("\"", "").replaceAll(",", "")
      val gdpPerCapital = line(3) match {
        case "GDP per capita (US dollars)" => gdpValue.toDouble / 1_000_000
        case "GDP in constant 2010 prices (millions of US dollars)" => gdpValue.replaceAll("\"", "").toDouble
        case "GDP real rates of growth (percent)" => 0.0 // or any default value for growth rates
        case _ => 0.0 // default value for any other types of input
      }

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


  //Function to calculate average gdp per capital for a country of type A
  private def calculateAverageGDPPerCapital[A](data: mutable.Buffer[A])(gdpPerCapitalSelector: A => Double): Double = {
    val totalGDPOfCountryA = data.map(gdpPerCapitalSelector).sum
    val averageGDP = totalGDPOfCountryA / data.length

    averageGDP
  }


  /*
  Task 1:
  Finding the country with the highest GDP per capital
  */

  // Function to filter the data and keep only the latest year for each country
  private def filterLatestYearData(data: mutable.Buffer[CountryGDP]): mutable.Buffer[CountryGDP] = {

    //select countries' highest gdp per capital value for latest year input
    val latestYearData = data.groupBy(countryGDP => (countryGDP.country, countryGDP.year))
      .view.mapValues(groupedData => groupedData.maxBy(_.gdpPerCapital))

    //return Buffer list
    latestYearData.values.toBuffer
  }

  // Function to find the maximum element in a collection based on a given property
  private def findMaxBy[A, B](data: mutable.Buffer[A])(property: A => B)(implicit ord: Ordering[B]): A = {
    data.maxBy(property)
  }




  /*
  Task 2:
  Finding the average GDP per capital(US dollars) for Malaysia in the provided data
  */

  //Function to find the average value of Malaysia's GDP
  private def findGDPMalaysia(data: mutable.Buffer[CountryGDP]): Double = {

    //returns a buffer containing countries that are Malaysia
    val malaysiaRecords = data.filter((countryGDP: CountryGDP) => getCountry(countryGDP) == "Malaysia")

    //calculates the average GDP of selected country
    val AverageGDPMalaysia = calculateAverageGDPPerCapital(malaysiaRecords)(_.gdpPerCapital)

    //return average GDP value
    AverageGDPMalaysia
  }



  /*
  Task 3:
  Finding the five countries with the lowest average GDP per capital(US dollars) in the provided data
  */

  //Function to find lowest average GDP countries
  private def findLowestAverageGDPCountries(data: mutable.Buffer[CountryGDP], n: Int): List[(String, Double)] = {

    //group countries by name and calculate its average gdp per capital
    val averageGDPCountries = data.groupBy((countryGDP: CountryGDP) => getCountry(countryGDP))
      .view.mapValues((groupedData: mutable.Buffer[CountryGDP]) => calculateAverageGDPPerCapital(groupedData)(_.gdpPerCapital))
    //append n number of countries with lowest average gdp into a list
    val lowestAverageGDPCountries = averageGDPCountries.toList.sortBy(_._2).take(n)

    //return list
    lowestAverageGDPCountries
  }



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
    println(s"Country: ${countryWithHighestGDPPerCapital.country}\nYear: ${countryWithHighestGDPPerCapital.year}\nGDP per capital: ${"%.2f".format(countryWithHighestGDPPerCapital.gdpPerCapital)} Millions of US Dollars")

    //Task 2

    // Find the average of Malaysia's GDP
    val malaysiaAverageGDP = findGDPMalaysia(data)

    println("\n========================================")
    println("AVERAGE GDP PER CAPITAL OF MALAYSIA")
    println("========================================")

    println(s"Country: Malaysia \nAverage GDP per capital: ${"%.2f".format(malaysiaAverageGDP)} Millions of US Dollars")

    //Task 3
    val lowestAverageGDP = findLowestAverageGDPCountries(data, 5)

    println("\n======================================================")
    println("FIVE COUNTRIES WITH THE LOWEST AVERAGE GDP PER CAPITAL")
    println("======================================================")
    lowestAverageGDP.foreach { case (country, avgGDP) =>
      println(s"$country (${"%.2f".format(avgGDP)} Millions of US Dollars)")
    }
    println()
  }
}