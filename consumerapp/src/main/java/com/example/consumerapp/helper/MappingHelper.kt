package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.db.FavoriteContract
import com.example.consumerapp.model.Favorite

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val usersList = ArrayList<Favorite>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavoriteContract.FavoriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(FavoriteContract.FavoriteColumns.USERNAME))
                val githubUrl = getString(getColumnIndexOrThrow(FavoriteContract.FavoriteColumns.GITHUB_URL))
                val avatar = getString(getColumnIndexOrThrow(FavoriteContract.FavoriteColumns.AVATAR))
                usersList.add(Favorite(id, username, avatar, githubUrl))
            }
        }
        return usersList
    }
}