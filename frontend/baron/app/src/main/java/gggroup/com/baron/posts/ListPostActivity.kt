package gggroup.com.baron.posts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import gggroup.com.baron.R
import gggroup.com.baron.adapter.IItemClickListener
import gggroup.com.baron.adapter.PostAdapter
import gggroup.com.baron.detail.DetailActivity
import gggroup.com.baron.entities.ItemSearch
import gggroup.com.baron.entities.OverviewPost
import gggroup.com.baron.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_list_post.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ListPostActivity : AppCompatActivity(), ListPostContract.View {

    private var posts: ArrayList<OverviewPost>? = null
    private var adapter: PostAdapter? = null
    private var search: ItemSearch? = null
    private var positionSort: Int = 0
    private var currentSelectedPosition = 0
    private lateinit var presenter: ListPostContract.Presenter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_post)

        initToolbar()

        initWaveSwipe()

        initRecyclerView()

        presenter = ListPostPresenter(this)

        val bundle = intent.getBundleExtra("myBundle")
        search = bundle.getParcelable("search")

        presenter.getItemSearch(search?.city, search?.district, search?.minPrice, search?.maxPrice, search?.type_house, null, 1)
    }

    private fun dialogSort() {
        val item = arrayOf("Giá tăng dần", "Giá giảm dần", "Diện tích tăng dần", "Diện tích giảm dần")
        val builder = AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo")
                .setSingleChoiceItems(item, currentSelectedPosition, null)
                .setPositiveButton("Đồng ý") { dialog, _ ->
                    dialog.dismiss()
                    currentSelectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    positionSort = currentSelectedPosition
                    refresh()
                }
                .setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss()
                    currentSelectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                }
        builder.show()
    }

    private fun initRecyclerView() {
        recycler_view.hasFixedSize()
        posts = ArrayList()
        adapter = PostAdapter(posts!!, this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter?.setOnItemClickListener(object : IItemClickListener {
            override fun onClickItem(post: OverviewPost, animationView: ImageView) {
                val intent = Intent(this@ListPostActivity, DetailActivity::class.java)
                intent.putExtra("post_id", post.id)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@ListPostActivity, animationView, getString(R.string.transition_image_detail))
                startActivity(intent, optionsCompat.toBundle())
            }
        })
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.getItemSearch(search?.city, search?.district, search?.minPrice, search?.maxPrice, search?.type_house, positionSort, page + 1)
            }
        }
        recycler_view.addOnScrollListener(scrollListener)
    }

    private fun initWaveSwipe() {
        val executors = Executors.newFixedThreadPool(1)
        executors.execute {
            wave_swipe.setColorSchemeColors(Color.WHITE, Color.WHITE)
            wave_swipe.setWaveColor(Color.argb(200, 0, 176, 255))
        }
        executors.shutdown()
        executors.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.DAYS)
        wave_swipe.setOnRefreshListener {
            refresh()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()

            this.overridePendingTransition(0, R.anim.exit)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> {
                dialogSort()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(0, R.anim.exit)
        finish()
    }

    private fun refresh() {
        adapter?.clearData()
        posts = null
        presenter.getItemSearch(search?.city, search?.district, search?.minPrice, search?.maxPrice, search?.type_house, positionSort, 1)
    }

    override fun showNotification(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: ListPostContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResponse(posts: ArrayList<OverviewPost>?) {
        hideShimmerAnimation()
        adapter?.setData(posts!!)
        wave_swipe.isRefreshing = false
    }

    override fun onFailure(message: String?) {
        showNotification(message)
    }

    override fun onPause() {
        super.onPause()
        System.gc()
        Runtime.getRuntime().gc()
        search = null
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun showShimmerAnimation() {
        shimmer_layout.startShimmerAnimation()
    }

    override fun hideShimmerAnimation() {
        shimmer_layout.stopShimmerAnimation()
        shimmer_layout.visibility = View.GONE
    }
}