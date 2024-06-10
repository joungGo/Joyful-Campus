package com.example.joyfulcampus.ui.chat.chatdetail

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.Key
import com.example.joyfulcampus.databinding.FragmentChatdetailBinding
import com.example.joyfulcampus.ui.chat.ChatFragment
import com.example.joyfulcampus.ui.chat.userlist.UserItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID


class ChatDetailFragment : Fragment(R.layout.fragment_chatdetail) {

    private lateinit var binding: FragmentChatdetailBinding
    private lateinit var ChatDetailAdapter: ChatDetailAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var chatRoomId: String = ""
    private var otherUserId: String = ""
    private var myUserId: String = ""
    private var myUserName: String = ""
    private var selectedUrl: Uri? = null

    private val chatItemList = mutableListOf<ChatDetailItem>()


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            selectedUrl = uri

            if (selectedUrl != null) {
                val photoUri = selectedUrl ?: return@registerForActivityResult
                uploadImage(
                    uri = photoUri,
                    successHandler = {
                        uploadImagechat(it)
                    },
                    errorHandler = {
                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatdetailBinding.bind(view)

        setupPhotoImage(view)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        ChatDetailAdapter = ChatDetailAdapter()

//      fragment 간 정보를 arguments를 통해 데이터를 받아옵니다
        chatRoomId = arguments?.getString(EXTRA_CHAT_ROOM_ID) ?: return
        otherUserId = arguments?.getString(EXTRA_OTHER_USER_ID) ?: return
        myUserId = Firebase.auth.currentUser?.uid ?: ""



        linearLayoutManager = LinearLayoutManager(getActivity())


        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get().addOnSuccessListener {
            val myUserItem = it.getValue(UserItem::class.java)
            myUserName = myUserItem?.username ?: ""

            getOtherUserData()
        }

        binding.chatdetailRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = ChatDetailAdapter
        }

        Firebase.firestore.collection("chat")
            .get()
            .addOnSuccessListener { result ->
                result.map {
                    it.toObject<ChatDetailItem>()
                }
            }


        ChatDetailAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                linearLayoutManager.smoothScrollToPosition(
                    binding.chatdetailRecyclerView,
                    null,
                    ChatDetailAdapter.itemCount - 1
                )
            }
        })

//      카메라 가능해지면 open
//        binding.cameraImage.setOnClickListener {
//        }
        binding.cameraImage.isVisible = false

//          문서 가능해지면 open
//        binding.clipImage.setOnClickListener {
//        }
        binding.clipImage.isVisible = false

//      전송 버튼
        binding.sendbutton.setOnClickListener{
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
                "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
            )

            Firebase.database.reference.updateChildren(updates)

            binding.messageEditText.text.clear()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .apply{
                    replace(R.id.frameLayout, ChatFragment())
                    commit()
                }
        }

        binding.backbuttonframeyout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .apply{
                    replace(R.id.frameLayout, ChatFragment())
                    commit()
                }
        }

//      sidebar
//        binding.chatdetailsidebarbutton.setOnClickListener {
//        }
//        binding.chatdetailsidebarframeyout.setOnClickListener {
//        }
        binding.chatdetailsidebarbutton.isVisible = false


    }

    private fun getOtherUserData(){
        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener {
                val otherUserItem = it.getValue(UserItem::class.java)
                if (otherUserItem != null) {
                    binding.chatdetailtoolbartitle.text = otherUserItem.username
                }
                ChatDetailAdapter.otherUserItem = otherUserItem

                getChatData()
            }
    }

    private fun getChatData(){
        //      채팅 내용
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatdetailItem = snapshot.getValue(ChatDetailItem::class.java)
                chatdetailItem ?: return

                chatItemList.add(chatdetailItem)
                ChatDetailAdapter.submitList(chatItemList.toMutableList())
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }



    private fun setupPhotoImage(view : View){
        binding.photoImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uploadImage(
        uri: Uri,
        successHandler: (String) -> Unit,
        errorHandler: (Throwable?) -> Unit,
        ){
        val fileName = "${UUID.randomUUID()}.png"
        Firebase.storage.reference.child("chat/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener { task ->
                if ( task.isSuccessful ){
                    Firebase.storage.reference.child("chat/photo/${fileName}")
                        .downloadUrl
                        .addOnSuccessListener {
                            successHandler(it.toString())

                            val newChatItem = ChatDetailItem(
                                imageUrl = it.toString(),
                                userId = myUserId

                            )

                            Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply {
                                newChatItem.chatId = key
                                setValue(newChatItem)
                            }

                        } .addOnFailureListener {
                            errorHandler(it)
                        }
                } else {
                    errorHandler(task.exception)
                }
            }

        val lastimage = "사진"
//          업데이트
        val updates: MutableMap<String, Any> = hashMapOf(
            "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to lastimage,
            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to lastimage,
            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
            "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
        )

        Firebase.database.reference.updateChildren(updates)
    }

    private fun uploadImagechat(photoUri: String){
        val articleId = UUID.randomUUID().toString()
        val ChatDetailItem = ChatDetailItem(
            articleId = articleId,
            createdAt = System.currentTimeMillis(),
            imageUrl = photoUri,
            userId = myUserId
        )

        Firebase.firestore.collection("chat").document(articleId)
            .set(ChatDetailItem)
            .addOnSuccessListener {
                Log.d(TAG, "Firestore 올림")

            }.addOnFailureListener{
            }
    }

//    문서 였던것
    private fun createFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, Companion.CREATE_FILE)
    }

    companion object {
        const val EXTRA_CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val EXTRA_OTHER_USER_ID = "OTHER_USER_ID"
        const val CREATE_FILE = 1
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .apply{
                replace(R.id.chatDetailFragment, fragment)
                commit()
            }
    }
}