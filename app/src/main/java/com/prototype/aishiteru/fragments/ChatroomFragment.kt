package com.prototype.aishiteru.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.BuildConfig
import com.prototype.aishiteru.DataGenerator
import com.prototype.aishiteru.DataListener
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.MessageItem
import com.prototype.aishiteru.databinding.FragmentChatroomBinding
import com.prototype.aishiteru.helpers.KayraAPI
import com.prototype.aishiteru.helpers.PromptBuilder
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatroomFragment : Fragment() {

    private var _binding: FragmentChatroomBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipientName: String

    private val messages : ArrayList<MessageItem> = DataGenerator.generateMessages()

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

        // do stuff here
        /*
        TO DO: You better send the message and make it save to the database :)
         */
        //recycler view
        recyclerView = binding.recyclerViewMessages
        this.recipientName = arguments?.getString("name").toString()

        (requireActivity() as AppCompatActivity).supportActionBar?.title = this.recipientName
                // set the adapter
        this.recyclerView.adapter = MessageAdapter(this.messages, this.recipientName)

        this.recyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        ////////  Debugging of prompt builder ////////
        val charName = this.recipientName
        val promptBuilder = PromptBuilder(this.requireContext())
        val debugString = promptBuilder.buildPrompt(
            charName, // Name of character
            "Jeff", // Name of user
            1, // Relation level
            false, // Is chatroom Japenis?
            this.messages.toTypedArray(), // Conversation history
            null // Additional context like "This roleplay happens in Starbucks, a ____"
        )
        println(debugString)
        //////// End of debugging ////////

        //////// Debugging of text generator ////////
        val kayraAPI = KayraAPI(requireContext())

        binding.imgSend.setOnClickListener {
            // Launch a coroutine
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    // Debugging of generation
                    val generation = kayraAPI.generateResponse(debugString,charName)
                    // Log the generation result
                    println("Resulting Kayra Generation: "+generation)

                    // Show success toast
                    StyleableToast.makeText(
                        requireContext(),
                        "Message sent!",
                        Toast.LENGTH_SHORT,
                        R.style.neutralToast
                    ).show()
                } catch (e: Exception) {
                    // Handle any exceptions
                    e.printStackTrace()
                    StyleableToast.makeText(
                        requireContext(),
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT,
                        R.style.neutralToast
                    ).show()
                }
            }
        }
        //////// End of debugging ////////
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}