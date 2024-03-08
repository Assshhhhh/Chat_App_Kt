package com.example.chatappkt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkt.R
import com.example.chatappkt.databinding.ItemReceivedMessageBinding
import com.example.chatappkt.databinding.ItemSentMessageBinding
import com.example.chatappkt.models.MessageModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context, var messageList: ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            return SentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false))
        }else{
            return ReceiveViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java){
            val currentMessage = messageList[position]
            val viewHolder = holder as SentViewHolder
            holder.binding.tvSentMessage.text = currentMessage.message
        }
        else{
            val currentMessage = messageList[position]
            val viewHolder = holder as ReceiveViewHolder
            holder.binding.tvReceiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }

    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: ItemSentMessageBinding = ItemSentMessageBinding.bind(itemView)
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: ItemReceivedMessageBinding = ItemReceivedMessageBinding.bind(itemView)
    }

}