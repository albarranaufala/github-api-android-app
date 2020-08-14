package com.example.consumerapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.model.Favorite
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel: ViewModel() {
    val following = MutableLiveData<ArrayList<Favorite>>()

    fun setFollowing(username: String){
        val listItems = ArrayList<Favorite>()

        val token = "00eeccda8a24157045d5dad3bc94fe93498839c0"
        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val list = JSONArray(result)
                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItem = Favorite()
                        userItem.id = user.getInt("id")
                        userItem.username = user.getString("login")
                        userItem.avatar = user.getString("avatar_url")
                        userItem.githubUrl = user.getString("html_url")
                        listItems.add(userItem)
                    }
                    following.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<Favorite>>{
        return following
    }
}