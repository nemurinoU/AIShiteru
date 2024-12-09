package com.prototype.aishiteru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.QuizAdapter
import com.prototype.aishiteru.classes.Quiz
import com.prototype.aishiteru.databinding.FragmentQuizBinding
import com.prototype.aishiteru.helpers.DataHelper

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val quizList: ArrayList<Quiz> = DataHelper.loadQuizData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        binding.quizRecycler.adapter = QuizAdapter(quizList, object : QuizAdapter.OnItemClickListener {
            override fun onItemClick(quiz: Quiz) {
                Toast.makeText(requireContext(), quiz.title, Toast.LENGTH_SHORT).show()
            }
        })
        binding.quizRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.fabSwipe.setOnClickListener {
            findNavController().navigate(R.id.action_QuizFragment_to_NewsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}