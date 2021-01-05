package com.exercise.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.exercise.consumerapp.adapter.FavoriteAdapter
import com.exercise.consumerapp.databinding.ActivityFavoriteBinding
import com.exercise.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.exercise.consumerapp.model.Favorite
import com.exercise.consumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

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
        adapter = FavoriteAdapter()
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