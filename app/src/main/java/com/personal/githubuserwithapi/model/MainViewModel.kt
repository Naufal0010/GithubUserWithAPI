package com.personal.githubuserwithapi.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {
    val listUserNonMutable = ArrayList<User>()
    private val listUserMutable = MutableLiveData<ArrayList<User>>()

    fun getListUser(): LiveData<ArrayList<User>> {
        return  listUserMutable
    }

    fun setListUser(user: String) {
        val apiKey = "d7959a523b6e0b2bbad26d9c3d2ba073d11193ed"
        val url = "https://api.github.com/search/users?q=$user"

        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    listUserNonMutable.clear()
                    val responseObject = JSONObject("items")
                    val responseArray = responseObject.getJSONArray("items")
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val username = item.getString("login")
                        getListUserDetail(username)
                    }
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure : ", error?.message.toString())
            }

        })
    }

    private fun getListUserDetail(user: String) {
        val apiKey = "d7959a523b6e0b2bbad26d9c3d2ba073d11193ed"
        val url = "https://api.github.com/users/$user"

        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val userData = User(username = responseObject.getString("login"),
                    name = responseObject.getString("name"),
                    avatar = responseObject.getString("avatar_url"),
                    location = responseObject.getString("location"),
                    company = responseObject.getString("company"),
                    repository = responseObject.getString("public_repos"),
                    followers = responseObject.getString("followers"),
                    following = responseObject.getString("following"))

                    listUserNonMutable.add(userData)
                    listUserMutable.postValue(listUserNonMutable)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure : ", error?.message.toString())
            }

        })
    }
}