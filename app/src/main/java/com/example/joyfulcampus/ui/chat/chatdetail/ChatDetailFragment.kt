package com.example.joyfulcampus.ui.chat.chatdetail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.FragmentChatdetailBinding
import com.example.joyfulcampus.ui.chat.userlist.UserItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database


class ChatDetailFragment: Fragment(R.layout.fragment_chatdetail) {

    private lateinit var binding: FragmentChatdetailBinding

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUserName: String = ""

    private val chatItemList = mutableListOf<ChatDetailItem>()

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatdetailBinding.bind(view)

//      fragment 간 정보를 arguments를 통해 데이터를 받아옵니다
        chatRoomId = arguments?.getString(EXTRA_CHAT_ROOM_ID) ?: return
        otherUserId = arguments?.getString(EXTRA_OTHER_USER_ID) ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""

        val chatDetailAdapter = ChatDetailAdapter()

        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get().addOnSuccessListener {
            val myUserItem = it.getValue(UserItem::class.java)
            myUserName = myUserItem?.username ?: ""
        }

        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get().addOnSuccessListener {
            val otherUserItem = it.getValue(UserItem::class.java)
            chatDetailAdapter.otherUserItem = otherUserItem
        }

//      채팅 내용
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatdetailItem = snapshot.getValue(ChatDetailItem::class.java)
                chatdetailItem ?: return

                chatItemList.add(chatdetailItem)
                chatDetailAdapter.submitList(chatItemList.toMutableList())
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })

        binding.chatdetailRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatDetailAdapter
        }

//      전송 버튼
        binding.sendImage.setOnClickListener{
            val message = binding.messageEditText.text.toString()

            if (message.isEmpty()){
                Snackbar.make(binding.root, "빈 메시지를 전송할 수는 없습니다.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newChatItem = ChatDetailItem(
                message = message,
                userId = myUserId
            )

            Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                newChatItem.chatId = key
                setValue(newChatItem)
            }

//          업데이트
            val updates: MutableMap<String, Any> = hashMapOf(
                "${Key. DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${Key. DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${Key. DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                "${Key. DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${Key. DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
            )

            Firebase.database.reference.updateChildren(updates)

            binding.messageEditText.text.clear()
        }
    }

    companion object {
        const val EXTRA_CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val EXTRA_OTHER_USER_ID = "OTHER_USER_ID"
    }
}