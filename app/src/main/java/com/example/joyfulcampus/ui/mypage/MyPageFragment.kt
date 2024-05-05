package com.example.joyfulcampus.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentMypageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        setupSignUpButton()
        setupSignInOutButton()
    }

    // 로그인, 회원가입 진행 중 다른 view 또는 앱을 나갔다 들어온 경우 동작 유지 가능하게 구현
    override fun onStart() {
        super.onStart()

        // 로그아웃 상태 = (Firebase.auth.currentUser == null)
        if (Firebase.auth.currentUser == null) {
            initViewToSignOutState()
        } else { // 로그인 상태
            initViewToSignInState()
        }
    }

    // 회원가입 기능 구현
    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "이메일 또는 패스워드를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener //  해당 클릭 이벤트 처리 블록을 종료하고 함수를 빠져나가는 역할을 합니다.
            }
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(binding.root, "회원가입에 성공했습니다.", Snackbar.LENGTH_SHORT).show()
                        initViewToSignInState()
                    } else {
                        // 해당 액티비티나 프래그먼트의 레이아웃 최상위 뷰(=binding.root)에 Snackbar를 표시하겠다는 의미입니다.
                        Snackbar.make(binding.root, "회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    it.printStackTrace() // 예외를 출력하여 개발자가 예외의 원인을 파악하고 디버깅할 수 있도록 도와줍니다.
                }
        }
    }

    // 로그인 / 로그아웃 기능 구현 => 회원가입된 상태
    private fun setupSignInOutButton() {
        binding.signInOutButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (Firebase.auth.currentUser == null) { // 앱에 로그인하지 않은 상태 => 로그인 한 상태에서 아래 코드를 실행하면 X
                // 로그인
                if (email.isEmpty() || password.isEmpty()) {
                    Snackbar.make(binding.root, "이메일 또는 패스워드를 입력해주세요.", Snackbar.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) { // 로그인 성공한 경우
                            initViewToSignInState() // 로그아웃으로 바꾸기 => why? : 회원가입 버튼 누르면 자동으로 로그인 되어서.
                        } else { // 로그인 실패한 경우
                            Snackbar.make(
                                binding.root,
                                "로그인에 실패했습니다. 이메일 또는 패스워드를 확인해주세요.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else { // 로그인이 된 상태
                // 로그아웃
                Firebase.auth.signOut()
                initViewToSignOutState() // 로그인으로 바꾸기

            }
        }
    }

    // 로그인이 성공한 경우 바뀌어야 하는 기능 구현
    private fun initViewToSignInState() {
        // Firebase Authentication을 사용하여 현재 사용자의 이메일을 가져와서 이메일 입력란에 설정합니다.
        binding.emailEditText.setText(Firebase.auth.currentUser?.email)
        binding.emailEditText.isEnabled = false // 텍스트를 입력할 수 없는 상태(view는 보인다.)
        binding.passwordEditText.isVisible = false // 비밀번호 입력칸 안보이게
        binding.signInOutButton.text = getString(R.string.signOut) // 로그인 버튼이 로그아웃 버튼으로 바뀌게
        binding.signUpButton.isEnabled = false // 회원가입 버튼 안보이게
    }

    // 로그아웃이 성공한 경우 바뀌어야 하는 기능 구현
    private fun initViewToSignOutState() {
        binding.emailEditText.text.clear()
        binding.emailEditText.isEnabled = true
        binding.passwordEditText.text.clear()
        binding.passwordEditText.isVisible = true
        binding.signInOutButton.text = getString(R.string.signIn)
        binding.signUpButton.isEnabled = true
    }
}