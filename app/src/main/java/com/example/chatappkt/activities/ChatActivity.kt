package com.example.chatappkt.activities

import android.os.Bundle
import android.os.Message
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatappkt.R
import com.example.chatappkt.adapters.MessageAdapter
import com.example.chatappkt.databinding.ActivityChatBinding
import com.example.chatappkt.models.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var messageList: ArrayList<MessageModel>
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var dbRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.username_toolbar)
        setSupportActionBar(toolbar)

        messageList = ArrayList<MessageModel>()
        messageAdapter = MessageAdapter(this, messageList)

        dbRef = FirebaseDatabase.getInstance().getReference("Chats")

        val name_extra = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        toolbar.title = name_extra

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // Set Recycler
        binding.messagesRecycler.layoutManager = LinearLayoutManager(this)
        binding.messagesRecycler.adapter = messageAdapter

        // Show chat on recycler
        dbRef.child(senderRoom!!).child("Messages").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children){

                    val message = postSnapshot.getValue(MessageModel::class.java)
                    messageList.add(message!!)

                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.sendImageButton.setOnClickListener {

            val message = binding.editMessageBox.text.toString()
            val messageObject = MessageModel(message, senderUid!!)

            dbRef.child(senderRoom!!).child("Messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child(receiverRoom!!).child("Messages").push()
                        .setValue(messageObject)
                }
            binding.editMessageBox.setText("")
        }

    }
}