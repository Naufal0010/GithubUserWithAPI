package com.exercise.consumerapp.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.exercise.consumerapp.BuildConfig
import com.exercise.consumerapp.R
import com.exercise.consumerapp.model.Following
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowingViewModel: ViewModel() {
    val listFollowingUser = ArrayList<Following>()
    private val listFollowingUserMutable = MutableLiveData<ArrayList<Following>>()

    fun getListFollowing() : LiveData<ArrayList<Following>> {
        return listFollowingUserMutable
    }

    fun setListFollowing(context: Context, user: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "Request")
        client.setUserAgent("User-Agent")
        val url = "https://api.github.com/users/$user/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for(index in 0 until responseArray.length()) {
                        val username = responseArray.getJSONObject(index).getString("login")
                        getListFollowingUserDetail(context, username)
                    }
                } catch (e : Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("setFollowingFailure ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : ${context.resources.getString(R.string.code_401)}"
                    403 -> "$statusCode : ${context.resources.getString(R.string.code_403)}"
                    404 -> "$statusCode : ${context.resources.getString(R.string.code_404)}"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListFollowingUserDetail(context: Context, user: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "Request")
        client.setUserAgent("User-Agent")
        val url = "https://api.github.com/users/$user"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val userData = Following(username = responseObject.getString("login"),
                        name = responseObject.getString("name"),
                        avatar = responseObject.getString("avatar_url"),
                        location = responseObject.getString("location"),
                        company = responseObject.getString("company"),
                        repository = responseObject.getString("public_repos"),
                        followers = responseObject.getString("followers"),
                        following = responseObject.getString("following"))

                    listFollowingUser.add(userData)
                    listFollowingUserMutable.postValue(listFollowingUser)
                } catch (e: Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("getListFollowing ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : ${context.resources.getString(R.string.code_401)}"
                    403 -> "$statusCode : ${context.resources.getString(R.string.code_403)}"
                    404 -> "$statusCode : ${context.resources.getString(R.string.code_404)}"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}