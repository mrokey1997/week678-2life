package gggroup.com.baron.main.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.facebook.shimmer.ShimmerFrameLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import gggroup.com.baron.R
import gggroup.com.baron.adapter.IItemClickListener
import gggroup.com.baron.adapter.PostAdapter
import gggroup.com.baron.detail.DetailActivity
import gggroup.com.baron.entities.ItemSearch
import gggroup.com.baron.entities.OverviewPost
import gggroup.com.baron.post.PostActivity
import gggroup.com.baron.posts.ListPostActivity
import gggroup.com.baron.utils.OnPagerNumberChangeListener


class HomeFragment : Fragment(), OnPagerNumberChangeListener, HomeContract.View {

    private var posts: ArrayList<OverviewPost>
    private var adapter: PostAdapter? = null
    private var presenter: HomeContract.Presenter
    private var shimmerLayout: ShimmerFrameLayout? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    init {
        posts = ArrayList()
        presenter = HomePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)

        //Bind view
        val pagerIndicator = view.findViewById<IndefinitePagerIndicator>(R.id.viewpager_pager_indicator)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_home)
        val imgTogether = view.findViewById<RoundedImageView>(R.id.img_together)
        val imgRent = view.findViewById<RoundedImageView>(R.id.img_rent)
        shimmerLayout = view.findViewById(R.id.shimmer_layout)

        //Set up toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        //Set up recycler view
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = PostAdapter(posts, context!!)
        adapter?.setType(1)
        recyclerView.adapter = adapter
        adapter?.setOnItemClickListener(object : IItemClickListener {
            override fun onClickItem(post: OverviewPost, animationView: ImageView) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("post_id", post.id)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@HomeFragment.requireActivity(), animationView, getString(R.string.transition_image_detail))
                startActivity(intent, optionsCompat.toBundle())
            }
        })
        pagerIndicator.attachToRecyclerView(recyclerView)

        if (posts.isEmpty())
            presenter.getNewPosts()
        else hideShimmerAnimation()

        //post
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_post)
        fab.setOnClickListener {
            val intent = Intent(this@HomeFragment.requireContext(), PostActivity::class.java)
            startActivity(intent)
        }
        imgTogether.setOnClickListener {
            actionSearch(1)
        }
        imgRent.setOnClickListener {
            actionSearch(0)
        }
        return view
    }

    override fun actionSearch(type: Int) {
        val intent = Intent(this@HomeFragment.requireContext(), ListPostActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("search", ItemSearch(null, null, 0f, 60f, type))
        intent.putExtra("myBundle", bundle)
        val options = ActivityOptions.makeCustomAnimation(this@HomeFragment.requireContext(), R.anim.enter, 0)
        startActivity(intent, options.toBundle())
    }

    override fun onPagerNumberChanged() {
        adapter?.notifyDataSetChanged()
    }

    override fun showNotification(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResponse(posts: ArrayList<OverviewPost>?) {
        hideShimmerAnimation()
        this.posts = posts!!
        adapter?.setData(posts)
    }

    override fun onFailure(message: String?) {
        showNotification(message)
    }

    override fun showShimmerAnimation() {
        shimmerLayout?.startShimmerAnimation()
    }

    override fun hideShimmerAnimation() {
        shimmerLayout?.stopShimmerAnimation()
        shimmerLayout?.visibility = View.GONE
    }
}