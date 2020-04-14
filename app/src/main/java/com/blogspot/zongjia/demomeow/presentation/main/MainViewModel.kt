package com.blogspot.zongjia.demomeow.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.repositories.CatRepository
import com.blogspot.zongjia.demomeow.util.SingleLiveEvent
import com.blogspot.zongjia.demomeow.util.UseCaseResult
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val catRepository: CatRepository) : ViewModel(), CoroutineScope {
    // Coroutine's background job
    private val job = Job()
    // Define Default thread for Coroutine as Main and add job
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val showLoading = MutableLiveData<Boolean>()
    val catsList = MutableLiveData<List<Cat>>()
    val showError = SingleLiveEvent<String>()
    val navigateToDetail = SingleLiveEvent<String>()
    val firstLoadPage = MutableLiveData<Boolean>()
    init {
        firstLoadPage.value = true
    }
    fun firstLoadOrNot() {
        // 因為firstLoadPage是SingleLiveEvent，所以只有第一次會觸發。
        // 所以底下這行value設為true 或 false其實不影響功能。
        if (firstLoadPage.value == true) {
            loadCats()
        }
    }
    fun loadCats() {
        // Show progressBar during the operation on the MAIN (default) thread
        showLoading.value = true
        // Launch the Coroutine
        launch {
            // Switching from MAIN to IO thread for API operation
            // Update our data list with the new one from API
            val result = withContext(Dispatchers.IO) {catRepository.getCatList()}
            // Hide progressBar once the operation is done on the MAIN (default) thread
            showLoading.value = false
            when(result) {
                is UseCaseResult.Success -> catsList.value = result.data
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
        firstLoadPage.value = false
    }
    fun catClicked(imageUrl: String) {
        navigateToDetail.value = imageUrl
    }
    override fun onCleared() {
        super.onCleared()
        // Clear our job when the linked activity is destroyed to avoid memory leaks
        job.cancel()
    }
}