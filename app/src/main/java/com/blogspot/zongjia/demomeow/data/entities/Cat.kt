package com.blogspot.zongjia.demomeow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "cats")
data class Cat(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name="imgUrl")
    @SerializedName("url")
    val imageUrl: String
)
/*
* {
*
* }
* */