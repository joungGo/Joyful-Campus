package com.example.joyfulcampus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      상태창 색 입히기
        this.window.apply {
            statusBarColor = resources.getColor(R.color.gray_cc, null)
        }


// Sign up button
        binding.signupButton1.setOnClickListener {
            val username = binding.signupnameEditText.text.toString()
            val email = binding.signupemailEditText.text.toString()
            val password = binding.signuppasswordEditText.text.toString()

            // Check name, email, password
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "공백이 있습니다. 정확하게 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val currentUser = Firebase.auth.currentUser
                            currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    Snackbar.make(binding.root, "확인 링크가 이메일로 전송되었습니다. 이메일을 확인해 주세요.",Snackbar.LENGTH_SHORT).show()
                                    binding.signupButton1.isVisible = false
                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        "Failed to send verification email.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // Check email verification status after some time or prompt user to check email
                            binding.signupButton2.setOnClickListener {
                                if (currentUser != null) {
                                    currentUser.reload().addOnCompleteListener {
                                        if (currentUser.isEmailVerified) {
                                            val userId = currentUser.uid
                                            Firebase.messaging.token.addOnCompleteListener { tokenTask ->
                                                val token = tokenTask.result
                                                // Save to Firebase
                                                val user = mutableMapOf<String, Any>()
                                                user["userId"] = userId
                                                user["username"] = username
                                                user["useremail"] = email
                                                user["fcmToken"] = token
                                                user["userprofileurl"] = ""

                                                Firebase.database(DB_URL).reference.child(DB_USERS)
                                                    .child(userId).updateChildren(user)

                                                Snackbar.make(
                                                    binding.root,
                                                    "성공적으로 등록되었습니다.",
                                                    Snackbar.LENGTH_SHORT
                                                ).show()

                                                val intent = Intent(this, AuthActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                                startActivity(intent)
                                                finish()
                                            }
                                        } else {
                                            Snackbar.make(
                                                binding.root,
                                                "가입하려면 이메일을 인증해야 합니다.",
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        } else {
                            Snackbar.make(binding.root, "가입 실패.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Close the app when the back button is pressed
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}