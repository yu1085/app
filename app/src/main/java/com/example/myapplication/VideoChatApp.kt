package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class VideoChatApp : AppCompatActivity() {
    private val CAMERA_PERMISSION_REQUEST = 100
    private val AUDIO_PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_chat)

        // 请求相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        }

        // 请求麦克风权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), AUDIO_PERMISSION_REQUEST)
        }

        // 视频速配按钮
        val videoMatchButton = findViewById<Button>(R.id.videoMatchButton)
        videoMatchButton.setOnClickListener {
            matchUser("视频")
        }

        // 语音速配按钮
        val voiceMatchButton = findViewById<Button>(R.id.voiceMatchButton)
        voiceMatchButton.setOnClickListener {
            matchUser("语音")
        }

        // 活跃用户展示
        val activeUsersText = findViewById<TextView>(R.id.activeUsersText)
        activeUsersText.text = "当前活跃用户：10人"

        // 筛选功能按钮
        val filterButton = findViewById<Button>(R.id.filterButton)
        filterButton.setOnClickListener {
            Toast.makeText(this, "筛选功能即将上线", Toast.LENGTH_SHORT).show()
        }

        // 用户个人资料页面按钮
        val profileButton = findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            Toast.makeText(this, "用户个人资料页面即将上线", Toast.LENGTH_SHORT).show()
        }

        // 消息列表按钮
        val messageButton = findViewById<Button>(R.id.messageButton)
        messageButton.setOnClickListener {
            Toast.makeText(this, "消息列表功能即将上线", Toast.LENGTH_SHORT).show()
        }
    }

    private fun matchUser(type: String) {
        // 模拟匹配逻辑
        val matchedUser = "用户${(1..10).random()}"
        Toast.makeText(this, "已匹配到$matchedUser，准备开始${type}聊天", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "相机权限已授予", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "相机权限被拒绝", Toast.LENGTH_SHORT).show()
                }
            }
            AUDIO_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "麦克风权限已授予", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "麦克风权限被拒绝", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
} 