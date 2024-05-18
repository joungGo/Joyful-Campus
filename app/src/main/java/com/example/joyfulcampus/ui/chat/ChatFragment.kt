package com.example.joyfulcampus.ui.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentChatBinding
import com.example.joyfulcampus.ui.chat.chatlist.ChatListFragment
import com.example.joyfulcampus.ui.chat.userlist.UserFragment

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private lateinit var binding: FragmentChatBinding
    private val userFragment = UserFragment()
    private val chatFragment = ChatListFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        val toolbarBodyTemplate = view.findViewById<Toolbar>(R.id.chatToolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBodyTemplate)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈 버튼 활성화
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_apps_24)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBodyTemplate.title = "편안한 캠퍼스"


        binding.chatnavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.userList -> {
                    replaceFragment(userFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.chatroomList -> {
                    replaceFragment(chatFragment)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }

        }
        replaceFragment(userFragment)

    }


    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .apply{
                replace(R.id.frameLayout, fragment)
                commit()
            }

    }
}