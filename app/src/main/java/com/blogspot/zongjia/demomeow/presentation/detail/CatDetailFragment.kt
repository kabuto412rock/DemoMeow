package com.blogspot.zongjia.demomeow.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.presentation.loadImage
import com.blogspot.zongjia.demomeow.presentation.main.MainFragmentDirections
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CatDetailFragment : Fragment() {
    val args:  CatDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cat_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailCatImage.loadImage(args.imageUrl)
    }
}
