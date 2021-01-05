package com.exercise.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.exercise.consumerapp.databinding.ActivityDetailBinding
import com.exercise.consumerapp.fragment.SectionsPagerAdapter
import com.exercise.consumerapp.model.User


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var listUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listUser = intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User


        setDataToDetail()
        viewPagerSection()
    }


    private fun setDataToDetail() {
        Glide.with(this)
            .load(listUser.avatar)
            .into(binding.circleImageView)
        binding.tvName.text = listUser.name
        binding.tvLocation.text = listUser.location
        binding.tvCompany.text = listUser.company
        binding.tvRepositoryCount.text = listUser.repository
        binding.tvFollowersCount.text = listUser.followers
        binding.tvFollowingCount.text = listUser.following

        supportActionBar?.title = listUser.username
    }


    private fun viewPagerSection() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }
}