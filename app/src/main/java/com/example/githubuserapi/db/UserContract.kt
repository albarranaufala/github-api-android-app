package com.example.githubuserapi.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {
    const val AUTHORITY = "com.example.githubuserapi"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "users"
            const val _ID = "id"
            const val USERNAME = "username"
            const val GITHUB_URL = "github_url"
            const val AVATAR = "avatar"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}