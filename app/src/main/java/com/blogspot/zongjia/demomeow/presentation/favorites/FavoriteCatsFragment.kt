package com.blogspot.zongjia.demomeow.presentation.favorites

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.data.entities.Cat
import com.blogspot.zongjia.demomeow.presentation.favorites.adapter.FavoriteCatsAdapter
import com.blogspot.zongjia.demomeow.presentation.favorites.dialogs.DialogClearConfirmFragment
import kotlinx.android.synthetic.main.fragment_favorite_cat.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class FavoriteCatsFragment : Fragment() {
    // Instantiate viewModel with Koin
    private val viewModel: FavoriteCatsViewModel by viewModel()
    private var menuActionClear: MenuItem? = null
    private lateinit var catAdapter : FavoriteCatsAdapter
    private var deleteCat: Cat? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favoiretes_menu, menu)
        menuActionClear = menu.findItem(R.id.favorites_menu_action_clear)
        menuActionClear?.also {
            // 當catsList數量不為0則顯示"清空"的menuItem，反之為0則隱藏menuItem
            it.isVisible = viewModel.catsList.value?.size != 0
        }
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
        val onCatRemoved: (cat: Cat)-> Unit = {
            viewModel.removeCat(it)
        }

        // Instantiate our custom Adapter
        catAdapter = FavoriteCatsAdapter(onCatClicked, onCatRemoved)
        catsRecyclerView.apply {
            // Displaying data in a Grid design
            layoutManager = LinearLayoutManager(
                this@FavoriteCatsFragment.context
            )
            adapter = catAdapter
        }
        // 參考SwipItem作法:https://github.com/nemanja-kovacevic/recycler-view-swipe-to-delete/blob/master/app/src/main/java/net/nemanjakovacevic/recyclerviewswipetodelete/MainActivity.java
        // 初始化RecyclerView需要的SwipeItemHelper
        setUpItemTouchHelper()
        // 初始化ItemAnimationDecorationHelper()
        setUpAnimationDecoratorHelper()
    }
    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
    */
    private fun setUpItemTouchHelper(){
        val simpleItemTouchCallback = object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            lateinit var background: Drawable
            lateinit var xMark: Drawable
            var xMarkMargin: Int = 0
            var initiated : Boolean = false
            fun init() {
                context?.apply {
                    background = ColorDrawable(Color.RED)
                    xMark = ContextCompat.getDrawable(this, R.drawable.ic_delete_white_24dp)!!
                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    xMarkMargin = resources.getDimension(R.dimen.ic_clear_margin).toInt()
                    initiated = true
                }
            }
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val position = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as FavoriteCatsAdapter
                if (adapter.isPendingRemoval(position)) {
                    return 0
                }
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipePosition = viewHolder.adapterPosition
                val adapter = catsRecyclerView.adapter as FavoriteCatsAdapter
                // Has undo on
                adapter.pendingRemoval(swipePosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.adapterPosition == -1) {
                    return
                }
                if (!initiated) {
                    init()
                }
                // draw red background
                background.setBounds(itemView.right + dX.toInt(), itemView.top,itemView.right,itemView.bottom)
                background.draw(c)

                // draw TrashCan mark
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicWidth = xMark.intrinsicWidth
                val intrinsicHeight = xMark.intrinsicHeight

                val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - xMarkMargin
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkBottom = xMarkTop + intrinsicHeight
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                xMark.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(catsRecyclerView)
    }
    /*
    * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
    * after an item is removed.
    */
    private fun setUpAnimationDecoratorHelper() {
        catsRecyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            // we want to cache this and not allocate anything repeatedly in the onDraw method
            var background: Drawable? = null
            var initiated: Boolean = false

            private fun init() {
                background = ColorDrawable(Color.RED)
                initiated = true
            }

            override fun onDraw(c: Canvas ,parent: RecyclerView, state: RecyclerView.State ) {
                if (!initiated) {
                    init()
                }

                // only if animation is in progress
                if (parent.itemAnimator?.isRunning() ?: false) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    var lastViewComingDown: View? = null
                    var firstViewComingUp: View? =null

                    // this is fixed
                    val left: Int = 0
                    val right = parent.width

                    // this we need to find out
                    var top = 0
                    var bottom = 0

                    // find relevant translating views
                    val childCount = parent.layoutManager?.childCount
                    val layoutManager = parent.getLayoutManager()
                    if (childCount == null|| layoutManager == null) return
                    for (i in 0 until childCount) {
                        layoutManager?.getChildAt(i)?.also { child ->
                            if (child.getTranslationY() < 0) {
                                // view is coming down
                                lastViewComingDown = child
                            } else if (child.getTranslationY() > 0) {
                                // view is coming up
                                if (firstViewComingUp == null) {
                                    firstViewComingUp = child;
                                }
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        val lastDownB = lastViewComingDown?.bottom
                        val lastDownTy = lastViewComingDown?.translationY?.toInt()
                        val firstUpTop = firstViewComingUp?.top
                        val firstUpTy = firstViewComingUp?.translationY?.toInt()
                        if (lastDownB !=null && lastDownTy != null && firstUpTop != null && firstUpTy != null) {
                            top = lastDownB + lastDownTy
                            bottom = firstUpTop + firstUpTy
                        }
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        val downB = lastViewComingDown?.bottom
                        val downTy = lastViewComingDown?.translationY?.toInt()
                        if (downB !=null && downTy != null) {
                            top = downB + downTy
                            bottom = downB
                        }
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        val firstUpTop = firstViewComingUp?.getTop()
                        val firstUpTy = firstViewComingUp?.getTranslationY()?.toInt()
                        if (firstUpTop != null && firstUpTy != null) {
                            top = firstUpTop
                            bottom = firstUpTop + firstUpTy
                        }
                    }
                    background?.setBounds(left, top, right, bottom)
                    background?.draw(c)

                }
                super.onDraw(c, parent, state)
            }

        })
    }

    private fun initViewModel() {
        // 觀察catList和當取得新CatList回報時更新adapter
        viewModel.catsList.observe(viewLifecycleOwner, Observer { newCatsList ->
            catAdapter.updateData(newCatsList)
            // 如果貓咪數量為空時
            if (newCatsList.isEmpty()) {
                menuActionClear?.also {item ->
                    // 如果貓咪數量為空且menuItem "清空"正在顯示時，
                    // 觸發無效化當前options menu，該函數會自動觸發onCreateOptionsMenu一遍。
                    if (item.isVisible) activity?.invalidateOptionsMenu()
                }
                // 隱藏 收藏貓咪列表
                catsRecyclerView.visibility = View.INVISIBLE
                // 顯示 沒有貓咪時的照片
                emptyCatImageView.visibility = View.VISIBLE
            }else {
                menuActionClear?.also {item ->
                    // 如果貓咪數量不為空，則顯示menuItem"清空" 提供用戶清空收藏
                    // 如果item正在隱藏，則觸發無效化當前options menu。(本來就顯示則忽略)
                    if (!item.isVisible) activity?.invalidateOptionsMenu()
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
            // 帶著 imageUrl 導覽到下一個Detail Fragment
            val action = FavoriteCatsFragmentDirections.actionFavoriteCatFragmentToCatDetailFragment(catId = cat.id, imageUrl = cat.imageUrl)
            findNavController().navigate(action)
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
