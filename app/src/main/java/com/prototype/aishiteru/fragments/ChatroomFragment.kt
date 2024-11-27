package com.prototype.aishiteru.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.prototype.aishiteru.MainActivity
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.CustomDate
import com.prototype.aishiteru.classes.MessageItem
import com.prototype.aishiteru.databinding.FragmentChatroomBinding
import io.github.muddz.styleabletoast.StyleableToast

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatroomFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentChatroomBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var fromName: String
    private lateinit var fromUid: String

    private var incoming_msgs : ArrayList<MessageItem> = ArrayList<MessageItem>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatroomBinding.inflate(inflater, container, false)

        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load the recycler view
        recyclerView = binding.recyclerViewMessages

        // set title bar to recipient name, the person the user is chatting with
        // it got the string via fragment arguments
        this.fromName = arguments?.getString("name").toString()
        this.fromUid = arguments?.getString("uid").toString()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = this.fromName

        // set the adapter and layout manager
        getMessages()

        startRecycler()


        binding.imgSend.setOnClickListener {
            StyleableToast.makeText(
                requireContext(),
                "Message sent!",
                Toast.LENGTH_SHORT,
                R.style.neutralToast
            ).show()
        }
    }

    private fun getMessages () {
        // messages -> key pair -> msg list of two people
        val usersCollection = db.collection("users")
        val msgsCollection = db.collection("messages")
        val mainActivity = activity as? MainActivity
        val SESSION_UID = mainActivity?.getSession().toString()

        var user = ""



        // after i get this
        usersCollection.document(SESSION_UID).get()
            .addOnSuccessListener { snapshot ->
                val data = snapshot.data
                user = data?.get("name").toString()
                println("HELLO HELLO !!!")
            }
            .addOnFailureListener { exception ->
                println("Error getting document: ${exception}")
            }
            .addOnCompleteListener {
                // get the messages where == to the needs
                msgsCollection
                    .whereEqualTo("from_name", fromName)
                    .whereEqualTo("from_uid", fromUid)
                    .whereEqualTo("to_name", user)
                    .whereEqualTo("to_uid", SESSION_UID)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.isEmpty) {
                            val existingDocument = snapshot.documents[0]
                            val msgsRef = msgsCollection.document(existingDocument.id) // Get the existing document reference
                            val msgList = msgsRef.collection("msglist")

                            // Fetch room data once
                            msgsRef.get()
                                .addOnSuccessListener { roomSnap ->
                                    val roomData = roomSnap.data
                                    val botAva = roomData?.get("from_ava") as? Int ?: 0 // Safe cast and default value
                                    val msgTo = roomData?.get("to_name") as? String ?: "" // Safe cast and default value

                                    // Iterate through the documents in the 'msglist' subcollection
                                    msgList.get()
                                        .addOnSuccessListener { msgSnapshot ->
                                            if (!msgSnapshot.isEmpty) {
                                                for (msgDocument in msgSnapshot.documents) {
                                                    // Access the message data
                                                    val msgData = msgDocument.data

                                                    val msgTime = msgData?.get("add_time") as? Map<String, Int>
                                                    val msgContent = msgData?.get("content") as? String
                                                    val msgNum = msgData?.get("msg_num") as? Int

                                                    val year = msgTime?.get("year")?.toInt() ?: 0
                                                    val month = msgTime?.get("month")?.toInt() ?: 0
                                                    val day = msgTime?.get("day")?.toInt() ?: 0
                                                    val hour = msgTime?.get("hour")?.toInt() ?: 0
                                                    val min = msgTime?.get("min")?.toInt() ?: 0

                                                    val currentDate = CustomDate(
                                                        year,
                                                        month,
                                                        day,
                                                        hour,
                                                        min
                                                    )

                                                    // in order to generate the array of messages
                                                    // i need the pertinent data
                                                    val tempCast = CastItem(this.fromName, botAva ?: 0, this.fromUid)

                                                    println("${msgContent.toString()}")
                                                    println("${currentDate}")
                                                    println("${tempCast}")
                                                    println("${msgTo}")
                                                    this.incoming_msgs.add(
                                                        MessageItem(
                                                            msgContent.toString(),
                                                            currentDate,
                                                            tempCast,
                                                            msgTo
                                                        )
                                                    )
                                                }
                                            } else {
                                                println("3 No messages found in the subcollection.")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            println("3 Error getting messages: ${exception}")
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    println("Error getting room data: ${exception}")
                                }
                        } else {
                            println("2 Document cannot be found.")
                        }
                    }
                    .addOnFailureListener { exception ->
                        println("2 Error getting document: ${exception}")
                    }
                    .addOnCompleteListener {
                        println("what ${this.incoming_msgs.size}")
                    }

            }

    }

    private fun startRecycler() {
        this.recyclerView.adapter = MessageAdapter(this.incoming_msgs, this.fromName)
        this.recyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}