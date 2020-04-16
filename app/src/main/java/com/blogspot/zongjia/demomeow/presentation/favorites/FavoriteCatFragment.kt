package com.blogspot.zongjia.demomeow.presentation.favorites

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.presentation.favorites.dialogs.DialogClearConfirmFragment
import com.blogspot.zongjia.demomeow.presentation.main.adapter.CatAdapter
import kotlinx.android.synthetic.main.fragment_favorite_cat.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class FavoriteCatFragment : Fragment() {
    // Instantiate viewModel with Koin
    private val viewModel: FavoriteCatsViewModel by viewModel()
    private var menuActionClear: MenuItem? = null
    private lateinit var catAdapter : CatAdapter

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favoiretes_menu, menu)
        menuActionClear = menu.findItem(R.id.favorites_menu_action_clear)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorites_menu_action_clear -> {
                viewModel.showClearCatDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


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
            // TODO::如果為空值，將recycler view隱藏，顯示沒收藏貓咪的圖片，以及menuItem"清空" 要隱藏。
            if (newCatsList.isEmpty()) {
                // 如果貓咪數量為空，隱藏menuItem"清空" 不提供用戶清空收藏
                // 因為這邊有可能menuActionClear變為null，無法設定隱藏，所以layout預設就是隱藏emptyCatImage
                if (menuActionClear?.isVisible == true) menuActionClear?.isVisible = false
                // 隱藏 收藏貓咪列表
                catsRecyclerView.visibility = View.INVISIBLE
                // 顯示 沒有貓咪時的照片
                emptyCatImageView.visibility = View.VISIBLE
            }else {
                // 如果貓咪數量不為空，則顯示menuItem"清空" 提供用戶清空收藏
                if (menuActionClear != null && (menuActionClear?.isVisible == false)){
                    menuActionClear?.isVisible = true
                }
                // 顯示 收藏貓咪列表
                catsRecyclerView.visibility = View.VISIBLE
                // 隱藏 沒有貓咪時的照片
                emptyCatImageView.visibility = View.INVISIBLE
            }
        })

        // 當showLoading的值為true時顯示ProgressBar，值為false時隱藏ProgressBar
        viewModel.showLoading.observe(viewLifecycleOwner, Observer {showLoading ->
            mainProgressBar.visibility = if (showLoading) View.VISIBLE else View.GONE
        })

        // 當showError值變動時，，使用Toast顯示錯誤訊息!
        viewModel.showError.observe(viewLifecycleOwner, Observer{ showError ->
            Toast.makeText(this.activity, showError, Toast.LENGTH_SHORT).show()
        })
        // 當navigateToDetail觸發時，將導覽到CatDetailFragment
        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {cat: Cat ->
            if (cat != null) {
                // 帶著 imageUrl 導覽到下一個Detail Fragment
                val action = FavoriteCatFragmentDirections.actionFavoriteCatFragmentToCatDetailFragment(catId = cat.id?: "", imageUrl = cat.imageUrl ?: "")
                findNavController().navigate(action)
            }
        })
        // 當showClearConfirmDialog觸發時，跳出DialogClearConfirmFragment
        viewModel.showClearConfirmDialog.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val ft: FragmentTransaction = childFragmentManager.beginTransaction()
                val prev = childFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val dialogFragment = DialogClearConfirmFragment()
                dialogFragment.show(ft, "dialog")

            }
        })
        // 只有viewModel建立開始，第一次執行會導致loadCats()
        // 避免翻轉時就發送Cat Api的請求，而導致catList變動。
        viewModel.firstLoadOrNot()
    }
}
