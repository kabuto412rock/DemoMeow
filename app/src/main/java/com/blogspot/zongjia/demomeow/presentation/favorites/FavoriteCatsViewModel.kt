package com.blogspot.zongjia.demomeow.presentation.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.repositories.IFavoriteCatRepository
import com.blogspot.zongjia.demomeow.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteCatsViewModel(private val catRepository: IFavoriteCatRepository) : ViewModel() {
    // 回收所以使用Rxjava的殘渣Disposables
    var compositeDisposables: CompositeDisposable = CompositeDisposable()

    // 判斷是否導向 DetailFragment的狀態，單次觸發
    val navigateToDetail =  SingleLiveEvent<Cat>()
    // 判斷是否為第一次載入的狀態
    val firstLoadPage = MutableLiveData<Boolean>()
    // 判斷是否要顯示 正在載入的(旋轉圖標) 的狀態
    val showLoading = MutableLiveData<Boolean>()
    // 判斷是否要顯示

    // 貓咪列表
    val catsList = MutableLiveData<List<Cat>>()
    // 如果錯誤=>顯示錯誤內容的字串，單次觸發
    val showError = SingleLiveEvent<String>()
    val showClearConfirmDialog = SingleLiveEvent<Boolean>()

    init {
        // 當第一次ViewModel被建立，設為第一次載入的狀態
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
                    showLoading.value = false
                }
            },{
                showError.value = it.message
            })
        )
        firstLoadPage.value = false
    }
    fun showClearCatDialog() {
        showClearConfirmDialog.value = true
    }
    fun clearCats() {
        showLoading.value = true
        compositeDisposables.add(
            catRepository.deleteAll()
                .subscribe {
                    Log.d("clearCats", "清除所有收藏的貓")
                    showLoading.value = false
                }
        )
    }
    fun catClicked(cat: Cat) {
        // TODO::點擊後開啟新的detail
        navigateToDetail.value = cat
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}