package com.personal.githubuserwithapi.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.personal.githubuserwithapi.R
import com.personal.githubuserwithapi.databinding.ItemRowUserBinding
import com.personal.githubuserwithapi.entity.Favorite

class FavoriteAdaper(private val activity: (Favorite) -> Unit) : RecyclerView.Adapter<FavoriteAdaper.FavoriteViewHolder>() {

    var listFavorite = ArrayList<Favorite>()
    set(listFavorite) {
        if (listFavorite.size > 0) {
            this.listFavorite.clear()
        }
        this.listFavorite.addAll(listFavorite)

        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdaper.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)

        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdaper.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(favorite: Favorite) {
            Glide.with(itemView.context)
                .load(favorite.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imageItemPhoto)
            binding.tvItemUsername.text = favorite.username
            binding.tvItemName.text = favorite.name
        }
    }
}