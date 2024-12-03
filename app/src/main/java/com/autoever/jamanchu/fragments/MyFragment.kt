package com.autoever.jamanchu.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.autoever.jamanchu.R
import com.autoever.jamanchu.activities.FriendActivity
import com.autoever.jamanchu.activities.IntroActivity
import com.autoever.jamanchu.models.User
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

class MyFragment : Fragment() {
    val db: FirebaseFirestore = Firebase.firestore
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_my, container, false)

            val textViewLogout = view.findViewById<TextView>(R.id.textViewLogout)
            textViewLogout.setOnClickListener {
                // Firebase 인증 로그아웃
                FirebaseAuth.getInstance().signOut()
                
                // 인트로 화면으로 이동
                val intent = Intent(requireContext(), IntroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            // 내가 추가한 친구로 이동
            val layoutFriend = view.findViewById<View>(R.id.layoutFriend)
            layoutFriend.setOnClickListener {
                val intent = Intent(requireContext(), FriendActivity::class.java)
                startActivity(intent)
            }
            
            // 프로필 이미지뷰
            val imageViewUser = view.findViewById<ImageView>(R.id.imageViewUser)
            val textViewNickName = view.findViewById<TextView>(R.id.textViewNickName)
            val textViewIntroduce = view.findViewById<TextView>(R.id.textViewIntroduce)
            val textViewGender = view.findViewById<TextView>(R.id.textViewGender)
            val textViewAge = view.findViewById<TextView>(R.id.textViewAge)
            // 내 정보 불러오기
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val currentUserId = currentUser.uid
                getUser(currentUserId) { user ->
                    if (user != null) {
                        // 사용자 정보가 성공적으로 가져와졌을 때 처리
                        println("Email: ${user.email}, Nickname: ${user.nickname}")
                        Glide.with(requireContext())
                            .load(user.image) // 불러올 이미지의 URL 또는 URI
                            .placeholder(R.drawable.user)
                            .into(imageViewUser) // 이미지를 표시할 ImageView
                        textViewNickName.text = user.nickname
                        textViewIntroduce.text = user.introduction
                        textViewGender.text = user.gender.toString()
                        textViewAge.text = user.age.toString()
                    } else {
                        // 사용자 정보를 가져오는 데 실패했을 때 처리
                        println("User not found or error occurred.")
                    }
                }
            }
            return view
        }
    fun getUser(userId: String, callback: (User?) -> Unit) {
        db.collection("users")
            .document(userId) // 사용자 ID로 문서 가져오기
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    callback(user) // 변환한 User 객체를 callback으로 전달
                } else {
                    callback(null) // 해당하는 문서가 없을 경우 null 전달
                }
            }
            .addOnFailureListener { exception ->
                // 에러 처리
                println("Error getting document: $exception")
                callback(null) // 에러 발생 시 null 전달
            }
    }




}