package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.messageRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MessageAdapter { msg ->
            Toast.makeText(context, "点击消息: $msg", Toast.LENGTH_SHORT).show()
        }
        return view
    }
} 