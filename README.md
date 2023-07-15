# GDP Analysis and Polymorphism in Scala Collections

This Scala program analyzes GDP data using Scala's collection API to answer specific questions about countries' GDP per capita. The program reads a dataset downloaded from UNdata and calculates the highest GDP per capita, average GDP per capita for Malaysia, and the country with the lowest average GDP per capita.

## Dataset

The dataset used in this program is downloaded from UNdata and should be provided in a CSV format. Each row in the dataset represents a country's GDP data for a specific year, including the country name, the year, and the GDP per capita in US dollars.

Ensure that you have downloaded the dataset file and update the `path/to/dataset.csv` placeholder in the Scala program with the actual path to the dataset file.

## Running the Program

To run the program, you need to have Scala installed on your machine. Here are the steps:

1. Clone this repository or download the source code files.
2. Open a terminal and navigate to the project directory.
3. Update the path to the dataset file in the Scala program as mentioned above.
4. Compile the Scala program using the `scalac` command:
   `scalac MyProgram.scala`
5. Run the compiled program using the `scala` command:
   `scala MyProgram`
6. The program will display the answers to the questions in the terminal.

## Questions Answered

1. Which country has the highest GDP per capita?
- The program will display the country with the highest GDP per capita based on the provided dataset.

2. What is the average GDP per capita for Malaysia in the provided data?
- The program will calculate and display the average GDP per capita for Malaysia.

3. Which of the five countries is the lowest average GDP per capita in the provided data?
- The program will determine the country with the lowest average GDP per capita among five specified countries.
