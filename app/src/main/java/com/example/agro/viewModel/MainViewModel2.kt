package com.example.agro.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agro.model1.Content
import com.example.agro.repository.StoryRepository1
import com.example.agro.repository.StoryRepository2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel2(private val repository: StoryRepository2): ViewModel()  {

    val stories = MutableLiveData<Content>()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStories(stories)
        }
    }
}