package com.prototype.aishiteru

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prototype.aishiteru.databinding.FragmentChatlistBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ChatlistFragment : Fragment() {

    private var _binding: FragmentChatlistBinding? = null

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

        binding.fabSwipe.setOnClickListener {
            findNavController().navigate(R.id.action_ChatlistFragment_to_ChatroomFragment)
        }

        binding.testChatClickable.setOnClickListener {
            findNavController().navigate(R.id.action_ChatlistFragment_to_ChatroomFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}