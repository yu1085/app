package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.util.Log

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val queue: RequestQueue = Volley.newRequestQueue(this)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val url = "http://10.0.2.2:8080/api/login"
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)
            Log.d(TAG, "Sending login request: $json")
            val request = JsonObjectRequest(Request.Method.POST, url, json,
                Response.Listener { response ->
                    Log.d(TAG, "Received response: $response")
                    val code = response.optInt("code")
                    val message = response.optString("message")
                    if (code == 200) {
                        val data = response.optJSONObject("data")
                        val name = data?.optString("name") ?: ""
                        val email = data?.optString("email") ?: ""
                        Toast.makeText(this, "欢迎 $name\n邮箱: $email", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.e(TAG, "Login failed: code=$code, message=$message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "Network error", error)
                    Toast.makeText(this, "网络错误: ${error.message}", Toast.LENGTH_SHORT).show()
                })
            queue.add(request)
        }

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            Toast.makeText(this, "一键登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
} 