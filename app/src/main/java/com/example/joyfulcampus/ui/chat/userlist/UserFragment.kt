package com.example.joyfulcampus.ui.chat.userlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key.Companion.DB_CHAT_ROOMS
import com.example.joyfulcampus.data.Key.Companion.DB_USERS
import com.example.joyfulcampus.databinding.FragmentUserlistBinding
import com.example.joyfulcampus.ui.chat.chatdetail.ChatDetailFragment
import com.example.joyfulcampus.ui.chat.chatlist.ChatRoomItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.UUID

class UserFragment : Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val currentUser = Firebase.auth.currentUser

        val userlistAdapter = UserAdapter { otherUser ->
            val myUserId = Firebase.auth.currentUser?.uid ?: ""
            val chatRoomDB = Firebase.database.reference.child(DB_CHAT_ROOMS).child(myUserId)
                .child(otherUser.userId ?: "")

//              데이터 가져오기
            chatRoomDB.get().addOnSuccessListener {

                var chatRoomId = ""
                if (it.value != null) {
                    // 데이터가 존재
                    val chatRoom = it.getValue(ChatRoomItem::class.java)
                    chatRoomId = chatRoom?.chatRoomId ?: ""

                } else {
                    // 데이터없으니 틀 생성
                    chatRoomId = UUID.randomUUID().toString()
                    val newChatRoom = ChatRoomItem(
                        chatRoomId = chatRoomId,
                        otherUserName = otherUser.username,
                        otherUserId = otherUser.userId
                    )
                    chatRoomDB.setValue(newChatRoom)
                }

                val chatdetailFragment = ChatDetailFragment()

//                  fragment 간 정보 및 fragment 이동
                val bundle = Bundle()
                bundle.putString(ChatDetailFragment.EXTRA_OTHER_USER_ID, otherUser.userId)
                bundle.putString(ChatDetailFragment.EXTRA_CHAT_ROOM_ID, chatRoomId)

                chatdetailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.frameLayout, chatdetailFragment)
                        addToBackStack(null)
                        commit()
                    }
            }
        }
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userlistAdapter
        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userItemList = mutableListOf<UserItem>()

                    snapshot.children.forEach {
                        val user = it.getValue(UserItem::class.java)
                        user ?: return

                        if (user.userId != currentUserId) {
                            userItemList.add(user)
                        }
                    }
                    userlistAdapter.submitList(userItemList)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}