package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private var filterCity: String? = null
    private lateinit var adapter: ActiveUserAdapter
    private val REQUEST_FILTER = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val matchBtn = view.findViewById<Button>(R.id.matchBtn)
        val filterBtn = view.findViewById<Button>(R.id.filterBtn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.activeUserRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ActiveUserAdapter { user ->
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("nickname", user.nickname)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        matchBtn.setOnClickListener {
            Toast.makeText(context, "速配功能开发中", Toast.LENGTH_SHORT).show()
        }
        filterBtn.setOnClickListener {
            startActivityForResult(Intent(context, FilterActivity::class.java), REQUEST_FILTER)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            filterCity = data?.getStringExtra("city")
            // 模拟根据筛选条件刷新活跃用户
            adapter.filterCity = filterCity
            adapter.notifyDataSetChanged()
        }
    }
} 