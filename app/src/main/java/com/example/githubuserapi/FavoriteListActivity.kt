package com.example.githubuserapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.adapter.UserAdapter
import com.example.githubuserapi.db.UserHelper
import com.example.githubuserapi.viewmodel.FavoriteListViewModel
import kotlinx.android.synthetic.main.activity_favorite_list.*
import kotlinx.android.synthetic.main.activity_favorite_list.progressBar
import kotlinx.android.synthetic.main.activity_user_detail.*

class FavoriteListActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var userHelper: UserHelper
    private lateinit var favoriteListViewModel: FavoriteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        recyclerViewFavorites.adapter = adapter

        favoriteListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FavoriteListViewModel::class.java)
        favoriteListViewModel.setFavorites(userHelper)

        favoriteListViewModel.getFavorites().observe(this, Observer { favoriteItems ->
            if(favoriteItems !== null) {
                if(favoriteItems.isEmpty()){
                    Toast.makeText(this, R.string.favorite_empty, Toast.LENGTH_SHORT).show()
                }
                adapter.setData(favoriteItems)
                showLoading(false)
            } else {
                adapter.setData(arrayListOf())
                showLoading(true)
            }
        })

        supportActionBar?.let {
            it.title = resources.getString(R.string.favorite)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()

        favoriteListViewModel.setFavorites(userHelper)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }
}