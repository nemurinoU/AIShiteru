package com.prototype.aishiteru.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.prototype.aishiteru.DataListener

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ChatlistFragment : Fragment() {

    private var _binding: FragmentChatlistBinding? = null
    private lateinit var recyclerView: RecyclerView
    val db = FirebaseFirestore.getInstance()

    private var castList : ArrayList<CastItem> = ArrayList<CastItem>()

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

        recyclerView = binding.recyclerViewCast


        // change the data fed to the adapter
        // get all documents from database
        val castRef = db.collection("cast")

        castRef.get()
            .addOnSuccessListener { result: QuerySnapshot ->
                // iterate through documents
                for (document in result) {
                    val castName = document.getString("name")
                    val castAva = document.getLong("imageId")

                    // Check if castAva is not null before converting to Int
                    if (castAva != null) {
                        val castId = document.getString("userId") // Get the userId

                        // Create the CastItem object
                        val cast = CastItem(castName ?: "", castAva.toInt(), castId ?: "")

                        // Add the cast to the list
                        this.castList.add(cast)

                    } else {
                        // Handle the case where castAva is null (e.g., log a message)
                        Log.w("Firestore", "Missing imageId for cast: $castName")
                    }
                }

                setRecycler(this.castList)
                this.castList = ArrayList<CastItem>()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.w("Firestore", "Error getting documents.", exception)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecycler(cast: ArrayList<CastItem>) {
        // set the adapter
        this.recyclerView.adapter = ChatlistAdapter(cast, object : ChatlistAdapter.OnItemClickListener {
            override fun onItemClick(castItem: CastItem) {
                //val action = ChatlistFragmentDirections.actionChatlistFragmentToChatroomFragment(castItem)
                val bundle = Bundle()
                bundle.putString("name", castItem.name)
                bundle.putString("uid", castItem.userId)
                findNavController().navigate(R.id.action_ChatlistFragment_to_ChatroomFragment, bundle)
                //Toast.makeText(requireContext(), "TEST", Toast.LENGTH_SHORT).show()

            }
        })
        this.recyclerView.layoutManager =  GridLayoutManager(requireContext(), 2)
    }
}