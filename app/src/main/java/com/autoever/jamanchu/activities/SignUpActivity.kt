package com.autoever.jamanchu.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.autoever.jamanchu.R
import com.autoever.jamanchu.models.Gender
import com.autoever.jamanchu.models.User

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
                else -> throw IllegalArgumentException("올바른 성별을 선택")
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
        }
    }
}