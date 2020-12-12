package com.personal.githubuserwithapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.personal.githubuserwithapi.databinding.ActivityDetailBinding
import com.personal.githubuserwithapi.fragment.SectionsPagerAdapter
import com.personal.githubuserwithapi.model.User

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f

        setDataToDetail()
    }

    private fun setDataToDetail() {
        val listUser = intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User
        Glide.with(this)
            .load(listUser.avatar)
            .into(binding.circleImageView)
        binding.tvName.text = listUser.name
        binding.tvLocation.text = listUser.location
        binding.tvCompany.text = listUser.company
        binding.tvFollowersCount.text = listUser.followers
        binding.tvFollowingCount.text = listUser.following

        supportActionBar?.title = listUser.username
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
    
    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }
}