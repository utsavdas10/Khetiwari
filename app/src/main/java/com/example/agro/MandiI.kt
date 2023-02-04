package com.example.agro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agro.api.RetrofitHelper
import com.example.agro.api.StoryBlokService1
import com.example.agro.api.StoryBlokService2
import com.example.agro.repository.StoryRepository1
import com.example.agro.repository.StoryRepository2
import com.example.agro.viewModel.MainViewModel1
import com.example.agro.viewModel.MainViewModel2
import com.example.agro.viewModel.MainViewModelFactory1
import com.example.agro.viewModel.MainViewModelFactory2

class MandiI : AppCompatActivity() {
    lateinit var mainViewModel2: MainViewModel2
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi_i)

        val storyService = RetrofitHelper.getInstance().create(StoryBlokService2::class.java)
        val repository = StoryRepository2(storyService)


        mainViewModel2 = ViewModelProvider(this, MainViewModelFactory2(repository))[MainViewModel2::class.java]

        mainViewModel2.stories.observe(this, Observer { content->
            val jsonString1 = content.story.content.Description.content[0].content[0].text
            val jsonString2 = content.story.content.Description.content[1].content[0].text

            findViewById<TextView>(R.id.manditext).text = jsonString1 +"\n\n"+jsonString2

        })
    }
}
