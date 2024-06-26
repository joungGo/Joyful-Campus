package com.example.joyfulcampus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.joyfulcampus.MainActivity
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Suppress("DEPRECATION")
class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      상태창 색 입히기
        this.window.apply {
            statusBarColor = resources.getColor(R.color.gray_cc,null)
        }

        // 로그인 버튼
        binding.loginButton.setOnClickListener {
            val email = binding.signupemailEditText.text.toString()
            val password = binding.signuppasswordEditText.text.toString()


            if(email.isEmpty()||password.isEmpty()) {
                Snackbar.make(binding.root, "이메일 또는 패스워드가 입력되지 않았습니다.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // email, password 입력
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Snackbar.make(binding.root, "로그인에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Close the app when the back button is pressed
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}
