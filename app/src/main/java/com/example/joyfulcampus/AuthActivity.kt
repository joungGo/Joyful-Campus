package com.example.joyfulcampus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val currentUser = Firebase.auth.currentUser

        binding.authloginoutbutton.setOnClickListener {
            if (currentUser == null) { //로그아웃
                initviewToSignOutState()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else { //로그인
                Firebase.auth.signOut()
                initviewToSignOutState()
            }
        }

        binding.authstartbutton.setOnClickListener {
            if (currentUser == null) {
                Snackbar.make(binding.root, "로그인 해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.authsignupbutton.setOnClickListener {
            if (currentUser == null) {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    //로그인 상태
    private fun initviewToSignInState() {
        binding.authloginoutbutton.text = getString(R.string.Logout)
        binding.authsignupbutton.isEnabled = false
    }

    //로그아웃 상태
    private fun initviewToSignOutState() {
        binding.authloginoutbutton.text = getString(R.string.Login)
        binding.authsignupbutton.isEnabled = true

    }
}