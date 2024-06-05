package com.example.joyfulcampus.ui.chat.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.FragmentChatlistBinding
import com.example.joyfulcampus.ui.chat.chatdetail.ChatDetailFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdapter{ chatRoomItem ->
//          fragment 간 이동
            val bundle = Bundle()
            bundle.putString(ChatDetailFragment.EXTRA_OTHER_USER_ID, chatRoomItem.otherUserId)
            bundle.putString(ChatDetailFragment.EXTRA_CHAT_ROOM_ID, chatRoomItem.chatRoomId)

            val chatdetailFragment = ChatDetailFragment()

            chatdetailFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .apply{
                    replace(R.id.frameLayout, chatdetailFragment)
                    addToBackStack(null)
                    commit()
                }
        }
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }


        val currentUserid = Firebase.auth.currentUser?.uid ?: return
        val chatRoomsDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(currentUserid)

        chatRoomsDB.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = snapshot.children.map {
                    it.getValue(ChatRoomItem::class.java)
                }
                chatListAdapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}