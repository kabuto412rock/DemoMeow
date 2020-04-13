package com.blogspot.zongjia.demomeow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blogspot.zongjia.demomeow.presentation.main.MainViewModel
import com.blogspot.zongjia.demomeow.presentation.main.adapter.CatAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
const val NUMBERS_OF_COLUMN = 3

class MainFragment : Fragment() {
    // Instantiate viewModel with Koin
    private val viewModel: MainViewModel by viewModel()
    private lateinit var catAdapter : CatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // The click listener for displaying a cat image in detail activity
        val onCatClicked: (imageUrl: String )-> Unit =  {imageUrl->
            viewModel.catClicked(imageUrl)
        }
        // Instantiate our custom Adapter
        catAdapter = CatAdapter(onCatClicked)
        catsRecyclerView.apply {
            // Displaying data in a Grid design
            layoutManager = GridLayoutManager(
                this@MainFragment.context,
                NUMBERS_OF_COLUMN
            )
            adapter = catAdapter
        }
        // Initiate the observer on viewModel fields and then starts the API request
        initViewModel()
    }

    private fun initViewModel() {
        // 觀察catList和當取得新CatList回報時更新adapter
        viewModel.catsList.observe(viewLifecycleOwner, Observer {newCatsList ->
            catAdapter.updateData(newCatsList)
        })

        // 當showLoading的值為true時顯示ProgressBar，值為false時隱藏ProgressBar
        viewModel.showLoading.observe(viewLifecycleOwner, Observer {showLoading ->
            mainProgressBar.visibility = if (showLoading) View.VISIBLE else View.GONE
        })

        // 當showError值變動時，，使用Toast顯示錯誤訊息!
        viewModel.showError.observe(viewLifecycleOwner, Observer{ showError ->
            Toast.makeText(this.activity, showError, Toast.LENGTH_SHORT).show()
        })
//        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {imageUrl ->
//            if (imageUrl != null) startActivity(DetailActivity.getStartIntent(this, imageUrl))
//        })
        // TODO:: 導覽到下一個Detail Fragment
        viewModel.loadCats()

    }

}
