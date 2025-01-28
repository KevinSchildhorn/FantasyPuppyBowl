package com.kevinschildhorn.fantasypuppybowl

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.net.HttpURLConnection
import java.net.URL

class HTMLParser {
    suspend fun getDogInfo(urlString: String): List<ListItem> {
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        try {
            val html = urlConnection.inputStream.bufferedReader().readText()
            return parseHtml(html)
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun parseHtml(html: String): List<ListItem> {
        val ksoupHtmlParser = KsoupHtmlParser()
        ksoupHtmlParser.write(html)
        val doc = Jsoup.parse(html)
        val names = doc.body().allElements.filter { element ->
            element.hasClass("o-PhotoGalleryPromo__m-AssetData")
        }.map {
            val nameElement =
                it.allElements.find { element -> element.hasClass("o-PhotoGalleryPromo__a-HeadlineText") }
            val descriptionElement =
                it.allElements.find { element -> element.hasClass("o-PhotoGalleryPromo__a-Description") }

            val description = (descriptionElement?.childNodes()?.first()?.childNodes()
                ?.first() as? TextNode)?.text() // TODO
            (nameElement?.childNodes()?.first() as? TextNode)?.text() to description

        }
        val imagesMobile = doc.body().allElements.filter { element ->
            element.hasClass("m-MediaBlock__a-Image")
        }.map {
            "https:" + it.attr("src")
        }
        val imagesDesktop = doc.body().allElements.filter { element -> // TODO: For Desktop Sites
            element.hasClass("rsImg")
        }.map {
            val attr = it.attr("src").ifEmpty { null } ?: it.attr("href")
            "https:" + attr
        }
        val finalImages = if(imagesMobile.size > imagesDesktop.size) imagesMobile else imagesDesktop

        val result = names.mapIndexed { index, pair ->
            ListItem(
                name = pair.first ?: "",
                description = pair.second ?: "",
                image = finalImages[index] ?: "",
            )
        }
        return result
    }

    private fun parseHtml_old(html: String): List<ListItem> {
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
        val items: List<ListItem>? = dogList.mainEntity?.itemListElement?.map {
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