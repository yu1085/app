package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nickname = intent.getStringExtra("nickname") ?: "昵称"
        val nicknameView = findViewById<TextView>(R.id.nickname)
        nicknameView.text = nickname

        val videoBtn = findViewById<Button>(R.id.videoBtn)
        val messageBtn = findViewById<Button>(R.id.messageBtn)
        val likeBtn = findViewById<Button>(R.id.likeBtn)
        videoBtn.setOnClickListener {
            Toast.makeText(this, "发起视频聊天", Toast.LENGTH_SHORT).show()
        }
        messageBtn.setOnClickListener {
            Toast.makeText(this, "私信Ta", Toast.LENGTH_SHORT).show()
        }
        likeBtn.setOnClickListener {
            Toast.makeText(this, "已喜欢", Toast.LENGTH_SHORT).show()
        }
    }
} 