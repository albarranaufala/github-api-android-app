package com.example.githubuserapi

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapi.adapter.SectionsPagerAdapter
import com.example.githubuserapi.db.UserContract
import com.example.githubuserapi.db.UserHelper
import com.example.githubuserapi.helper.MappingHelper
import com.example.githubuserapi.model.User
import com.example.githubuserapi.viewmodel.UserDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        const val EXTRA_USER = "extra_user"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private lateinit var userDetailViewModel: UserDetailViewModel
    private var user = User()
    private lateinit var userHelper: UserHelper
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
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

        checkFavorite(user.username)

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
                    values.put(UserContract.UserColumns.USERNAME, user.username)
                    values.put(UserContract.UserColumns.AVATAR, user.avatar)
                    values.put(UserContract.UserColumns.GITHUB_URL, user.githubUrl)

                    val result = userHelper.insert(values)
                    if(result > 0) {
                        Toast.makeText(this, R.string.favorite_success, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, R.string.favorite_failed, Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    val result = userHelper.deleteByUsername(user.username)
                    if(result > 0) {
                        Toast.makeText(this, R.string.remove_favorite_success, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, R.string.remove_favorite_failed, Toast.LENGTH_SHORT).show()
                    }
                }
                isFavorite = !isFavorite
                changeFloatingIcon(isFavorite)
            }
        }
    }

    fun checkFavorite(username: String){
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryByUsername(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val users = deferredNotes.await()
            isFavorite = users.size > 0
            changeFloatingIcon(isFavorite)
        }
    }

    fun changeFloatingIcon(isFav: Boolean){
        if(isFav){
            btn_favorite.setImageResource(R.drawable.ic_favorite_fill)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}