package com.blogspot.zongjia.demomeow.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.presentation.loadImage
import kotlinx.android.synthetic.main.fragment_cat_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class CatDetailFragment : Fragment() {
    val args:  CatDetailFragmentArgs by navArgs()

    val viewModel : CatDetailViewModel by viewModel { parametersOf(args.catId, args.imageUrl) }
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

        floatLikeButton.setOnClickListener {
            viewModel.clickLike()
        }
        // initialize CatDetailViewModel
        initViewModel()
    }

    private fun initViewModel() {

        viewModel.likeThisCat.observe(viewLifecycleOwner, Observer {
            // TODO:: show
            if (it == true) {
                floatLikeButton.setImageDrawable(resources.getDrawable(R.drawable.ic_red_heart, context?.theme
                    ))
            }else if(it == false){
                floatLikeButton.setImageDrawable(resources.getDrawable(R.drawable.ic_gray_heart, context?.theme
                ))
            }
        })
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
