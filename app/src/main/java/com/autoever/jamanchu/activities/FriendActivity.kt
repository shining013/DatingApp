package com.autoever.jamanchu.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.jamanchu.R

class FriendActivity : AppCompatActivity() {
    lateinit var adapter: FriendAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friend)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val friends = listOf("내친구", "친친", "소울메이트")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = FriendAdapter(friends)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

class FriendAdapter(
    private var friends: List<String>
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView)
        val button = view.findViewById<Button>(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.textView.text = friends[position]
    }
}