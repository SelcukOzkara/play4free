package com.example.play4free.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.data.datamodels.Games
import com.example.play4free.databinding.FavItemBinding
import com.example.play4free.ui.DashboardFragmentDirections

class FavAdapter(
    private var viewModel: GameViewModel
) : ListAdapter<Games, FavAdapter.ItemViewHolder>(UtilDiffDigimon()) {

    inner class ItemViewHolder(private val binding: FavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Games) {
            with(binding) {
                favImgIV.load(item.thumbnail)
                favTitleTV.text = item.title
                if (viewModel.user.value != null) {
                    favLikeBTN.visibility = View.VISIBLE
                    if (item.isLiked) favLikeBTN.setImageResource(R.drawable.baseline_thumb_up_24)
                    else favLikeBTN.setImageResource(R.drawable.unlike)

                    favLikeBTN.setOnClickListener {
                        if (item.isLiked) {
                            viewModel.removeLikedItem(item.id)
                        } else viewModel.addLikedItem(item.id)

                        viewModel.updateFav(!item.isLiked, item.id)
                    }
                } else favLikeBTN.visibility = View.INVISIBLE


                favCV.setOnClickListener {
                    val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left).setPopEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left).build()

                    it.findNavController().navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToDetailFragment(
                            item.id,
                        ), navOptions
                    )
                }
            }
        }
    }

    private class UtilDiffDigimon : DiffUtil.ItemCallback<Games>() {
        override fun areItemsTheSame(oldItem: Games, newItem: Games): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Games, newItem: Games): Boolean {
            return (oldItem.id == newItem.id &&
                    oldItem.game_url == newItem.game_url &&
                    oldItem.developer == newItem.developer &&
                    oldItem.genre == newItem.genre &&
                    oldItem.platform == newItem.platform &&
                    oldItem.publisher == newItem.publisher &&
                    oldItem.release_date == newItem.release_date &&
                    oldItem.short_description == newItem.short_description &&
                    oldItem.thumbnail == newItem.thumbnail &&
                    oldItem.title == newItem.title &&
                    oldItem.isLiked == newItem.isLiked)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FavItemBinding.inflate(
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