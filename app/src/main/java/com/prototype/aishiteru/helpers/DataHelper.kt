// Specify your package here
package com.prototype.aishiteru.helpers

import com.prototype.aishiteru.R
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.News
import com.prototype.aishiteru.classes.Quiz
import java.util.ArrayList

// RSS Imports
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

// Jsoup Import
import org.jsoup.Jsoup


object DataHelper {
    private val api = RetrofitClient.instance.create(NewsAPI::class.java)

    // Function to load the latest X news data from multiple NHK RSS feeds
    suspend fun loadNHKNewsData(limit: Int = 5): ArrayList<News> = withContext(Dispatchers.IO) {
        val data = ArrayList<News>()
        val feeds = mapOf(
            "https://www3.nhk.or.jp/rss/news/cat0.xml" to "BREAKING",
            "https://www3.nhk.or.jp/rss/news/cat1.xml" to "SOCIETY",
            "https://www3.nhk.or.jp/rss/news/cat2.xml" to "DAILY LIFE",
            "https://www3.nhk.or.jp/rss/news/cat3.xml" to "SCIENCE & CULTURE",
            "https://www3.nhk.or.jp/rss/news/cat6.xml" to "INTERNATIONAL"
        )

        try {
            for ((feedUrl, difficulty) in feeds) {
                val url = URL(feedUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)

                var eventType = parser.eventType
                var currentItem: News? = null
                var currentTag: String? = null
                var itemCount = 0

                while (eventType != XmlPullParser.END_DOCUMENT && itemCount < limit) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            currentTag = parser.name
                            if (currentTag == "item") {
                                currentItem = News(
                                    difficulty = difficulty, // Set difficulty based on feed
                                    header = "",
                                    date = CustomDate(1970, 1, 1),
                                    imageUrl = "",
                                    content = ""
                                )
                            }
                        }
                        XmlPullParser.TEXT -> {
                            currentItem?.let {
                                when (currentTag) {
                                    "title" -> it.header = parser.text
                                    "link" -> {
                                        it.content = parser.text
                                        it.imageUrl =
                                            fetchImageUrl(parser.text).toString() // Fetch image URL from article link
                                    }
                                    "pubDate" -> {
                                        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                                        val parsedDate = dateFormat.parse(parser.text)
                                        parsedDate?.let { date ->
                                            val calendar = java.util.Calendar.getInstance()
                                            calendar.time = date
                                            it.date = CustomDate(
                                                calendar.get(java.util.Calendar.YEAR),
                                                calendar.get(java.util.Calendar.MONTH) + 1,
                                                calendar.get(java.util.Calendar.DAY_OF_MONTH)
                                            )
                                        }
                                    }
                                    //"description" -> it.content = parser.text
                                    else -> {}
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (parser.name == "item") {
                                currentItem?.let { data.add(it) }
                                itemCount++
                            }
                            currentTag = null
                        }
                    }
                    eventType = parser.next()
                }

                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Sort the data by date (newest to oldest)
        data.sortWith(compareByDescending { it.date.toCalendar().time })

        data
    }

    // Function to fetch the image URL from the HTML content of a news article
    private fun fetchImageUrl(articleUrl: String): String? {
        return try {
            val document = Jsoup.connect(articleUrl).get()

            // Try to get Open Graph image first
            val ogImageElement = document.select("meta[property=og:image]").firstOrNull()
            if (ogImageElement != null) {
                val ogImageUrl = ogImageElement.attr("content")
                if (ogImageUrl.isNotEmpty()) {
                    android.util.Log.d("DataHelper", "Fetched OG image URL: $ogImageUrl")
                    return ogImageUrl
                }
            }

            // Fallback to searching for an image with a specific pattern in the src attribute
            document.select("img").forEach { img ->
                android.util.Log.d("DataHelper", "Found image: " + img.absUrl("src"))
            }

            val imageElement = document.select("img").firstOrNull()
            android.util.Log.d("DataHelper", "Image element selected: " + (imageElement?.absUrl("src") ?: "No image found"))
            imageElement?.absUrl("src").also { imageUrl ->
                android.util.Log.d("DataHelper", "Fetched image URL: " + (imageUrl ?: "No image found"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Extension function to convert CustomDate to Calendar for sorting purposes
    fun CustomDate.toCalendar(): java.util.Calendar {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(this.getYear(), this.getMonth() - 1, this.getDay())
        return calendar
    }


    fun loadQuizData(): ArrayList<Quiz> {
        val data = ArrayList<Quiz>()
        data.add(
            Quiz(
                0, "Easy Kanji Series 1", 2, R.drawable.test_book1
            )
        )
        data.add(
            Quiz(
                0, "Easy Kanji Series 2", 5, R.drawable.test_book2
            )
        )
        data.add(
            Quiz(
                0, "Ordering Restaurant Food", 7, R.drawable.test_udon
            )
        )
        data.add(
            Quiz(
                0, "At the Airport", 9, R.drawable.test_plane
            )
        )

        return data
    }

    fun loadAchievementData(): ArrayList<Achievement> {
        val data = ArrayList<Achievement>()
        data.add(
            Achievement(
                0, "AIShiteru", "Unlock all Characters",
                android.R.drawable.ic_menu_call,
                2, 7, false
            )
        )
        data.add(
            Achievement(
                1, "Harem God", "Max out Everyone's Relationship Level",
                android.R.drawable.ic_menu_compass,
                6, 35, false
            )
        )
        data.add(
            Achievement(
                2, "Nihongo Jouzu", "Complete all Quizzes",
                android.R.drawable.ic_menu_sort_alphabetically,
                10, 10, false
            )
        )
        data.add(
            Achievement(
                3, "Certified Japanese", "Read all News",
                android.R.drawable.ic_menu_gallery,
                4, 15, false
            )
        )
        data.add(
            Achievement(
                4, "100% Complete", "Unlock all Achievements",
                android.R.drawable.ic_menu_my_calendar,
                5, 20, false
            )
        )

        return data
    }
}


// Uses News API instead
/*
suspend fun loadNewsData(limit: Int = 10): ArrayList<News> {
    val data = ArrayList<News>()
    val country = "jp"
    val apiKey = "292c5591921442898c97001b090b7477"

    return suspendCancellableCoroutine { continuation ->
        val call = api.getTopHeadlines(country, apiKey)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        val articles = newsResponse.articles.take(limit) // Limit articles
                        for (article in articles) {
                            val datePublished = article.publishedAt.substring(0, 10).split("-")
                            data.add(
                                News(
                                    difficulty = "MEDIUM", // Default value
                                    header = article.title,
                                    date = CustomDate(datePublished[0].toInt(), datePublished[1].toInt(), datePublished[2].toInt()),
                                    imageUrl = article.urlToImage ?: "", // Image URL or empty string
                                    content = article.description ?: "No content available" // Handle null safely
                                )
                            )
                        }
                        continuation.resume(data)
                    } else {
                        continuation.resumeWithException(Exception("Empty response body"))
                    }
                } else {
                    continuation.resumeWithException(Exception("Failed to fetch news: ${response.errorBody()?.string()}"))
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }*/

/* Hardcoded test
val data = ArrayList<News>()
data.add(
    News(
        "MEDIUM", "この三人の美人はだれ？",
        CustomDate(2024, 10, 19),
        R.drawable.test_idols, "アイマスー。"

    )
)
data.add(
    News(
        "EASY", "フモって増えてくる！",
        CustomDate(2024, 10, 17),
        R.drawable.test_fumo, "フモフモ。"

    )
)
data.add(
    News(
        "HARD", "このゲーム見なきゃ。",
        CustomDate(2024, 9, 5),
        R.drawable.test_chess, "これはテストです。"

    )
)
data.add(
    News(
        "EASY", "なんだこりゃ。",
        CustomDate(2024, 9, 2),
        R.drawable.test_bean, "へんなやつ。"

    )
)

}*/