package com.example.joyfulcampus.ui.mypage

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.FragmentMypageHomeBinding
import com.example.joyfulcampus.ui.auth.AuthActivity
import com.example.joyfulcampus.ui.chat.chatdetail.ChatDetailItem
import com.example.joyfulcampus.ui.chat.userlist.UserItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.google.rpc.context.AttributeContext.Auth
import java.util.UUID

class MyPageFragment: Fragment(R.layout.fragment_mypage_home) {
    private lateinit var binding: FragmentMypageHomeBinding

    private var selectedUrl: Uri? = null
    private var myUserId: String = ""
    private var username: String = ""



    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            selectedUrl = uri
            if (selectedUrl != null) {
                val userprofileurl = selectedUrl ?: return@registerForActivityResult
                uploadImage(
                    uri = userprofileurl,
                    successHandler = {
                        Log.d(TAG, "사진 가져옴")
                        uploadImageprofile(it)
                    },
                    errorHandler = {
                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageHomeBinding.bind(view)

        mypagetoolbar()

//      Firebase realtime database 가져올 위치 설정
        myUserId = Firebase.auth.currentUser?.uid ?: ""
        val userDB = Firebase.database.reference.child(Key.DB_USERS).child(myUserId)

//      이름 가져오기
        userDB.child("username").get().addOnSuccessListener {
            username = it.getValue(String::class.java).toString()
            binding.usernametext.text = username ?: "No username found"
        }

//      학과 가져오기


//      아이디(이메일) 가져오기
        userDB.child("useremail").get().addOnSuccessListener {
            val useremail = it.getValue(String::class.java)
            binding.useridtextview.text = useremail ?: "No userid found"
        }



//      프로필 변경하기
        binding.profileeditbotton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

//      프로필 가져오기
        userDB.child("userprofileurl").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userprofileurl = snapshot.getValue(String::class.java)
                if (userprofileurl == "") {

                } else {
                    Glide.with(binding.profileimage).load(userprofileurl).into(binding.profileimage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }
        })

//      비밀번호 변경
        binding.passwordframelayout.setOnClickListener {
            userDB.child("useremail").get().addOnSuccessListener {
                val useremail = it.getValue(String::class.java)
                if (useremail != null) {
                    Firebase.auth.sendPasswordResetEmail(useremail)
                        .addOnCompleteListener {task ->
                            if(task.isSuccessful){
                                Snackbar.make(binding.root, "이메일로 승인 요청했습니다. \n이메일에서 비밀번호를 변경해주세요", Snackbar.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }


//      로그아웃
        binding.logoutframelayout.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(activity, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadImage(
        uri: Uri,
        successHandler: (String) -> Unit,
        errorHandler: (Throwable?) -> Unit,
    ){
        val fileName = "${UUID.randomUUID()}.png"
        Firebase.storage.reference.child("mypage/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener { task ->
                if ( task.isSuccessful ){
                    Firebase.storage.reference.child("mypage/photo/${fileName}")
                        .downloadUrl
                        .addOnSuccessListener {
                            successHandler(it.toString())

                            val user = mutableMapOf<String, Any>()
                            user["userprofileurl"] = it.toString()

                            Firebase.database(Key.DB_URL).reference.child(Key.DB_USERS).child(myUserId).updateChildren(user)

                        } .addOnFailureListener {
                            errorHandler(it)
                        }
                } else {
                    errorHandler(task.exception)
                }
            }
    }

    private fun uploadImageprofile(userprofileurl: String){
        val UserItem = UserItem(
            userprofileurl = userprofileurl,
            userId = myUserId,
            username = username,
        )

        Firebase.firestore.collection("mypage").document(myUserId)
            .set(UserItem)
            .addOnSuccessListener {
                Log.d(TAG, "Firestore 올림")
            }.addOnFailureListener{
            }
    }

    private fun mypagetoolbar() {
        // Toolbar 설정
        val toolbarBodyTemplate = view?.findViewById<Toolbar>(R.id.mypagetoolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBodyTemplate)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈 버튼 활성화
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.list)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        // 텍스트 컬러를 다르게 적용할 문자열 생성
        val title = getString(R.string.편안한_Campus)
        val spannableTitle = SpannableStringBuilder(title)

        // "편안한" 부분을 굵게 처리 및 색상 적용
        val boldStyleSpan = StyleSpan(Typeface.BOLD)
        val redColor = ContextCompat.getColor(requireContext(), R.color.편안한)
        spannableTitle.setSpan(boldStyleSpan, 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableTitle.setSpan(ForegroundColorSpan(redColor), 0, 3, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // "캠퍼스" 부분에 색상 적용
        val blueColor = ContextCompat.getColor(requireContext(), R.color.black)
        spannableTitle.setSpan(ForegroundColorSpan(blueColor), 4, title.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // SpannableStringBuilder을 툴바 타이틀로 설정
        toolbarBodyTemplate?.title = spannableTitle
    }
}