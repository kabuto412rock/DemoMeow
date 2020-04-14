package com.blogspot.zongjia.demomeow.presentation.detail

import androidx.lifecycle.MutableLiveData
import com.blogspot.zongjia.demomeow.presentation.base.BaseViewmodel

class CatDetailViewModel: BaseViewmodel() {
    val likeThisCat = MutableLiveData<Boolean>()

}