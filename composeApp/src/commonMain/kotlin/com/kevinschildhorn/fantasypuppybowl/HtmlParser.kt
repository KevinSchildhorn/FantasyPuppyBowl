package com.kevinschildhorn.fantasypuppybowl

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

class HTMLParser {
    suspend fun getDogInfo(urlString: String):List<ListItem> {
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        try {
            val html = urlConnection.inputStream.bufferedReader().readText()
            return parseHtml(html)
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun parseHtml(html: String):List<ListItem> {
        val ksoupHtmlParser = KsoupHtmlParser()
        ksoupHtmlParser.write(html)
        val doc = Jsoup.parse(html)
        val script = doc.head().allElements.find {
            it.`is`("script") &&
                    it.html().contains("mainEntityOfPage")
        }
        print(script)
        val html = script!!.html()

        val dogs = Json.decodeFromString<List<ScriptContents>>(html)
        val dogList = dogs.filterNot { it.mainEntity == null }.first()
        val items:List<ListItem>? = dogList.mainEntity?.itemListElement?.map {
            it.item
        }
        print(items)
        ksoupHtmlParser.end()
        return items!!
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonIgnoreUnknownKeys
    data class ScriptContents(
        val mainEntity: MainEntity? = null,
    )


    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonIgnoreUnknownKeys
    data class MainEntity(
        val itemListElement: List<ListElement>,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonIgnoreUnknownKeys
    data class ListElement(
        val item: ListItem,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonIgnoreUnknownKeys
    data class ListItem(
        val name: String,
        val description: String,
        val image: String,
    )
}