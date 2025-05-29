package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val onClick: (String) -> Unit) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val data = listOf(
        "甜甜：宝贝（3分钟前）",
        "知聊团队：你还记得我吗？（4小时前）",
        "谁来看过我：有个小姐姐反复看了你（6天前）",
        "提醒：您的道具即将失效（6天前）",
        "春天：嗨，在干嘛呢，好无聊啊（05-19）"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position]
        holder.itemView.setOnClickListener { onClick(data[position]) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }
} 