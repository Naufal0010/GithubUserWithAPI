package com.exercise.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var location: String? = null,
    var company: String? = null,
    var repository: String? = null,
    var followers: String? = null,
    var following: String? = null
        ) : Parcelable