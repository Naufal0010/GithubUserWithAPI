package com.personal.githubuserwithapi.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Following (
    var username: String?,
    var name: String?,
    var avatar: String?,
    var location: String?,
    var company: String?,
    var repository: String?,
    var followers: String?,
    var following: String?
) : Parcelable