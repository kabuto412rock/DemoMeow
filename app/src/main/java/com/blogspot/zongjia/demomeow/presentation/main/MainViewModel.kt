package com.blogspot.zongjia.demomeow.presentation.main

import androidx.lifecycle.MutableLiveData
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.repositories.CatRepository
import com.blogspot.zongjia.demomeow.presentation.base.BaseViewmodel
import com.blogspot.zongjia.demomeow.util.SingleLiveEvent
import com.blogspot.zongjia.demomeow.util.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val catRepository: CatRepository) : BaseViewmodel() {

    val showLoading = MutableLiveData<Boolean>()
    val catsList = MutableLiveData<List<Cat>>()
    val showError = SingleLiveEvent<String>()
    val navigateToDetail = SingleLiveEvent<Cat>()
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
    fun catClicked(cat: Cat) {
        navigateToDetail.value = cat
    }
}