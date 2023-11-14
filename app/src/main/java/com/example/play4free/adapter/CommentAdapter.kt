package com.example.play4free.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.data.datamodels.Comments
import com.example.play4free.data.datamodels.Profile
import com.example.play4free.databinding.CommentItemBinding

class CommentAdapter(
    val dataset: List<Comments>,
    val context: Context,
    val viewmodel: GameViewModel
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

        viewmodel.firestore.collection("Profile").document(item.uid!!).get()
            .addOnSuccessListener {
                val profile = it.toObject(Profile::class.java)
                holder.binding.commentTV.text = item.content
                holder.binding.commentNameTV.text =  profile?.username
                Log.d("BildURL", profile?.pb.toString())
                holder.binding.commenPbIV.load(profile?.pb){
                    crossfade(true)
                    placeholder(R.drawable.baseline_account_box_24)
                    error(R.drawable.baseline_error_outline_24)
                    transformations(RoundedCornersTransformation(40f))
                }
            }

    }
}