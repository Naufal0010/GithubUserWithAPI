package com.personal.githubuserwithapi.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.personal.githubuserwithapi.R
import com.personal.githubuserwithapi.model.Followers
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowersViewModel: ViewModel() {
    val listFollowersUser = ArrayList<Followers>()
    private val listFollowersUserMutable = MutableLiveData<ArrayList<Followers>>()

    fun getListFollowers() : LiveData<ArrayList<Followers>> {
        return listFollowersUserMutable
    }

    fun setListFollowers(context: Context, user: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "334e341ab658ad824ac18099bfda148509be775f")
        client.addHeader("User-Agent", "Request")
        client.setUserAgent("User-Agent")
        val url = "https://api.github.com/users/$user/followers"
        client.get(url, object :  AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for(index in 0 until responseArray.length()) {
                        val username = responseArray.getJSONObject(index).getString("login")
                        getListFollowersUserDetail(context, username)
                    }
                } catch (e : Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("setFollowersFailure ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : ${R.string.code_401}"
                    403 -> "$statusCode : ${R.string.code_403}"
                    404 -> "$statusCode : ${R.string.code_404}"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListFollowersUserDetail(context: Context, user: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "334e341ab658ad824ac18099bfda148509be775f")
        client.addHeader("User-Agent", "Request")
        client.setUserAgent("User-Agent")
        val url = "https://api.github.com/users/$user"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val userData = Followers(username = responseObject.getString("login"),
                        name = responseObject.getString("name"),
                        avatar = responseObject.getString("avatar_url"),
                        location = responseObject.getString("location"),
                        company = responseObject.getString("company"),
                        repository = responseObject.getString("public_repos"),
                        followers = responseObject.getString("followers"),
                        following = responseObject.getString("following"))

                    listFollowersUser.add(userData)
                    listFollowersUserMutable.postValue(listFollowersUser)
                } catch (e: Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("getListFollowers ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : ${R.string.code_401}"
                    403 -> "$statusCode : ${R.string.code_403}"
                    404 -> "$statusCode : ${R.string.code_404}"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}