package com.my.demo.discover

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.my.demo.R
import com.squareup.picasso.Picasso

class RestaurantListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var restaurantList: MutableList<Restaurant> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return if(getItemViewType(position)==1){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
            RestaurantViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.new_list_item, parent, false)
            NewViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        if(getItemViewType(position)==1){

            (viewHolder as RestaurantViewHolder).bindData(restaurant)
        } else {
            (viewHolder as NewViewHolder).bindData(restaurant)
        }
    }

    fun addRestaurants(restaurants: List<Restaurant>){
        restaurantList.addAll(restaurants)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2  // 1 or 0
    }

}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(restaurant: Restaurant) {
        name.text = restaurant.name
        description.text = restaurant.description
        status.text = restaurant.getShortStatus()
        Picasso.get().load(restaurant.coverImageUrl).into(logoImage)
    }

    private val name: TextView = itemView.findViewById(R.id.restaurant_name)
    private val description:TextView = itemView.findViewById(R.id.description)
    private val logoImage : ImageView = itemView.findViewById(R.id.logo)
    private val status: TextView = itemView.findViewById(R.id.status)
}

class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(restaurant: Restaurant) {
        textview.text = restaurant.name
        textView2.text = restaurant.description
        textView3.text = restaurant.getShortStatus()
    }

    private val textview: TextView = itemView.findViewById(R.id.textView)
    private val textView2:TextView = itemView.findViewById(R.id.textView2)
    private val textView3: TextView = itemView.findViewById(R.id.textView3)
}
