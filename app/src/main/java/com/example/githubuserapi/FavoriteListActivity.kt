package com.example.githubuserapi

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.adapter.UserAdapter
import com.example.githubuserapi.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapi.viewmodel.FavoriteListViewModel
import kotlinx.android.synthetic.main.activity_favorite_list.*
import kotlinx.android.synthetic.main.activity_favorite_list.progressBar

class FavoriteListActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var favoriteListViewModel: FavoriteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        recyclerViewFavorites.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        favoriteListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FavoriteListViewModel::class.java)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean){
                favoriteListViewModel.setFavorites(contentResolver)
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

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

        favoriteListViewModel.setFavorites(contentResolver)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }
}