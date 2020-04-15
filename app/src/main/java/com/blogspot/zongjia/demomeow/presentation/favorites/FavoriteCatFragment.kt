package com.blogspot.zongjia.demomeow.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.presentation.main.adapter.CatAdapter
import kotlinx.android.synthetic.main.fragment_favorite_cat.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FavoriteCatFragment : Fragment() {
    // Instantiate viewModel with Koin
    private val viewModel: FavoriteCatsViewModel by viewModel()
    private lateinit var catAdapter : CatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化RecyclerView的Adapter
        initCatAdapter()
        // Initiate the observer on viewModel fields and then starts the API request
        initViewModel()
    }


    private fun initCatAdapter() {
        // The click listener for displaying a cat image in detail activity
        val onCatClicked: (cat: Cat)-> Unit =  { cat->
            viewModel.catClicked(cat)
        }
        // Instantiate our custom Adapter
        catAdapter = CatAdapter(onCatClicked)
        catsRecyclerView.apply {
            // Displaying data in a Grid design
            layoutManager = LinearLayoutManager(
                this@FavoriteCatFragment.context
            )
            adapter = catAdapter
        }
    }
    private fun initViewModel() {
        // 觀察catList和當取得新CatList回報時更新adapter
        viewModel.catsList.observe(viewLifecycleOwner, Observer { newCatsList ->
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
        // 只有viewModel建立開始，第一次執行會導致loadCats()
        // 避免翻轉時就發送Cat Api的請求，而導致catList變動。
        viewModel.firstLoadOrNot()
    }
}
