package com.blogspot.zongjia.demomeow.presentation.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.repositories.IFavoriteCatRepository
import com.blogspot.zongjia.demomeow.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteCatsViewModel(private val catRepository: IFavoriteCatRepository) : ViewModel() {
    var compositeDisposables: CompositeDisposable = CompositeDisposable()

    val showLoading = MutableLiveData<Boolean>()
    val catsList = MutableLiveData<List<Cat>>()
    val showError = SingleLiveEvent<String>()
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
        // Launch the Rxjava
        compositeDisposables.add(
            catRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                if (it != null) {
                    catsList.value = it
                }
            },{
                showError.value = it.message
            })
        )
        firstLoadPage.value = false
    }
    fun catClicked(cat: Cat) {
        // 暫時不回應點擊
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}