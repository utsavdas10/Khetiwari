package com.example.agro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agro.api.RetrofitHelper
import com.example.agro.api.StoryBlokService
import com.example.agro.api.StoryBlokService1
import com.example.agro.repository.StoryRepository
import com.example.agro.repository.StoryRepository1
import com.example.agro.viewModel.MainViewModel
import com.example.agro.viewModel.MainViewModel1
import com.example.agro.viewModel.MainViewModelFactory
import com.example.agro.viewModel.MainViewModelFactory1

class ShopI : AppCompatActivity() {
    lateinit var mainViewModel1: MainViewModel1
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_i)

        val storyService = RetrofitHelper.getInstance().create(StoryBlokService1::class.java)
        val repository = StoryRepository1(storyService)


        mainViewModel1 = ViewModelProvider(this, MainViewModelFactory1(repository))[MainViewModel1::class.java]

        mainViewModel1.stories.observe(this, Observer { content->
            val jsonString1 = content.story.content.Description.content[0].content[0].text
            val jsonString2 = content.story.content.Description.content[1].content[0].text

            findViewById<TextView>(R.id.shoptext).text = jsonString1 +"\n\n"+jsonString2

        })
    }
}