package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val recyclerView = findViewById<RecyclerView>(R.id.messageRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessageAdapter { message ->
            Toast.makeText(this, "点击了: $message", Toast.LENGTH_SHORT).show()
        }
    }
} 