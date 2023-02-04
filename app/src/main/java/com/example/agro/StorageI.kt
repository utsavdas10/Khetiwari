package com.example.agro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agro.api.RetrofitHelper
import com.example.agro.api.StoryBlokService
import com.example.agro.repository.StoryRepository
import com.example.agro.viewModel.MainViewModel
import com.example.agro.viewModel.MainViewModelFactory

class StorageI : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_i)

        val storyService = RetrofitHelper.getInstance().create(StoryBlokService::class.java)
        val repository = StoryRepository(storyService)


        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.stories.observe(this, Observer { content->
            val jsonString1 = content.story.content.Description.content[0].content[0].text
            val jsonString2 = content.story.content.Description.content[1].content[0].text

            findViewById<TextView>(R.id.storagetext).text = jsonString1 +"\n\n"+jsonString2

        })
    }
}