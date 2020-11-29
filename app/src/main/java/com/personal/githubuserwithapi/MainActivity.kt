package com.personal.githubuserwithapi

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.personal.githubuserwithapi.adapter.ListDataUserAdapter
import com.personal.githubuserwithapi.databinding.ActivityMainBinding
import com.personal.githubuserwithapi.model.MainViewModel
import com.personal.githubuserwithapi.model.User

class MainActivity : AppCompatActivity() {

    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var binding: ActivityMainBinding
    private lateinit var  adapter: ListDataUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"
        showLoading(false)
        showText(true)

        adapter = ListDataUserAdapter(listUser)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        showRecycleList()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showText(state: Boolean) {
        if (state) {
           binding.tvWelcome.visibility = View.VISIBLE
        }
        else {
            binding.tvWelcome.visibility = View.GONE
        }
    }


    private fun showRecycleList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
        binding.recyclerView.adapter = adapter
    }

    private fun viewModel(adapter: ListDataUserAdapter) {
        mainViewModel.getListUser().observe(this,  { listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    listUser.clear()
                    showText(false)
                    showRecycleList()
                    mainViewModel.setListUser(query, applicationContext)
                    showLoading(true)
                    viewModel(adapter)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        return true
    }
}