package com.personal.githubuserwithapi

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.personal.githubuserwithapi.adapter.FavoriteAdapter
import com.personal.githubuserwithapi.databinding.ActivityFavoriteBinding
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.personal.githubuserwithapi.db.FavoriteHelper
import com.personal.githubuserwithapi.model.Favorite
import com.personal.githubuserwithapi.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var adapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite_users)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        }
        else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_DETAIL_USER)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_DETAIL_USER, adapter.listFavorite)
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favorites = deferredFavorites.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (favorites.size > 0) {
                binding.recyclerView.visibility = View.VISIBLE
                adapter.listFavorite = favorites
            }
            else {
                adapter.listFavorite = ArrayList()
                showSnackBarMessage(resources.getString(R.string.empty_data))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.recyclerView, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }
}