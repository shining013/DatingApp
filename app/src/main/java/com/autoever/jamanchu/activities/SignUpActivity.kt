package com.autoever.jamanchu.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.autoever.jamanchu.R
import com.autoever.jamanchu.models.Gender
import com.autoever.jamanchu.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextNickname = findViewById<EditText>(R.id.editTextNickname)
        val editTextIntroduction = findViewById<EditText>(R.id.editTextIntroduction)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val spinner: Spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.age_array,
            android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val textViewComplete = findViewById<TextView>(R.id.textViewComplete)
        textViewComplete.setOnClickListener {
            val selectedGenderId = radioGroup.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedGenderId)
            val gender = when (selectedRadioButton.text.toString()) {
                "남자" -> Gender.MALE
                "여자" -> Gender.FEMALE
                else -> throw IllegalArgumentException("올바른 성별을 선택하시오")
            }
            val selectedAge = spinner.selectedItem.toString().toInt()

            val user = User(
                "",
                editTextEmail.text.toString(),
                editTextNickname.text.toString(),
                editTextIntroduction.text.toString(),
                gender,
                selectedAge
            )
            Log.d("User", user.toString())

            signUp(user, editTextPassword.text.toString())
        }
    }
    
    // 회원가입 메서드
    fun signUp(user: User, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener(this) {task ->
            if (task.isSuccessful) {
                // 회원가입 성공
               val userId = auth.currentUser?.uid
                if (userId != null) {
                    user.id = userId
                    saveUserData(user)
                }
            } else {
                // 에러 처리
                Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
            }}
    }

    private fun saveUserData(user: User) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.d("SignUpActivity","User data successfully written!")

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{ e->
                Log.e("SignUpActivity","Error writing document", e)
            }
    }
}