package com.example.joyfulcampus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.joyfulcampus.MainActivity
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.ActivityAuthBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onStart() {
        super.onStart()

        if(Firebase.auth.currentUser == null){
            initviewToSignOutState()
        } else{
            initviewToSignInState()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var currentUser = Firebase.auth.currentUser

//      로그아웃 버튼
        binding.authloginoutbutton.setOnClickListener {
            if (currentUser == null) { //로그아웃
                initviewToSignOutState()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else { //로그인
                Firebase.auth.signOut()
                initviewToSignOutState()
                currentUser = null
            }
        }

        // 시작 버튼
        binding.authstartbutton.setOnClickListener {
            if (currentUser == null) { // 로그인 상태가 아닐때
                Snackbar.make(binding.root, "로그인 해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else { // 로그인 상태일 때
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 회원가입 버튼
        binding.authsignupbutton.setOnClickListener {
            if (currentUser == null) {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Close the app when the back button is pressed
        finishAffinity()
    }

    //로그인 상태일 때 변경될 화면
    private fun initviewToSignInState() {
        binding.authloginoutbutton.text = getString(R.string.Logout)
        binding.authsignupbutton.isEnabled = false
    }

    //로그아웃 상태일 때 변경될 화면
    private fun initviewToSignOutState() {
        binding.authloginoutbutton.text = getString(R.string.Login)
        binding.authsignupbutton.isEnabled = true

    }
}