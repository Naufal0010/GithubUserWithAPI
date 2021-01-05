package com.personal.githubuserwithapi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.personal.githubuserwithapi.DetailActivity
import com.personal.githubuserwithapi.R
import com.personal.githubuserwithapi.databinding.ItemRowUserBinding
import com.personal.githubuserwithapi.model.Following
import com.personal.githubuserwithapi.model.User

class ListFollowingUserAdapter(private val listFollowing : ArrayList<Following>) : RecyclerView.Adapter<ListFollowingUserAdapter.ViewHolder>() {

    fun setDataFollowing(items : ArrayList<Following>) {
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFollowingUserAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFollowingUserAdapter.ViewHolder, position: Int) {
        holder.bind(listFollowing[position])

        val data = listFollowing[position]
        holder.itemView.setOnClickListener {
            val userData = User (data.username,
                data.name,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following
            )

            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_USER, userData)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listFollowing.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(data: Following) {
            Glide.with(itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imageItemPhoto)
            binding.tvItemUsername.text = data.username
            binding.tvItemName.text = data.name
        }
    }
}