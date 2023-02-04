package com.example.agro.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agro.repository.StoryRepository2

class MainViewModelFactory2 (private val repository: StoryRepository2): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel2(repository) as T
    }
}