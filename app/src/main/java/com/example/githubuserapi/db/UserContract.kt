package com.example.githubuserapi.db

import android.provider.BaseColumns

internal class UserContract {
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "users"
            const val _ID = "id"
            const val USERNAME = "username"
            const val GITHUB_URL = "github_url"
            const val AVATAR = "avatar"
        }
    }
}