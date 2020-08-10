package com.example.githubuserapi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapi.R
import com.example.githubuserapi.UserDetailActivity
import com.example.githubuserapi.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItem : User) {
            with(itemView) {
                textUsername.text = userItem.username
                textGithubUrl.text = userItem.githubUrl
                Picasso.get().load(userItem.avatar)
                    .placeholder(R.drawable.dummy_avatar)
                    .into(avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = mData[position]
        holder.bind(user)

        holder.itemView.setOnClickListener{
            val userDetailIntent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
            holder.itemView.context.startActivity(userDetailIntent)
        }
    }

}