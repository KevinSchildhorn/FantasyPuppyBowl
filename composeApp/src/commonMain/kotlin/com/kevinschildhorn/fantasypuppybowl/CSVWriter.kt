package com.kevinschildhorn.fantasypuppybowl

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

class CSVWriter {
    fun writeCSV(fileName: String, dogs: List<DogListItem>) {
        csvWriter().open(fileName) {
            writeRow("Name", "Team", "Touchdowns", "Field Goals", "Penalties", "Total")
            dogs.forEach {
                writeRow(it.name, it.team, 0, 0, 0, 0)
            }
            writeRow("Total")
            writeRow("RED", "", "", "", "", 0)
            writeRow("GREEN", "", "", "", "", 0)
            writeRow("YELLOW", "", "", "", "", 0)
            writeRow("BLUE", "", "", "", "", 0)
        }
    }
}