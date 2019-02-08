package com.my.demo.discover

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.my.demo.R
import com.my.demo.network.DoorDashApi
import com.my.demo.network.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_main.*


class RestaurantListActivity : AppCompatActivity(), RestaurantListView {
    private lateinit var presenter: RestaurantPresenter

    private lateinit var restaurantListAdapter: RestaurantListAdapter

    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Discover"

        restaurantListAdapter = RestaurantListAdapter()

        recycler_view.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var page = 1
                private val VISIBLE_THRESHOLD = 5
                private var totalItemCount = 0
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linearLayoutManager = layoutManager as LinearLayoutManager
                    totalItemCount = linearLayoutManager.itemCount
                    if (!loading && linearLayoutManager.findLastVisibleItemPosition() + VISIBLE_THRESHOLD >= totalItemCount) {

                        loading = true
                        page += 1
                        presenter.onLoadMore(page++)
                    }
                }
            })

        }

        presenter = RestaurantPresenter(this, RetrofitClientInstance.retrofitClient.create(DoorDashApi::class.java))

        presenter.onCreate()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun addRestaurants(restaurants: List<Restaurant>) {
        restaurantListAdapter.addRestaurants(restaurants)
        loading = false
    }

    override fun onErrorLoadingRestaurants(error: Throwable) {
        Toast.makeText(this, "Error loading restaurants: $error", Toast.LENGTH_SHORT).show()
    }
}


interface RestaurantListView {
    fun addRestaurants(restaurants: List<Restaurant>)

    fun onErrorLoadingRestaurants(error: Throwable)
}
