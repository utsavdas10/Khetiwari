package com.example.agro.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.agro.api.StoryBlokService1
import com.example.agro.api.StoryBlokService2
import com.example.agro.model1.Content

class StoryRepository2 (private val StoryService : StoryBlokService2) {

    suspend fun getStories(storiesLiveData: MutableLiveData<Content>){
        val title = StoryService.getStories()
        if (title.isSuccessful) {
            if(title.body() != null){
                Log.d("Repo", title.body().toString())
                storiesLiveData.postValue(title.body())
            }
        }
    }
}