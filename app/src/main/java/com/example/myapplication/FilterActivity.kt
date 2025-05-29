package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val confirmBtn = findViewById<Button>(R.id.confirmBtn)
        val cityEdit = findViewById<EditText>(R.id.cityEdit)
        confirmBtn.setOnClickListener {
            val city = cityEdit.text.toString().trim()
            val intent = Intent()
            intent.putExtra("city", city)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
} 