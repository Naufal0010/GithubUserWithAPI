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
import com.personal.githubuserwithapi.model.User

class ListDataUserAdapter(private val listData : ArrayList<User>) : RecyclerView.Adapter<ListDataUserAdapter.ListViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataUserAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListDataUserAdapter.ListViewHolder, position: Int) {
        holder.bind(listData[position])

        val data = listData[position]
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

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(data: User) {
            Glide.with(itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imageItemPhoto)
            binding.tvItemName.text = data.username
            binding.tvItemCompany.text = data.company
        }
    }

}