package com.example.githubuserapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapi.adapter.SectionsPagerAdapter
import com.example.githubuserapi.model.User
import com.example.githubuserapi.viewmodel.UserDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        userDetailViewModel.setUser(user.username)

        userDetailViewModel.getUser().observe(this, Observer { userItem ->
            userItem?.let {
                textUsername.text = userItem.username
                textGithubUrl.text = userItem.githubUrl
                textFollowers.text = userItem.followersCount.toString()
                textFollowing.text = userItem.followingCount.toString()
                textRepositories.text = userItem.repositoriesCount.toString()
                Picasso.get().load(userItem.avatar)
                    .placeholder(R.drawable.dummy_avatar)
                    .into(avatar)
//                showLoading(false)
            }
        })


        supportActionBar?.let {
            it.title = user.username
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}