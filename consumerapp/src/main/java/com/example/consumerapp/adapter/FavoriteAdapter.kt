package com.example.consumerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapp.R
import com.example.consumerapp.UserDetailActivity
import com.example.consumerapp.model.Favorite
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var mData = ArrayList<Favorite>()

    fun setData(items: ArrayList<Favorite>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoriteItem : Favorite) {
            with(itemView) {
                textUsername.text = favoriteItem.username
                textGithubUrl.text = favoriteItem.githubUrl
                Picasso.get().load(favoriteItem.avatar)
                    .placeholder(R.drawable.dummy_avatar)
                    .into(avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return FavoriteViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val user = mData[position]
        holder.bind(user)

        holder.itemView.setOnClickListener{
            val userDetailIntent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
            holder.itemView.context.startActivity(userDetailIntent)
        }
    }
}