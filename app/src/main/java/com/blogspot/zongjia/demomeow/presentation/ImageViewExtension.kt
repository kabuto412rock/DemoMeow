package com.blogspot.zongjia.demomeow.presentation

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(imageUrl: String ) {

    Glide.with(this)
        .load(imageUrl)
        .apply(RequestOptions().centerCrop())
        .into(this)
}