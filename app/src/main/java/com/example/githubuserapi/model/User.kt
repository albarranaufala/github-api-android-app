package com.example.githubuserapi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var id: Int = 0,
    var username: String = "",
    var avatar: String? = null,
    var githubUrl: String? = null
) : Parcelable