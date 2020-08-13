package com.example.githubuserapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapi.db.UserHelper
import com.example.githubuserapi.helper.MappingHelper
import com.example.githubuserapi.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteListViewModel : ViewModel() {
    val listFavorites = MutableLiveData<ArrayList<User>>()

    fun setFavorites(userHelper: UserHelper){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()

            listFavorites.postValue(favorites)
        }
    }

    fun getFavorites() : LiveData<ArrayList<User>> {
        return listFavorites
    }
}