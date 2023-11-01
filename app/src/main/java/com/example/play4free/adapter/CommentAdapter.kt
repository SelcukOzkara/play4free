package com.example.play4free.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.play4free.data.datamodels.Comments
import com.example.play4free.databinding.CommentItemBinding
import com.example.play4free.databinding.FragmentDetailBinding

class CommentAdapter(
    val dataset: List<Comments>,
    var currentUserId: String?,
    val context: Context
) : RecyclerView.Adapter<CommentAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: CommentItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = dataset[position]

        currentUserId = item.uid
        holder.binding.commentTV.text = item.content
        holder.binding.commentNameTV.text = item.uid
        holder.binding.commenPbIV.load(item.pb)


    }
}