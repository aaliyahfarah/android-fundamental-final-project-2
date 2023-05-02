package com.dicoding.usergithubapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.usergithubapp.databinding.ItemListUserBinding
import com.dicoding.usergithubapp.util.Constanta.EXTRA_USER
import com.dicoding.usergithubapp.model.User
import com.dicoding.usergithubapp.ui.detail.DetailActivity

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val listUser = ArrayList<User>()

    fun setAllData(data: List<User>) {
        listUser.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class UserViewHolder(private val view: ItemListUserBinding): RecyclerView.ViewHolder(view.root) {

        fun bind(user: User) {

            view.apply {
                tvUsername.text = user.username
            }

            Glide.with(itemView.context)
                .load(user.avatar)
                .into(view.ivImgUser)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USER, user.username)

                itemView.context.startActivity(intent)
            }
        }
    }
}