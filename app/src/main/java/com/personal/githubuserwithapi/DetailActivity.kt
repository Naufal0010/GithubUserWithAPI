package com.personal.githubuserwithapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.personal.githubuserwithapi.databinding.ActivityDetailBinding
import com.personal.githubuserwithapi.fragment.SectionsPagerAdapter
import com.personal.githubuserwithapi.model.User

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var listUser: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}