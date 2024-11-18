package com.prototype.aishiteru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prototype.aishiteru.R
import com.prototype.aishiteru.adapters.NewsAdapter
import com.prototype.aishiteru.classes.News
import com.prototype.aishiteru.databinding.FragmentNewsBinding
import com.prototype.aishiteru.helpers.DataHelper
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val newsList: ArrayList<News> = ArrayList()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        newsAdapter = NewsAdapter(newsList, object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(news: News) {
                // Show the WebView pop-up
                binding.webViewContainer.visibility = View.VISIBLE
                binding.newsWebView.apply {
                    settings.javaScriptEnabled = true // Enable JavaScript
                    settings.domStorageEnabled = true // Enable DOM storage
                    settings.cacheMode = WebSettings.LOAD_DEFAULT // Use cache for faster loading if available
                    webViewClient = WebViewClient()
                    loadUrl(news.content) // Assuming 'content' holds the URL of the news
                }
            }
        })
        binding.newsRecycler.adapter = newsAdapter
        binding.newsRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Load news data in a coroutine
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE // Show the loading spinner
            try {
                val fetchedNews = DataHelper.loadNHKNewsData(2) // Fetch news
                newsList.clear() // Clear existing data
                newsList.addAll(fetchedNews) // Add fetched data
                newsAdapter.notifyDataSetChanged() // Notify the adapter
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load news: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE // Hide the loading spinner
            }
        }

        // Handle back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webViewContainer.visibility == View.VISIBLE) {
                    binding.webViewContainer.visibility = View.GONE // Hide the WebView pop-up
                    binding.newsWebView.loadUrl("about:blank") // Clear the WebView content
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed() // Default back action
                }
            }
        })

        binding.fabSwipe.setOnClickListener {
            findNavController().navigate(R.id.action_NewsFragment_to_MapFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
