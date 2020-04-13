package com.blogspot.zongjia.demomeow

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blogspot.zongjia.demomeow.presentation.adapter.CatAdapter
import com.blogspot.zongjia.demomeow.presentation.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

const val NUMBERS_OF_COLUMN = 3
class MainActivity : AppCompatActivity() {
    // Instantiate viewModel with Koin
    private val viewModel: MainViewModel by viewModel()
    private lateinit var catAdapter : CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instantiate our custom Adapter
        catAdapter = CatAdapter()
        catsRecyclerView.apply {
            // Displaying data in a Grid design
            layoutManager = GridLayoutManager(this@MainActivity, NUMBERS_OF_COLUMN)
            adapter = catAdapter
        }
        // Initiate the observer on viewModel fields and then starts the API request
        initViewModel()
    }
    private fun initViewModel() {
        // 觀察catList和當取得新CatList回報時更新adapter
        viewModel.catsList.observe(this, Observer {newCatsList ->
            catAdapter.updateData(newCatsList)
        })

        // 當showLoading的值為true時顯示ProgressBar，值為false時隱藏ProgressBar
        viewModel.showLoading.observe(this, Observer {showLoading ->
            mainProgressBar.visibility = if (showLoading) View.VISIBLE else View.GONE
        })

        // 當showError值變動時，，使用Toast顯示錯誤訊息!
        viewModel.showError.observe(this, Observer{ showError ->
            Toast.makeText(this, showError, Toast.LENGTH_SHORT).show()
        })
        viewModel.loadCats()
    }
}
