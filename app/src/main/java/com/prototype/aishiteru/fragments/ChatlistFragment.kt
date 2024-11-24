package com.prototype.aishiteru.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prototype.aishiteru.DataGenerator
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.AchievementAdapter
import com.prototype.aishiteru.adapters.ChatlistAdapter
import com.prototype.aishiteru.adapters.MessageAdapter
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.classes.MessageItem
import com.prototype.aishiteru.databinding.FragmentChatlistBinding
import androidx.core.os.bundleOf
import com.prototype.aishiteru.DataListener

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ChatlistFragment : Fragment() {

    private var _binding: FragmentChatlistBinding? = null
    private lateinit var recyclerView: RecyclerView

    // TO DO: Change this to database json or something
    private val castList : ArrayList<CastItem> = DataGenerator.loadCast()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatlistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.testChatClickable.setOnClickListener {
        //
        //}

        recyclerView = binding.recyclerViewCast

        // set the adapter
        //this.recyclerView.adapter = ChatlistAdapter(this.castList)
        this.recyclerView.adapter = ChatlistAdapter(this.castList, object : ChatlistAdapter.OnItemClickListener {
            override fun onItemClick(castItem: CastItem) {
                //val action = ChatlistFragmentDirections.actionChatlistFragmentToChatroomFragment(castItem)
                val bundle = Bundle()
                bundle.putString("name", castItem.name)
                findNavController().navigate(R.id.action_ChatlistFragment_to_ChatroomFragment, bundle)
                //Toast.makeText(requireContext(), "TEST", Toast.LENGTH_SHORT).show()

            }
        })
        this.recyclerView.layoutManager =  GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}