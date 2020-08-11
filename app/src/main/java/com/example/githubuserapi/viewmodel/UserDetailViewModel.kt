package com.example.githubuserapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapi.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class UserDetailViewModel: ViewModel() {
    val user = MutableLiveData<User>()

    fun setUser(username: String) {
        val token = "00eeccda8a24157045d5dad3bc94fe93498839c0"
        val url = "https://api.github.com/users/$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val objectResult = JSONObject(result)
                    val userResult = User()
                    userResult.id = objectResult.getInt("id")
                    userResult.username = objectResult.getString("login")
                    userResult.avatar = objectResult.getString("avatar_url")
                    userResult.githubUrl = objectResult.getString("html_url")
                    userResult.followersCount = objectResult.getInt("followers")
                    userResult.followingCount = objectResult.getInt("following")
                    userResult.repositoriesCount = objectResult.getInt("public_repos")

                    user.postValue(userResult)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser() : LiveData<User> {
        return user
    }
}