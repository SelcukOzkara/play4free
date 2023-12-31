package com.example.play4free.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.play4free.GameViewModel
import com.example.play4free.data.datamodels.Giveaways
import com.example.play4free.databinding.GiveawayItemBinding


class GiveawayAdapter(
    private var viewModel: GameViewModel,
    private var context: Context
) : ListAdapter<Giveaways, GiveawayAdapter.ItemViewHolder>(UtilDiffGiveaway()) {

    inner class ItemViewHolder(private val binding: GiveawayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Giveaways) {
            with(binding) {
                giveawayIV.load(item.image)
                giveawayTitleTV.text = item.title
                giveawayDescTV.text = item.instructions
                getBTN.setOnClickListener {
                    val getNow = Intent(Intent.ACTION_VIEW)
                    getNow.data = Uri.parse(item.open_giveaway_url)
                    startActivity(context, getNow, null)
                }
                giveawayCV.setOnClickListener {
                    giveawayDescTV.toggle()
                }
            }
        }
    }

    private class UtilDiffGiveaway : DiffUtil.ItemCallback<Giveaways>() {
        override fun areItemsTheSame(oldItem: Giveaways, newItem: Giveaways): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Giveaways, newItem: Giveaways): Boolean {
            return (
                    oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.image == newItem.image &&
                    oldItem.open_giveaway_url == newItem.open_giveaway_url &&
                    oldItem.instructions == newItem.instructions
                    )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            GiveawayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

}