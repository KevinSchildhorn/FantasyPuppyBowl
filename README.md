# Fantasy Puppy Bowl

This is a simple app made in Kotlin Multiplatform that facilitates Puppy Bowl Fantasy Drafting. 

> Note: It should be obvious but this is a project I made for fun. It is not to be taken seriously or used for any betting.

It works by:
* Pulling the player data from the website lineup
* Parsing the HTML script from an html json into data classes
* Displaying the players in a grid in compose multiplatform

The app features:
* The option to search for a player
* Select players for four teams: Red, Green, Blue and Yellow
* Toggle whether to show or hide selected players
* Exporting your selection into a CSV to track scoring

This is all done in KMP using the Libraries:
* [Coil](https://github.com/coil-kt/coil)
* [KSoup](https://github.com/MohamedRejeb/Ksoup)
* [Kotlin-CSV](https://github.com/jsoizo/kotlin-csv)
* [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization)
