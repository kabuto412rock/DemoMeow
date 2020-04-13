package com.blogspot.zongjia.demomeow.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cat.view.*
import kotlin.properties.Delegates

class CatAdapter: RecyclerView.Adapter<CatAdapter.CatViewHolder>() {
    // Our data list is going to be notified when we assign a new list of data to it
    private var catList: List<Cat> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatAdapter.CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun getItemCount(): Int = catList.size

    override fun onBindViewHolder(holder: CatAdapter.CatViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val cat = catList[position]
            holder.bind(cat)
        }
    }
    // Update recyclerView's data
    fun updateData(newCatList: List<Cat>) {
        catList = newCatList
    }
    class CatViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(cat: Cat) {
            // Load images using Glide library
            Glide.with(itemView.context)
                .load(cat.imageUrl)
//                .centerCrop()
                .thumbnail()
                .into(itemView.itemCatImageView)
        }
    }
}