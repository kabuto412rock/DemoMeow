package com.blogspot.zongjia.demomeow.data.entities

import com.google.gson.annotations.SerializedName

data class Cat(
    val id: String,
    @SerializedName("url")
    val imageUrl: String
)
/*
* {
*
* }
* */