package com.personal.githubuserwithapi.helper

import android.database.Cursor
import com.personal.githubuserwithapi.db.DatabaseContract
import com.personal.githubuserwithapi.entity.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))


                favoriteList.add(Favorite(username, name, avatar))
            }
        }

        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): Favorite {
        var favoriteData = Favorite()
        favoriteCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))

            favoriteData = Favorite(username, name, avatar)
        }

        return favoriteData
    }
}