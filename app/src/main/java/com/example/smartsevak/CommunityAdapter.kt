package com.example.smartsevak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityAdapter(private val postList: ArrayList<UserPost>): RecyclerView.Adapter<CommunityAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postUserNameTV: TextView = itemView.findViewById(R.id.postUserNameTV)
        val postTextTV: TextView = itemView.findViewById(R.id.postTextTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.postUserNameTV.text = postList[position].UserName
        holder.postTextTV.text = postList[position].PostText
    }
}