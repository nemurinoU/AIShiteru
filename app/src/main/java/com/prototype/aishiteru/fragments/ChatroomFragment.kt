package com.prototype.aishiteru.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.DataGenerator
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.MessageItem
import com.prototype.aishiteru.databinding.FragmentChatroomBinding
import io.github.muddz.styleabletoast.StyleableToast

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatroomFragment : Fragment() {

    private var _binding: FragmentChatroomBinding? = null
    private lateinit var recyclerView: RecyclerView

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

        // set the adapter
        this.recyclerView.adapter = MessageAdapter(this.messages)

        this.recyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.imgSend.setOnClickListener {
            StyleableToast.makeText(
                requireContext(),
                "Message sent!",
                Toast.LENGTH_SHORT,
                R.style.neutralToast
            ).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}