package com.prototype.aishiteru

import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.MessageItem

class DataGenerator {
    companion object {
        private val cast1 : CastItem = CastItem ("Limon C. Saida", 0, R.drawable.avatar_lim, "0x1")
        private val cast2 : CastItem = CastItem ("Diiaphy Nakao", 0, R.drawable.avatar_diia, "0x2")
        private val cast3 : CastItem = CastItem ("Doko Niiruno", 0, R.drawable.avatar_doko, "0x3")
        private val cast4 : CastItem = CastItem ("Wolf Chii", 0, R.drawable.avatar_wolp, "0x4")
        private val cast5 : CastItem = CastItem ("{user}", -1, R.drawable.ai, "-1")

        private val castList = listOf(cast1, cast2, cast3, cast4)

        fun generateMessages(): ArrayList<MessageItem> {
            val messages = ArrayList<MessageItem>()
            val currentDate = CustomDate(2024, 11, 18, 6, 0)

            for (cast in castList) {
                // Add a message from the cast member to the user
                messages.add(MessageItem("Hello from ${cast.name}!", currentDate, cast, cast5))
                // Add a reply from the user to the cast member
                messages.add(MessageItem("Reply from {user} to ${cast.name}", currentDate, cast5, cast))
                // Add another message from the user to the cast member
                messages.add(MessageItem("How are you, ${cast.name}?", currentDate, cast5, cast))
                // Add a reply from the cast member to the user
                messages.add(MessageItem("I'm good, {user}. How about you?", currentDate, cast, cast5))
            }

            return messages
        }
    }
}