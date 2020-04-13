package com.blogspot.zongjia.demomeow.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.presentation.loadImage
import kotlinx.android.synthetic.main.activity_detail.*

const val EXTRA_CAT_IMAGE_URL = "EXTRA_CAT_IMAGE_URL"

class DetailActivity : AppCompatActivity() {
    companion object{
        fun getStartIntent(context: Context, imageUrl: String): Intent {
            return Intent(context, DetailActivity::class.java)
                .putExtra(EXTRA_CAT_IMAGE_URL, imageUrl)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

    }
}