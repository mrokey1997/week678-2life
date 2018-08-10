package gggroup.com.baron.user.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import gggroup.com.baron.R
import gggroup.com.baron.adapter.ArrayAdapterWithIcon
import gggroup.com.baron.adapter.IItemClickListener
import gggroup.com.baron.adapter.PostAdapter
import gggroup.com.baron.detail.DetailActivity
import gggroup.com.baron.entities.Item
import gggroup.com.baron.entities.OverviewPost
import gggroup.com.baron.entities.ResultGetUser
import gggroup.com.baron.user.password.ChangePasswordActivity
import gggroup.com.baron.user.update.UpdateInfoActivity
import gggroup.com.baron.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File


class ProfileDetailActivity : AppCompatActivity(), ProfileDetailContract.View {
    private var posts = ArrayList<OverviewPost>()
    private var adapter = PostAdapter(posts, this)
    private var presenter: ProfileDetailContract.Presenter? = null
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        presenter = ProfileDetailPresenter(this)
        val token = getSharedPreferences("_2life", Context.MODE_PRIVATE)
                .getString("TOKEN_USER", "")
        (presenter as ProfileDetailPresenter).getUser(token)
        presenter?.getUserPosts(token, 1)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.inflateMenu(R.menu.profile_menu)
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
            //Change the ImageView image source depends on menu item click
                R.id.info -> {
                    startActivity(Intent(this@ProfileDetailActivity, UpdateInfoActivity::class.java))
                    return@OnMenuItemClickListener true
                }
                R.id.change_password -> {
                    startActivity(Intent(this@ProfileDetailActivity, ChangePasswordActivity::class.java))
                }
                R.id.change_avatar -> {
                    val items = arrayListOf(Item("Chụp ảnh mới", R.drawable.photo_camera), Item("Chọn ảnh có sẵn", R.drawable.gallery))
                    val adapter = ArrayAdapterWithIcon(this, items)

                    val builder = AlertDialog.Builder(this)
                            .setTitle("Cập nhật ảnh đại diện")
                            .setAdapter(adapter) { _, position ->
                                changeAvatar(position)
                            }
                    builder.show()
                    return@OnMenuItemClickListener true
                }
            }
            //If above criteria does not meet then default is false;
            false
        })
        //(presenter as ProfileDetailPresenter).updateUser("cd9f944152c1a8095fa9","chính","0393939393",file  )
        initRecyclerView()
        initWaveSwipe()
    }

    private fun changeAvatar(position: Int) {
        when (position) {
            0 -> notAvailable()
            1 -> available()
        }
    }

    private fun available() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .single()
                .toolbarFolderTitle("Bộ sưu tập") // folder selection title
                .toolbarImageTitle("Chọn ảnh")
                .toolbarDoneButtonText("XONG") // done button text
                .start(0) // image selection title
    }

    private fun notAvailable() {
        ImagePicker.cameraOnly().start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images = ImagePicker.getImages(data)
        if (images != null && !images.isEmpty()) {
            val token = getSharedPreferences("_2life", Context.MODE_PRIVATE)
                    .getString("TOKEN_USER", "")
            cat_avatar.setImageBitmap(BitmapFactory.decodeFile(images[0].path))
            presenter?.updateAvatar(token, File(images[0].path))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResponseUserPosts(posts: ArrayList<OverviewPost>?) {
        if (posts != null) {
            adapter.setData(posts)
        }
        wave_swipe.isRefreshing = false
    }

    override fun setPresenter(presenter: ProfileDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResponse(resultGetUser: ResultGetUser) {
        cat_title.text = resultGetUser.user?.full_name
        subtitle.text = resultGetUser.user?.email
        if (resultGetUser.user?.avatar?.contains("http")!!) {
            Glide.with(this).load(resultGetUser.user?.avatar).into(cat_avatar)
        } else Glide.with(this).load("https:" + resultGetUser.user?.avatar).into(cat_avatar)
    }

    private fun initRecyclerView() {
        rv_profile.hasFixedSize()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_profile.layoutManager = layoutManager
        adapter.setType(3)
        rv_profile.adapter = adapter
        adapter.setOnItemClickListener(object : IItemClickListener {
            override fun onClickItem(post: OverviewPost, animationView: ImageView) {
                val intent = Intent(this@ProfileDetailActivity, DetailActivity::class.java)
                intent.putExtra("post_id", post.id)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@ProfileDetailActivity, animationView, getString(R.string.transition_image_detail))
                startActivity(intent, optionsCompat.toBundle())
            }
        })
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                val token = getSharedPreferences("_2life", Context.MODE_PRIVATE)
                        .getString("TOKEN_USER", "")
                presenter?.getUserPosts(token, page + 1)
            }
        }
        rv_profile.addOnScrollListener(scrollListener)

    }

    private fun refresh() {
        val token = getSharedPreferences("_2life", Context.MODE_PRIVATE)
                .getString("TOKEN_USER", "")
        adapter.clearData()
        presenter?.getUserPosts(token, 1)
    }

    private fun initWaveSwipe() {
        wave_swipe.setColorSchemeColors(Color.WHITE, Color.WHITE)
        wave_swipe.setWaveColor(Color.argb(200, 0, 176, 255))
        wave_swipe.setOnRefreshListener {
            refresh()
        }
    }

}
