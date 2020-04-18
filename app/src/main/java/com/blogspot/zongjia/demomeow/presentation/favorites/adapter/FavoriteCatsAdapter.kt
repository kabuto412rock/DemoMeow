package com.blogspot.zongjia.demomeow.presentation.favorites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_cat.view.*
import kotlin.properties.Delegates

class FavoriteCatsAdapter(val onCatClicked: ((Cat) -> Unit)): RecyclerView.Adapter<FavoriteCatsAdapter.CatViewHolder>() {
    // Our data list is going to be notified when we assign a new list of data to it
    private var catList: List<Cat> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cat, parent, false)
        val holder = CatViewHolder(view)
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onCatClicked.invoke(catList[holder.adapterPosition])
            }
        }

        return holder
    }

    override fun getItemCount(): Int = catList.size

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
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
        companion object{
            val requestOptions = RequestOptions().centerCrop()
        }
        fun bind(cat: Cat) {

            // Load images using Glide library
            Glide.with(itemView.context)
                .load(cat.imageUrl)
                .apply (
                    RequestOptions.placeholderOf(R.drawable.ic_strange_cat)
                )
                .apply(requestOptions)// it's centerCrop()
                .thumbnail()
                .into(itemView.itemCatImageView)
        }

    }
}