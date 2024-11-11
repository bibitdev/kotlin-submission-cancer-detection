package com.dicoding.asclepius.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ArticleMainBinding
import com.dicoding.asclepius.response.ApiConfig
import com.dicoding.asclepius.response.ResponseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Article : AppCompatActivity() {
    private lateinit var binding: ArticleMainBinding
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArticleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Artikel"
            setDisplayHomeAsUpEnabled(true)
        }

        setupRecyclerView()
        fetchArticles()
    }

    private fun setupRecyclerView() {
        adapter = ArticleAdapter()
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        binding.rvArticle.adapter = adapter
    }

    private fun fetchArticles() {
        ApiConfig.apiService.getHealthArticles().enqueue(object : Callback<ResponseApi> {
            override fun onResponse(call: Call<ResponseApi>, response: Response<ResponseApi>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    adapter.submitList(articles)
                } else {
                    Log.e("HealthArticlesActivity", "Failed to load articles")
                }
            }

            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Log.e("HealthArticlesActivity", "Error: ${t.message}")
            }
        })
    }


}