package com.example.newsfeed

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfeed.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainActivity : AppCompatActivity(), NewsItemCLicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var madapter : NewsFeedAdapter
    private lateinit var selectedCategory: String

    private val bottomsheetFragment = BottomsheetFragment()
    private val chipNames = ArrayList<String>()
    var hashMap : HashMap<String, ArrayList<News>> = HashMap<String, ArrayList<News>> ()

    private val TAG = "MainActivity"
    private val apiKey = "YOUR_API_KEY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Latest News Feed"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

//        function to create the chips
        createChips()
        selectedCategory = chipNames[0]

        madapter = NewsFeedAdapter(this)

//      Making the first api call to get the response for technology endpoint
        fetchData(selectedCategory)

//        Defining the swipe refresh option
        refreshData()

        binding.recyclerView.adapter = madapter
    }

    private fun createChips(){
        chipNames.add("Technology")
        chipNames.add("Business")
        chipNames.add("Entertainment")
        chipNames.add("General")
        chipNames.add("Health")
        chipNames.add("Science")
        chipNames.add("Sports")

        for(i in 0 until chipNames.size){
          val chip = Chip(this)
          chip.text = chipNames[i]
            chip.isCheckable = true
            if(i ==0){
                chip.isChecked = true
            }
            chip.setOnClickListener {
                val clickedChip = it as Chip
                selectedCategory = clickedChip.text.toString()

                if(hashMap.containsKey(selectedCategory)){
                    madapter.updateNews(hashMap[selectedCategory]!!)
                } else {
                    fetchData(selectedCategory)
                }
            }
          binding.newsCategoryChipGroup.addView(chip)
        }
    }

    private fun refreshData(){
        binding.swiperefresh.setOnRefreshListener {
            fetchData(selectedCategory)
        }
    }

    @SuppressLint("NewApi")
    private fun fetchData(cat: String) {
        lifecycleScope.launch() {
            binding.newsLoaderProgressBar.visibility = View.VISIBLE

            val response = try {
                when (cat) {
                    "Technology" -> RetrofitInstance.api.getTechnologyNews(apiKey)
                    "Business" -> RetrofitInstance.api.getBusinessNews(apiKey)
                    "Entertainment" -> RetrofitInstance.api.getEntertainmentNews(apiKey)
                    "General" -> RetrofitInstance.api.getGeneralNews(apiKey)
                    "Health" -> RetrofitInstance.api.getHealthNews(apiKey)
                    "Science" -> RetrofitInstance.api.getScienceNews(apiKey)
                    "Sports" -> RetrofitInstance.api.getSportsNews(apiKey)
                    else -> RetrofitInstance.api.getGeneralNews(apiKey)
                }
            } catch (e: IOException) {
                Log.e(TAG, "IOException : ${e.toString()}")
                withContext (Dispatchers.Main){
                    binding.newsLoaderProgressBar.visibility = View.INVISIBLE
                }
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException : ${e.toString()}")
                withContext (Dispatchers.Main){
                    binding.newsLoaderProgressBar.visibility = View.INVISIBLE
                }
                return@launch
            }

            val newsArray = ArrayList<News>()

            if(response.isSuccessful && response.body()?.articles != null) {
                val apiResponse = response.body()
                val articles = apiResponse?.articles

                for(i in 0 until articles!!.size){

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                    val localDateTime = LocalDateTime.parse(articles[i].publishedAt, formatter)
                    val datePublished = "${localDateTime.month} ${localDateTime.dayOfMonth} ${localDateTime.year}"

                    val news = News(
                        title = articles[i].title ?: "",
                        author = articles[i].author ?: "",
                        url = articles[i].url ?: "",
                        imageUrl = articles[i].urlToImage ?: "",
                        publishedAt = datePublished ?: "",
                        source = articles[i].source.name ?: "",
                        description = "${articles[i].description}\n\n${articles[i].content}"?: ""
                    )
                    newsArray.add(news)
                }
                withContext (Dispatchers.Main){
                    madapter.updateNews(newsArray)
                    hashMap[selectedCategory] = newsArray
                    binding.newsLoaderProgressBar.visibility = View.INVISIBLE
                    binding.swiperefresh.isRefreshing = false
                }

            } else {
                Log.e(TAG, "Response not succesfull")
                withContext (Dispatchers.Main){
                    binding.newsLoaderProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onItemClicked(item: News) {
        val args = Bundle()
        args.putParcelable("news", item)
        bottomsheetFragment.arguments = args
        bottomsheetFragment.show(supportFragmentManager, "BottomSheetDialog")
    }

}