// Specify your package here
package com.prototype.aishiteru.helpers


import com.prototype.aishiteru.R
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.News
import com.prototype.aishiteru.classes.Quiz
import java.util.ArrayList

/*
 * To load the data, simply declare an ArrayList<Tweet> variable and assign, as such:
 *       lateinit var data: ArrayList<Tweet>
 *       .
 *       .
 *       .
 *       data = TestFile.loadTweetData()
 * However, you don't have to follow this and can implement this with what you feel 
 * comfortable with.
 * */
object DataHelper {
    fun loadNewsData(): ArrayList<News> {
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

        return data
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