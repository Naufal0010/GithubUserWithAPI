package com.personal.githubuserwithapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.personal.githubuserwithapi.databinding.ActivityDetailBinding
import com.personal.githubuserwithapi.fragment.SectionsPagerAdapter
import com.personal.githubuserwithapi.entity.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f

        setFavoriteIcon()
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
        binding.tvRepositoryCount.text = listUser.repository
        binding.tvFollowersCount.text = listUser.followers
        binding.tvFollowingCount.text = listUser.following

        supportActionBar?.title = listUser.username
    }

    private fun setFavoriteIcon() {
        if (isFavorite) {
            binding.fab.setImageResource(R.drawable.ic_favorite)
            snackBarMessage("Berhasil ditambahkan Favorite")
        }
        else {
            binding.fab.setImageResource(R.drawable.ic_favorite_border)
            snackBarMessage("Dikeluarkan dari Favorite")
        }
    }

    private fun snackBarMessage(message: String) {
        Snackbar.make(binding.fab, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
    
    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }
}