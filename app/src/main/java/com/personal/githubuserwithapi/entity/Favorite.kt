package com.personal.githubuserwithapi.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int?,
    var username: String?,
    var name: String?,
    var avatar: String?
    ) : Parcelable