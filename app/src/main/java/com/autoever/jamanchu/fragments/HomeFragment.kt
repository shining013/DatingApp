package com.autoever.jamanchu.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.jamanchu.R
import com.autoever.jamanchu.activities.ChatActivity
import com.autoever.jamanchu.models.User
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
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
        Glide.with(holder.itemView.context)
            .load(user.image)
            .placeholder(R.drawable.user)
            .into(holder.imageView)

        // 친구 추가 동작
        holder.textViewFriend.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val currentUserId = currentUser!!.uid
            addFriend(holder.itemView.context, currentUserId, user.id) // context : 사용자가 위젯에 있는지 등에 관한 정보
        }

        // 채팅하기
        holder.textViewChat.setOnClickListener {
            val context = holder.itemView.context // Context 가져오기
            val intent = Intent(context, ChatActivity::class.java)
            // 채팅방 ID 전달
            intent.putExtra("otherUser", user.id)
            context.startActivity(intent) // Context를 사용해 startActivity 호출
        }
    }

    override fun getItemCount() = users.size

    fun addFriend(context: Context, currentUserId: String, friendId: String) {
        val userRef = FirebaseFirestore.getInstance()
            .collection("users").document(currentUserId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            val currentFriends = documentSnapshot.get("friends")
                as? List<String> ?: emptyList()
            
            // 친구 ID가 존재하지 않는 경우에만 추가
            if (!currentFriends.contains(friendId)) {
                val updatedFriends = currentFriends + friendId

                userRef.update("friends", updatedFriends)
                    .addOnSuccessListener {
                        Log.d("Firestore","Friend added successfully")

                        Toast.makeText(context, "친구가 추가되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{ e->
                        Log.w("Firestore","Error adding friend", e)
                    }
            } else {
                Toast.makeText(context,"이미 친구로 추가된 사용자입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}