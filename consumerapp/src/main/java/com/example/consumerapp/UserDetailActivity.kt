package com.example.consumerapp

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.consumerapp.adapter.SectionsPagerAdapter
import com.example.consumerapp.db.FavoriteContract
import com.example.consumerapp.db.FavoriteContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.helper.MappingHelper
import com.example.consumerapp.model.Favorite
import com.example.consumerapp.viewmodel.UserDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.coroutines.*

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var userDetailViewModel: UserDetailViewModel
    private var user = Favorite()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        user = intent.getParcelableExtra<Favorite>(EXTRA_USER) as Favorite
        textUsername.text = user.username
        textGithubUrl.text = user.githubUrl

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        userDetailViewModel.setUser(user.username)

        userDetailViewModel.getUser().observe(this, Observer { userItem ->
            userItem?.let {
                textFollowers.text = userItem.followersCount.toString()
                textFollowing.text = userItem.followingCount.toString()
                textRepositories.text = userItem.repositoriesCount.toString()
                Picasso.get().load(userItem.avatar)
                    .placeholder(R.drawable.dummy_avatar)
                    .into(avatar)
                showLoading(false)
            }
        })

        supportActionBar?.let {
            it.title = user.username
            it.setDisplayHomeAsUpEnabled(true)
        }

        isFavorite = checkFavorite(user.username)
        changeFloatingIcon(isFavorite)

        btn_favorite.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun showLoading(state: Boolean) {
        if(state) {
            ln_user_follow.visibility = View.GONE
            progressBar.visibility =  View.VISIBLE
        } else {
            ln_user_follow.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_favorite -> {
                if(!isFavorite){
                    val values = ContentValues()
                    values.put(FavoriteContract.FavoriteColumns.USERNAME, user.username)
                    values.put(FavoriteContract.FavoriteColumns.AVATAR, user.avatar)
                    values.put(FavoriteContract.FavoriteColumns.GITHUB_URL, user.githubUrl)

                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, R.string.favorite_success, Toast.LENGTH_SHORT).show()
                }
                else {
                    contentResolver.delete(Uri.parse(CONTENT_URI.toString() + "/" + user.username), null, null)
                    Toast.makeText(this, R.string.remove_favorite_success, Toast.LENGTH_SHORT).show()
                }
                isFavorite = !isFavorite
                changeFloatingIcon(isFavorite)
            }
        }
    }

    fun checkFavorite(username: String):Boolean{
        var isFav = false
        val cursor = contentResolver.query(Uri.parse("$CONTENT_URI/$username"), null, null, null, null)
        if (cursor != null) {
            val users = MappingHelper.mapCursorToArrayList(cursor)
            cursor.close()
            isFav = users.size > 0
        }
        return isFav
    }

    fun changeFloatingIcon(isFav: Boolean){
        if(isFav){
            btn_favorite.setImageResource(R.drawable.ic_favorite_fill)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}