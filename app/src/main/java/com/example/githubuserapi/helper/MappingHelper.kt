package com.example.githubuserapi.helper

import android.database.Cursor
import com.example.githubuserapi.db.UserContract
import com.example.githubuserapi.model.User

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                val githubUrl = getString(getColumnIndexOrThrow(UserContract.UserColumns.GITHUB_URL))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR))
                usersList.add(User(id, username, avatar, githubUrl))
            }
        }
        return usersList
    }
}