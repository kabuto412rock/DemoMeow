package com.blogspot.zongjia.demomeow.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.presentation.loadImage
import kotlinx.android.synthetic.main.fragment_cat_detail.*

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cat_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cat_detail_menu_action_share -> {
                // 分享給其他人看 貓？！
                shareThisCat()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailCatImage.loadImage(args.imageUrl)
        Log.d("CatDetailFragment", "catId:${args.catId}")
    }

    private fun shareThisCat() {
        val shareIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, "貓圖網址")
            putExtra(Intent.EXTRA_TEXT, args.imageUrl)
            type = "text/plain"
        }, null)
        startActivity(shareIntent)

    }
}
