package com.personal.githubuserwithapi

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.personal.githubuserwithapi.databinding.ActivityDetailBinding
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.personal.githubuserwithapi.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.personal.githubuserwithapi.db.FavoriteHelper
import com.personal.githubuserwithapi.fragment.SectionsPagerAdapter
import com.personal.githubuserwithapi.model.User
import com.personal.githubuserwithapi.helper.MappingHelper

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var isFavorite = false
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var listUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        listUser = intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User

        favoriteCheck()
        binding.fab.setOnClickListener {
            if (isFavorite) {
                setFavoriteIcon(false)
                removedToFavorite()
                snackBarMessage(resources.getString(R.string.removed_favorite))
            }
            else {
                setFavoriteIcon(true)
                addToFavorite()
                snackBarMessage(resources.getString(R.string.added_favorite))
            }
        }

        setFavoriteIcon(isFavorite)
        setDataToDetail()
        viewPagerSection()
    }

    // for checking out if the user is favorite or not
    private fun favoriteCheck() {
        val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
        val mFavorite = MappingHelper.mapCursorToArrayList(cursor)
        for (data in mFavorite) {
            if (listUser.username == data.username) {
                isFavorite = true
            }
        }
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

    private fun setFavoriteIcon(state: Boolean) {
        if (state) {
            binding.fab.setImageResource(R.drawable.ic_favorite)
        }
        else {
            binding.fab.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun addToFavorite() {

        val values = ContentValues().apply {
            put(USERNAME, listUser.username)
            put(NAME, listUser.name)
            put(AVATAR, listUser.avatar)
            put(COMPANY, listUser.company)
            put(LOCATION, listUser.location)
            put(REPOSITORY, listUser.repository)
            put(FOLLOWERS, listUser.followers)
            put(FOLLOWING, listUser.following)
        }

        isFavorite = true
        contentResolver.insert(CONTENT_URI, values)
    }

    private fun removedToFavorite() {
        favoriteHelper.deleteByUsername(listUser.username.toString())
        isFavorite = false
    }

    private fun viewPagerSection() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
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