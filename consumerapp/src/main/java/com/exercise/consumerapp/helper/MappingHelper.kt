package com.exercise.consumerapp.helper

import android.database.Cursor
import com.exercise.consumerapp.db.DatabaseContract
import com.exercise.consumerapp.model.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))

                favoriteList.add(Favorite(username, name, avatar, location, company, repository, followers, following))
            }
        }

        return favoriteList
    }
}