package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActiveUserAdapter(val onClick: (User) -> Unit) : RecyclerView.Adapter<ActiveUserAdapter.ViewHolder>() {
    var filterCity: String? = null
    private val data = listOf(
        User("小优", "北京", 150, true),
        User("宁小雪", "厦门", 200, true),
        User("甜甜", "苏州", 300, true),
        User("玫瑰", "成都", 250, false)
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_active_user, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getFilteredData()[position]
        holder.nickname.text = user.nickname
        holder.city.text = user.city
        holder.price.text = "${user.price}/分钟"
        holder.status.setImageResource(if (user.online) R.drawable.ic_online else R.drawable.ic_offline)
        holder.itemView.setOnClickListener { onClick(user) }
    }
    override fun getItemCount() = getFilteredData().size
    private fun getFilteredData(): List<User> {
        return if (filterCity.isNullOrEmpty()) data else data.filter { it.city == filterCity }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.nickname)
        val city: TextView = view.findViewById(R.id.city)
        val price: TextView = view.findViewById(R.id.price)
        val status: ImageView = view.findViewById(R.id.status)
    }
}

data class User(val nickname: String, val city: String, val price: Int, val online: Boolean) 