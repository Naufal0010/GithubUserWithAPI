package com.personal.githubuserwithapi.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.personal.githubuserwithapi.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception


class MainViewModel: ViewModel() {
    val listUser = ArrayList<User>()
    private val listUserMutable = MutableLiveData<ArrayList<User>>()

    fun setListUser(user: String, context: Context) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "334e341ab658ad824ac18099bfda148509be775f")
        client.addHeader("User-Agent", "Request")
        client.setUserAgent("User-Agent")
        val url = "https://api.github.com/search/users?q=$user"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    listUser.clear()
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val responseArray = responseObject.getJSONArray("items")
                    for (i in 0 until responseArray.length()) {
                        val username = responseArray.getJSONObject(i).getString("login")
                        getListUserDetail(username, context)
                    }
                } catch (e: Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("failureSetListUser  ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : Jaringan kurang bagus"
                    403 -> "$statusCode : Silakan Coba Beberapa Saat Lagi"
                    404 -> "$statusCode : Not found"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListUserDetail(user: String, context: Context) {
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
                    val userData = User(username = responseObject.getString("login"),
                    name = responseObject.getString("name"),
                    avatar = responseObject.getString("avatar_url"),
                    location = responseObject.getString("location"),
                    company = responseObject.getString("company"),
                    repository = responseObject.getString("public_repos"),
                    followers = responseObject.getString("followers"),
                    following = responseObject.getString("following"))

                    listUser.add(userData)
                    listUserMutable.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("failureOnUserDetail  ", error?.message.toString())
                val message = when(statusCode) {
                    401 -> "$statusCode : Jaringan kurang bagus"
                    403 -> "$statusCode : Silakan Coba Beberapa Saat Lagi"
                    404 -> "$statusCode : Not found"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getListUser(): LiveData<ArrayList<User>> {
        return  listUserMutable
    }
}