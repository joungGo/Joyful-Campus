package com.example.joyfulcampus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.joyfulcampus.MainActivity
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key.Companion.DB_URL
import com.example.joyfulcampus.data.Key.Companion.DB_USERS
import com.example.joyfulcampus.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.messaging.messaging

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //      상태창 색 입히기
        this.window.apply {
            statusBarColor = resources.getColor(R.color.gray_cc,null)
        }


//      회원가입 버튼
        binding.signupButton.setOnClickListener {
            val username = binding.signupnameEditText.text.toString()
            val email = binding.signupemailEditText.text.toString()
            val password = binding.signuppasswordEditText.text.toString()

//          이름, email, password 확인
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "빈칸이 존재합니다 제대로 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    val currentUser = Firebase.auth.currentUser
                    if (task.isSuccessful && currentUser != null) {

                        val userId = currentUser.uid

                        Firebase.messaging.token.addOnCompleteListener{
                            val token = it.result

//                          Firebase에 저장
                            val user = mutableMapOf<String, Any>()
                            user["userId"] = userId
                            user["username"] = username
                            user["useremail"] = email
                            user["fcmToken"] = token

                            Firebase.database(DB_URL).reference.child(DB_USERS).child(userId).updateChildren(user)

                            Snackbar.make(binding.root, "회원가입에 성공했습니다. ", Snackbar.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent)
                            finish()
                        } // 예외 처릭 가능 .addOnFailureListener()
                    } else {
                        Snackbar.make(binding.root, "회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Close the app when the back button is pressed
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent)
        finish()
    }
}