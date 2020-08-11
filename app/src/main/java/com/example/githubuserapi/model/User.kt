package com.example.githubuserapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var id: Int = 0,
    var username: String = "",
    var avatar: String? = null,
    var githubUrl: String? = null,
    var followersCount: Int = 0,
    var followingCount: Int = 0,
    var repositoriesCount: Int = 0
) : Parcelable