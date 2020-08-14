package com.example.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.FavoriteAdapter
import com.example.consumerapp.db.FavoriteContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.helper.MappingHelper
import com.example.consumerapp.model.Favorite
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()

        rvFavorites.layoutManager = LinearLayoutManager(this)
        rvFavorites.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean){
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.setData(list)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()

            adapter.setData(favorites)
            showLoading(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }
}
