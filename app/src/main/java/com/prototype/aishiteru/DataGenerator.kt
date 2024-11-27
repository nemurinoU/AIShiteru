package com.prototype.aishiteru

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.MessageItem
import kotlinx.coroutines.*

class DataGenerator {
/*
    companion object {
        private val db = FirebaseFirestore.getInstance()

        private val cast1 : CastItem = CastItem ("Limon C. Saida", R.drawable.avatar_lim, "0x1")
        private val cast2 : CastItem = CastItem ("Diiaphy Nakao", R.drawable.avatar_diia, "0x2")
        private val cast3 : CastItem = CastItem ("Doko Niiruno", R.drawable.avatar_doko, "0x3")
        private val cast4 : CastItem = CastItem ("Wolf Chii", R.drawable.avatar_wolp, "0x4")
        private val cast5 : CastItem = CastItem ("{user}", R.drawable.ai, "-1")

        private val castList = listOf(cast1, cast2, cast3, cast4)

        fun generateMessages(): ArrayList<MessageItem> {
            val messages = ArrayList<MessageItem>()
            val currentDate = CustomDate(2024, 11, 18, 6, 0)

            for (cast in castList) {
                // Add a message from the cast member to the user
                messages.add(MessageItem("Hello from ${cast.name}!", currentDate, cast, cast5))
                // Add a reply from the user to the cast member
                messages.add(MessageItem("Reply from ${cast5.name} to ${cast.name}", currentDate, cast5, cast))
                // Add another message from the user to the cast member
                messages.add(MessageItem("How are you, ${cast.name}?", currentDate, cast5, cast))
                // Add a reply from the cast member to the user
                messages.add(MessageItem("I'm good, ${cast5.name}. How about you?", currentDate, cast, cast5))
            }

            return messages
        }

        fun loadMessagesToDatabase() {
            GlobalScope.launch {


                val currentDate = CustomDate(2024, 11, 18, 6, 0)

                val messagesCollection = db.collection("messages")

                /***
                 * For my sake, I'm putting in the pre-generated data in the database so I don't have to type it all again.
                 */
                for (cast in castList) {
                    val messages = ArrayList<MessageItem>()

                    // Add a message from the cast member to the user
                    messages.add(MessageItem("Hello from ${cast.name}!", currentDate, cast, cast5))
                    // Add a reply from the user to the cast member
                    messages.add(
                        MessageItem(
                            "Reply from ${cast5.name} to ${cast.name}",
                            currentDate,
                            cast5,
                            cast
                        )
                    )
                    // Add another message from the user to the cast member
                    messages.add(
                        MessageItem(
                            "How are you, ${cast.name}?",
                            currentDate,
                            cast5,
                            cast
                        )
                    )
                    // Add a reply from the cast member to the user
                    messages.add(
                        MessageItem(
                            "I'm good, ${cast5.name}. How about you?",
                            currentDate,
                            cast,
                            cast5
                        )
                    )

                    val cast_name = cast.name
                    val cast_uid = cast.userId
                    val cast_ava = cast.imageId
                    var ctr = 0

                    for (item in messages) {

                        val new_msg = mapOf(
                            "add_time" to mapOf(
                                "year" to currentDate.getYear().toString(),
                                "month" to currentDate.getMonth().toString(),
                                "day" to currentDate.getDay().toString(),
                                "hour" to currentDate.getHour().toString(),
                                "min" to currentDate.getMin().toString()
                            ),
                            "content" to item.text,
                            "from_uid" to cast.userId,
                            "msg_num" to ctr
                        )
                        ctr = ctr + 1

                        messagesCollection
                            .whereEqualTo("from_name", cast_name)
                            .whereEqualTo("from_uid", cast_uid)
                            .whereEqualTo("to_name", "Francis")
                            .whereEqualTo("to_uid", "iceswiftslade2002@gmail.com")
                            .get()
                            .addOnSuccessListener { snapshot: QuerySnapshot ->
                                if (snapshot.isEmpty) {
                                    val msgsRef = messagesCollection.document()
                                    val msgsFields = mapOf(
                                        "from_ava" to cast_ava,
                                        "from_name" to cast_name,
                                        "from_uid" to cast_uid,
                                        "to_name" to "Francis",
                                        "to_uid" to "iceswiftslade2002@gmail.com"
                                    )
                                    msgsRef.set(msgsFields, SetOptions.merge())
                                        .addOnSuccessListener {
                                            println("New chatroom created: ${msgsRef.id}")
                                            // create the msglist within the chatroom
                                            val msgList = msgsRef.collection("msglist")
                                            msgList.add(new_msg)
                                                .addOnSuccessListener {
                                                    println("Review added to subcollection: ${it.id}")
                                                }
                                                .addOnFailureListener { exception ->
                                                    println("Error adding review: ${exception}")
                                                }

                                        }
                                        .addOnFailureListener { exception ->
                                            println("Error creating product document: ${exception}")
                                        }
                                } else {
                                    // 4. Product Document Exists, Add Review to Subcollection
                                    val existingDocument = snapshot.documents[0]
                                    val msgsRef = messagesCollection.document(existingDocument.id) // Get the existing document reference
                                    val msgList = msgsRef.collection("msglist")
                                    msgList.add(new_msg)
                                        .addOnSuccessListener {
                                            println("Review added to subcollection: ${it.id}")
                                        }
                                        .addOnFailureListener { exception ->
                                            println("Error adding review: ${exception}")
                                        }
                                }
                            }

                        myDelayedFunction()

                    }

                }
            }
        }

        fun loadCast(): ArrayList<CastItem> {
            return ArrayList(castList)
        }

        fun loadCastToDatabase() {
            for (item in castList) {
                val docRef = db.collection("cast").document(item.userId) // Assuming email is unique

                // Option 1: Replace the entire document
                docRef.set(item)
                    .addOnSuccessListener {
                        Log.d(
                            "Firestore",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w("Firestore", "Error writing document", e) }

                // Option 2: Merge data into the document (if it exists)
                docRef.set(item, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(
                            "Firestore",
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w("Firestore", "Error writing document", e) }
            }
        }


        suspend fun myDelayedFunction() {
            delay(2000) // Delay for 2 seconds
            // ... your code ...
        }


    }*/
}