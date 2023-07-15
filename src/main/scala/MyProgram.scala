import scala.io.Source
import com.opencsv.CSVReader
import scala.collection.mutable
import scala.jdk.CollectionConverters._

object MyProgram{


  //case class for each country gdp input
  case class CountryGDP(country: String, year: Int, gdpPerCapita: Double)

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
      val gdpValue = line(4).replaceAll("\"", "").replaceAll(",", "").toDouble
      val series = line(3).replaceAll("\"", "")

      if (country.nonEmpty && series.equalsIgnoreCase("GDP per capita (US dollars)"))
        Some(CountryGDP(country, year, gdpValue))
      else
        None
    }
    //close reader
    reader.close()
    //return mutable buffer
    data
  }


  //Function to calculate average gdp per capita for a country of type A
  private def calculateAverageGDPPerCapita[A](data: mutable.Buffer[A])(gdpPercapitaSelector: A => Double): Double = {

    //get sum of gdp per capita of a country
    val totalGDPOfCountryA = data.map(gdpPercapitaSelector).sum
    //get the average gdp
    val averageGDP = totalGDPOfCountryA / data.length

    //return average gdp
    averageGDP
  }


  /*
  Task 1:
  Finding the country with the highest GDP per capita
  */

  // Function to filter the data and keep only the latest year for each country
  private def filterLatestYearData(data: mutable.Buffer[CountryGDP]): mutable.Buffer[CountryGDP] = {

    //select countries' highest gdp per capita value for latest year input
    val latestYearData = data.groupBy(countryGDP => (countryGDP.country, countryGDP.year))
      .view.mapValues(groupedData => groupedData.maxBy(_.gdpPerCapita))

    //return Buffer list
    latestYearData.values.toBuffer
  }

  // Function to find the maximum element in a collection based on a given property
  private def findMaxBy[A, B](data: mutable.Buffer[A])(property: A => B)(implicit ord: Ordering[B]): A = {
    data.maxBy(property)
  }




  /*
  Task 2:
  Finding the average GDP per capita(US dollars) for Malaysia in the provided data
  */

  //Function to find the average value of Malaysia's GDP
  private def findGDPMalaysia(data: mutable.Buffer[CountryGDP]): Double = {

    //returns a buffer containing countries that are Malaysia
    val malaysiaRecords = data.filter((countryGDP: CountryGDP) => getCountry(countryGDP) == "Malaysia")

    //calculates the average GDP of selected country
    val AverageGDPMalaysia = calculateAverageGDPPerCapita(malaysiaRecords)(_.gdpPerCapita)

    //return average GDP value
    AverageGDPMalaysia
  }



  /*
  Task 3:
  Finding the five countries with the lowest average GDP per capita(US dollars) in the provided data
  */

  //Function to find lowest average GDP countries
  private def findLowestAverageGDPCountries(data: mutable.Buffer[CountryGDP], n: Int): List[(String, Double)] = {

    //group countries by name and calculate its average gdp per capita
    val averageGDPCountries = data.groupBy((countryGDP: CountryGDP) => getCountry(countryGDP))
      .view.mapValues((groupedData: mutable.Buffer[CountryGDP]) => calculateAverageGDPPerCapita(groupedData)(_.gdpPerCapita))
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

    // Find the country with the highest GDP per capita
    val countryWithHighestGDPPercapita = findMaxBy(filteredData)(_.gdpPerCapita)

    //print task 1 outcome
    println("\n========================================")
    println("COUNTRY WITH THE HIGHEST GDP PER CAPITA")
    println("========================================")
    println(s"Country: ${countryWithHighestGDPPercapita.country}\nYear: ${countryWithHighestGDPPercapita.year}\nGDP per capita: ${"%.2f".format(countryWithHighestGDPPercapita.gdpPerCapita)} US Dollars")

    //Task 2
    // Find the average of Malaysia's GDP
    val malaysiaAverageGDP = findGDPMalaysia(data)

    //print task 2 outcome
    println("\n========================================")
    println("AVERAGE GDP PER capita OF MALAYSIA")
    println("========================================")

    println(s"Country: Malaysia \nAverage GDP per capita: ${"%.2f".format(malaysiaAverageGDP)} US Dollars")

    //Task 3
    //Find 5 countries with lowest average GDP per capita
    val lowestAverageGDP = findLowestAverageGDPCountries(data, 5)

    //print task 3 outcome
    println("\n======================================================")
    println("FIVE COUNTRIES WITH THE LOWEST AVERAGE GDP PER CAPITA")
    println("======================================================")
    lowestAverageGDP.foreach { case (country, avgGDP) =>
      println(s"$country (${"%.2f".format(avgGDP)} US Dollars)")
    }
    println()
  }
}