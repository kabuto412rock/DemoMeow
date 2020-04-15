package com.blogspot.zongjia.demomeow.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.data.repositories.IFavoriteCatRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CatDetailViewModel(val catId: String, val imgUrl: String, private val repository: IFavoriteCatRepository): ViewModel() {
    var compositeDisposables: CompositeDisposable = CompositeDisposable()
    val likeThisCat = MutableLiveData<Boolean>(false)
    private val cat: Cat
    init {
        cat = Cat(catId, imgUrl)

        compositeDisposables.add(repository.getOne(catId = cat.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.size == 0) {
                    Log.d("repository.getOne", "onNext(it.size == 0)")
                    likeThisCat.value = false
                }else {// 不為零，代表有這個cat在資料表中
                    Log.d("repository.getOne", "onNext(${it[0].id}, ${it[0].imageUrl})")
                    likeThisCat.value = true
                }
                Log.d("getOne.onNext","likeThisCat.value=${likeThisCat.value}")
            },{
                Log.d("repository.getOne", "onError($it)")
                likeThisCat.value = false
                Log.d("getOne.onError","likeThisCat.value=${likeThisCat.value}")
            }))
    }

    fun clickLike() {
        if (likeThisCat.value == false ) {
            // 添加一個
            compositeDisposables.add(repository.insert(cat).subscribe({
                Log.d("clickLike", "Insert Complete")
            },{
                Log.d("clickLike", "Insert Fail")
            }
            ))
        }else {
            // 刪除一個
            compositeDisposables.add(repository.delete(cat).subscribe({
                Log.d("clickLike", "Delete Complete")
            },{
                Log.d("clickLike", "Delete Fail")
            }))

        }
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}