package com.prototype.aishiteru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.AchievementAdapter
import com.prototype.aishiteru.classes.Achievement
import com.prototype.aishiteru.databinding.FragmentAchievementBinding
import com.prototype.aishiteru.helpers.DataHelper

class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    private val achievementList: ArrayList<Achievement> = DataHelper.loadAchievementData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        binding.achRecycler.adapter = AchievementAdapter(achievementList, object : AchievementAdapter.OnItemClickListener {
            override fun onItemClick(achievement: Achievement) {
                Toast.makeText(requireContext(), achievement.description, Toast.LENGTH_SHORT).show()
            }
        })
        binding.achRecycler.layoutManager = GridLayoutManager(requireContext(), 4)

        binding.fabSwipe.setOnClickListener {
            findNavController().navigate(R.id.action_AchievementFragment_to_QuizFragment)
        }

    }

}