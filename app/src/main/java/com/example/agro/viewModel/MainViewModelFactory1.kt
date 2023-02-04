package com.example.agro.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agro.repository.StoryRepository1

class MainViewModelFactory1(private val repository: StoryRepository1): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel1(repository) as T
    }
}