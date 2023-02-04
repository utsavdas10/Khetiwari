package com.example.agro.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agro.model1.Content
import com.example.agro.model1.ContentX
import com.example.agro.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository): ViewModel() {

    val stories = MutableLiveData<Content>()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getStories(stories)
        }
    }


}