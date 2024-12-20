package com.prototype.aishiteru.fragments

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.BuildConfig
import androidx.core.widget.NestedScrollView
import com.google.firebase.firestore.FirebaseFirestore
import com.prototype.aishiteru.DataGenerator
import com.prototype.aishiteru.MainActivity
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.MessageItem
import com.prototype.aishiteru.databinding.FragmentChatroomBinding
import com.google.firebase.firestore.Query
import com.prototype.aishiteru.helpers.KayraAPI
import com.prototype.aishiteru.helpers.PromptBuilder
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.delay

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatroomFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentChatroomBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var fromName: String
    private lateinit var fromUid: String
    private lateinit var SESSION_UID: String
    private lateinit var user: String
    private var botAva: Long = 0
    private lateinit var promptBuilder : PromptBuilder

    private var incoming_msgs : ArrayList<MessageItem> = ArrayList<MessageItem>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var scrollView : NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatroomBinding.inflate(inflater, container, false)

        scrollView = binding.chatScroll

        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.promptBuilder = PromptBuilder(this.requireContext())
        recyclerView = binding.recyclerViewMessages

        // set title bar to recipient name, the person the user is chatting with
        // it got the string via fragment arguments
        this.fromName = arguments?.getString("name").toString()
        this.fromUid = arguments?.getString("uid").toString()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = this.fromName

        // set the adapter and layout manager
        loadChats()




        binding.imgSend.setOnClickListener {
            /*
            StyleableToast.makeText(
                requireContext(),
                "Message sent!",
                Toast.LENGTH_SHORT,
                R.style.neutralToast
            ).show()
            */

            sendMessage()
        }

    }

    private fun loadChats () {
        // messages -> key pair -> msg list of two people
        val usersCollection = db.collection("users")
        val msgsCollection = db.collection("messages")

        // get the SESSION_UID
        SESSION_UID = getSessionUID()

        // First, we have to get the display name of the user
        usersCollection.document(SESSION_UID).get()
            .addOnSuccessListener { snapshot ->
                val data = snapshot.data
                this.user = data?.get("name").toString()

                msgsCollection
                    .whereEqualTo("from_name", fromName)
                    .whereEqualTo("from_uid", fromUid)
                    .whereEqualTo("to_name", this.user)
                    .whereEqualTo("to_uid", SESSION_UID)
                    .get()
                    .addOnSuccessListener { snapshotX ->
                        if (!snapshotX.isEmpty) {
                            val existingDocument = snapshotX.documents[0]
                            val msgsRef = msgsCollection.document(existingDocument.id) // Get the existing document reference
                            val msgList = msgsRef.collection("msglist")

                            // Fetch room data once
                            msgsRef.get()
                                .addOnSuccessListener { roomSnap ->
                                    val roomData = roomSnap.data
                                    botAva = roomData?.get("from_ava") as Long
                                    val msgTo = roomData?.get("to_name") as? String
                                    // Iterate through the documents in the 'msglist' subcollection
                                    msgList.get()
                                        .addOnSuccessListener { msgSnapshot ->
                                            if (!msgSnapshot.isEmpty) {

                                                for (msgDocument in msgSnapshot.documents) {
                                                    // Access the message data
                                                    val msgData = msgDocument.data

                                                    val msgTime = msgData?.get("add_time") as? Map<String, String>
                                                    val msgContent = msgData?.get("content") as? String
                                                    val msgNum = msgData?.get("msg_num") as? Long
                                                    val msgFrom = msgData?.get("from_uid") as? String

                                                    val year = msgTime?.get("year")
                                                    val month = msgTime?.get("month")
                                                    val day = msgTime?.get("day")
                                                    val hour = msgTime?.get("hour")
                                                    val min = msgTime?.get("min")

                                                    val currentDate = CustomDate(
                                                        year!!.toInt(),
                                                        month!!.toInt(),
                                                        day!!.toInt(),
                                                        hour!!.toInt(),
                                                        min!!.toInt()
                                                    )

                                                    // in order to generate the array of messages
                                                    // i need the pertinent data

                                                    val tempCast = if (msgFrom.toString().startsWith("0x")) {
                                                        CastItem(this.fromName, botAva!!.toInt(), this.fromUid)
                                                    } else {
                                                        CastItem(user, 0, SESSION_UID)
                                                    }

                                                    println("${msgContent.toString()}")
                                                    println("${currentDate}")
                                                    println("${tempCast}")
                                                    println("${msgTo}")
                                                    this.incoming_msgs.add(
                                                        MessageItem(
                                                            msgContent.toString(),
                                                            currentDate,
                                                            tempCast,
                                                            msgTo!!.toString(),
                                                            msgNum!!.toInt()
                                                        )
                                                    )
                                                }

                                                this.incoming_msgs.sortBy { it.msg_num }

                                                startRecycler()


                                            } else {
                                                println("No messages found in the subcollection.")
                                                println("Attempting to start recycler...")
                                                startRecycler()
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            println("3 Error getting messages: ${exception}")
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    println("Error getting room data: ${exception}")
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        println("Error getting document: ${exception}")
                    }
            }
            .addOnFailureListener { exception ->
                println("Error getting document: ${exception}")
            }
    }

    private fun startRecycler() {
        this.recyclerView.adapter = MessageAdapter(this.incoming_msgs, this.fromName)
        this.recyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        jumpToBottom()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSessionUID() : String {
        val mainActivity = activity as? MainActivity
        return mainActivity?.getSessionUID().toString()
    }

    private fun jumpToBottom () {

        val rootView: View = binding.root
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                // Check if the keyboard is visible
                val isKeyboardVisible = keypadHeight > screenHeight * 0.15

                if (isKeyboardVisible) {
                    // Keyboard is visible
                    scrollView.scrollToBottom()
                } else {
                    // Keyboard is hidden
                    scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
                }
            }
        })
    }

    private fun sendMessage () {
        // check if the text box is empty
        val draftMsg = binding.composeBox.text.toString()

        if (!draftMsg.isEmpty()) {
            // if not empty
            // save the message
            // update the UI

            val timestamp = CustomDate()
            val msgsCollection = db.collection("messages")


            // save it to this collection!
            msgsCollection
                .whereEqualTo("from_name", fromName)
                .whereEqualTo("from_uid", fromUid)
                .whereEqualTo("to_name", user)
                .whereEqualTo("to_uid", SESSION_UID)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty()) {
                        // pass
                    } else {
                        val existingDoc = snapshot.documents[0]
                        val msgsRef =
                            msgsCollection.document(existingDoc.id) // Get the existing document reference
                        val msgList = msgsRef.collection("msglist")
                        var newNum = 0

                        msgList.orderBy("msg_num", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener() { snapshot ->
                                if (snapshot.isEmpty()) {
                                    newNum = 0
                                } else {
                                    val tempNum = snapshot.getDocuments().get(0).getLong("msg_num")
                                    newNum = tempNum!!.toInt() + 1
                                }

                                val new_msg = mapOf(
                                    "add_time" to mapOf(
                                        "year" to timestamp.getYear().toString(),
                                        "month" to timestamp.getMonth().toString(),
                                        "day" to timestamp.getDay().toString(),
                                        "hour" to timestamp.getHour().toString(),
                                        "min" to timestamp.getMin().toString()
                                    ),
                                    "content" to draftMsg,
                                    "from_uid" to SESSION_UID,
                                    "msg_num" to newNum
                                )

                                this.incoming_msgs.add(
                                    MessageItem(
                                        draftMsg,
                                        timestamp,
                                        CastItem(user, 0, SESSION_UID),
                                        this.fromName, // recipient
                                        newNum
                                    )
                                )

                                msgList.add(new_msg)
                                    .addOnSuccessListener {
                                        println("Message added to subcollection: ${it.id}")

                                        binding.composeBox.setText("")
                                        recyclerView.adapter?.notifyDataSetChanged()
                                        jumpToBottom()

                                        lifecycleScope.launch {
                                            delay(1000L)
                                            startBreathingAnimation(binding.isTypingView, 4000)
                                            binding.isTypingView.text = "${fromName} is typing..."
                                            delay(4000L)

                                            callKayra()
                                        }

                                    }
                                    .addOnFailureListener { exception ->
                                        println("Error adding review: ${exception}")
                                    }
                            }
                    }
                }
        }
        else {
            // if empty nothing happens
        }
    }

    /***
     * This is where the AI's response get's generated.
     *
     */
    private fun callKayra () {
        binding.isTypingView.text = ""
        ////////  Debugging of prompt builder ////////
        val charName = this.fromName

        val debugString = promptBuilder.buildPrompt(
            charName, // Name of character
            this.user, // Name of user
            1, // Relation level
            false, // Is chatroom Japenis?
            this.incoming_msgs.toTypedArray(), // Conversation history
            null // Additional context like "This roleplay happens in Starbucks, a ____"
        )
        println(debugString)
        //////// End of debugging ////////

        //////// Debugging of text generator ////////
        val kayraAPI = KayraAPI(requireContext())

        lifecycleScope.launch{
            val response = kayraAPI.generateResponse(debugString, charName)
            storeAIResponse(response)
        }
    }

    private fun storeAIResponse (response : String) {
        val timestamp = CustomDate()
        val msgsCollection = db.collection("messages")

        // save it to this collection!
        msgsCollection
            .whereEqualTo("from_name", fromName)
            .whereEqualTo("from_uid", fromUid)
            .whereEqualTo("to_name", user)
            .whereEqualTo("to_uid", SESSION_UID)
            .get()
            .addOnSuccessListener { snapshot ->
                val existingDoc = snapshot.documents[0]
                val msgsRef = msgsCollection.document(existingDoc.id) // Get the existing document reference
                val msgList = msgsRef.collection("msglist")
                var newNum = 0

                msgList.orderBy("msg_num", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener() { snapshot ->
                        if (snapshot.isEmpty()) {
                            newNum = 0
                        }
                        else {
                            val tempNum = snapshot.getDocuments().get(0).getLong("msg_num")
                            newNum = tempNum!!.toInt() + 1
                        }

                        val new_msg = mapOf(
                            "add_time" to mapOf(
                                "year" to timestamp.getYear().toString(),
                                "month" to timestamp.getMonth().toString(),
                                "day" to timestamp.getDay().toString(),
                                "hour" to timestamp.getHour().toString(),
                                "min" to timestamp.getMin().toString()
                            ),
                            "content" to response,
                            "from_uid" to this.fromUid,
                            "msg_num" to newNum
                        )

                        this.incoming_msgs.add(
                            MessageItem(
                                response,
                                timestamp,
                                CastItem(this.fromName, botAva.toInt(), this.fromUid),
                                SESSION_UID,
                                newNum
                            )
                        )

                        msgList.add(new_msg)
                            .addOnSuccessListener {
                                println("Message added to subcollection: ${it.id}")

                                recyclerView.adapter?.notifyDataSetChanged()
                                jumpToBottom()
                            }
                            .addOnFailureListener { exception ->
                                println("Error adding review: ${exception}")
                            }
                    }
            }
    }


    fun NestedScrollView.scrollToBottom() {
        val lastChild = getChildAt(scrollView.childCount - 1)
        val bottom = lastChild.bottom + scrollView.paddingBottom
        val delta = bottom - (scrollView.scrollY+ scrollView.height)
        scrollView.smoothScrollBy(0, delta)
    }



    private fun startBreathingAnimation(textView: TextView, duration : Long) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val animator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0.5f, 1f)
            animator.duration = duration // 2 seconds
            animator.repeatCount = ObjectAnimator.INFINITE
            animator.repeatMode = ObjectAnimator.REVERSE
            animator.start()

        }
    }



}