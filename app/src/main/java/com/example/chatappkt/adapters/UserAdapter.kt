package com.example.chatappkt.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappkt.R
import com.example.chatappkt.activities.ChatActivity
import com.example.chatappkt.databinding.ItemUsersBinding
import com.example.chatappkt.models.UserModel

class UserAdapter(var context: Context, var arrayList: ArrayList<UserModel>) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var userData = arrayList[position]
        holder.binding.tvUserName.text = userData.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", userData.name)
            intent.putExtra("uid", userData.uid)
            context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemUsersBinding = ItemUsersBinding.bind(itemView)
    }

}