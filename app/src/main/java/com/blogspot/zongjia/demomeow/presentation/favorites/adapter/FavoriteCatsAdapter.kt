package com.blogspot.zongjia.demomeow.presentation.favorites.adapter

import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_swipe_cat.view.*
import kotlin.properties.Delegates


class FavoriteCatsAdapter(val onCatClicked:((Cat) -> Unit), val onCatRemoved: ((Cat) -> Unit)): RecyclerView.Adapter<FavoriteCatsAdapter.CatViewHolder>() {
    companion object {
        private val PENDING_REMOVAL_TIMEOUT: Long = 3000 // 3sec
    }

    // Our data list is going to be notified when we assign a new list of data to it
    private var catList: List<Cat> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }
    private var itemsPendingRemoval: MutableList<Cat> = emptyList<Cat>().toMutableList()
    private val handler: Handler = Handler() // hanlder for running delayed runnables

    var pendingRunnables: HashMap<Cat, Runnable> =
        HashMap() // map of items to pending runnables, so we can cancel a removal if need be


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCatsAdapter.CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_swipe_cat, parent, false)
        val holder = CatViewHolder(view)
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onCatClicked.invoke(catList[holder.adapterPosition])
            }
        }

        return holder
    }

    override fun getItemCount(): Int = catList.size

    override fun onBindViewHolder(holder: FavoriteCatsAdapter.CatViewHolder, position: Int) {
        if (position == RecyclerView.NO_POSITION) return

        val cat = catList[position]
//        holder.bind(cat)

        if(itemsPendingRemoval.contains(cat)) {
            // Show itemView in "show undo button" state
            holder.itemView.setBackgroundColor(Color.RED)
            holder.itemView.itemCatImageView.visibility = View.GONE
            holder.itemView.undoDeleteCatButton.visibility = View.VISIBLE
            holder.itemView.undoDeleteCatButton.setOnClickListener {
                // user wants to undo the removal, let's cancel the pending task
                val pendingRemovalRunnable = pendingRunnables[cat]
                pendingRunnables.remove(cat)
                if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
                itemsPendingRemoval.remove(cat)
                // this will rebind the row in normal state
                notifyItemChanged(catList.indexOf(cat))
            }
        }else {
            // Show itemView in "normal" state
            holder.itemView.apply {
                setBackgroundColor(Color.WHITE)
                itemCatImageView.visibility = View.VISIBLE
                undoDeleteCatButton.visibility = View.GONE
                undoDeleteCatButton.setOnClickListener(null)
            }
            holder.bind(cat)
        }
    }
    // Update recyclerView's data
    fun updateData(newCatList: List<Cat>) {
        catList = newCatList
    }
    fun pendingRemoval(position: Int) {
        val cat: Cat = catList[position]
        if (!itemsPendingRemoval.contains(cat)) {
            itemsPendingRemoval.add(cat)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the item
            val pendingRemovalRunnable = Runnable { remove(catList.indexOf(cat)) }
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT)
            pendingRunnables[cat] = pendingRemovalRunnable
        }
    }
//
    fun remove(position: Int) {
        val cat: Cat = catList.get(position)
        if (itemsPendingRemoval.contains(cat)) {
            itemsPendingRemoval.remove(cat)
        }
        if (catList.contains(cat)) {
            onCatRemoved(cat)
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        val cat: Cat = catList.get(position)
        return itemsPendingRemoval.contains(cat)
    }

    class CatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val requestOptions = RequestOptions().centerCrop()

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