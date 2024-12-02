package com.autoever.jamanchu.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.autoever.jamanchu.R
import com.autoever.jamanchu.api.RetrofitInstance
import com.autoever.jamanchu.models.Line
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LineActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_line)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editText = findViewById<EditText>(R.id.editText)
        val textViewComplete = findViewById<TextView>(R.id.textViewComplete)
        textViewComplete.setOnClickListener {
            val content = editText.text.toString()
            addLine(content)
        }
    }
    fun addLine(text: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val newLine = Line(id = "", user = userId, line = text)
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.createLine(newLine)
                if (response.isSuccessful && response.body() != null) {
                    setResult(RESULT_OK)
                    finish() // 액티비티 종료
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}