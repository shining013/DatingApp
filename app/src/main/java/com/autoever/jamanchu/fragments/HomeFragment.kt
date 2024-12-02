package com.autoever.jamanchu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.jamanchu.R
import com.autoever.jamanchu.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {
    val users:MutableList<User> = mutableListOf()
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val layoutManager = GridLayoutManager(requireContext(), 2) // 열 개수 설정
        recyclerView.layoutManager = layoutManager

        // 데이터 설정
        adapter = MyAdapter(users)
        recyclerView.adapter = adapter

        fetchUsers()

        return view
    }
    fun fetchUsers() {
        users.clear()
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val currentUserId = currentUser!!.uid

        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java).copy(id = document.id)
                    println("User: ${user.nickname}, Email: ${user.email}, Gender: ${user.gender}, Age:${user.age}")

                    if (currentUserId != user.id) {
                        users.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                println("Error getting documents:$exception")
            }
    }
}

class MyAdapter(private val users: List<User>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNick: TextView = itemView.findViewById(R.id.textViewNick)
        val textViewIntroduce: TextView = itemView.findViewById(R.id.textViewIntroduce)
        val textViewFriend: TextView = itemView.findViewById(R.id.textViewFriend)
        val textViewChat: TextView = itemView.findViewById(R.id.textViewChat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false) // 아이템 레이아웃 추가
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.textViewNick.text = user.nickname
        holder.textViewIntroduce.text = user.introduction
    }

    override fun getItemCount() = users.size
}