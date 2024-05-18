package com.example.joyfulcampus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.joyfulcampus.data.Key.Companion.DB_URL
import com.example.joyfulcampus.data.Key.Companion.DB_USERS
import com.example.joyfulcampus.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            val username = binding.signupnameEditText.text.toString()
            val email = binding.signupemailEditText.text.toString()
            val password = binding.signuppasswordEditText.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "빈칸이 존재합니다 제대로 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val userId = Firebase.auth.currentUser?.uid

                        if (userId != null) {
                            val user = mutableMapOf<String, Any>()
                            user["userId"] = userId
                            user["username"] = username
                            user["useremail"] = email


                            Firebase.database(DB_URL).reference.child(DB_USERS).child(userId).updateChildren(user)
                        }

                        Snackbar.make(binding.root, "회원가입에 성공했습니다. ", Snackbar.LENGTH_SHORT).show()
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()


                    } else {
                        Snackbar.make(binding.root, "회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()

                    }


                }
        }
    }

}