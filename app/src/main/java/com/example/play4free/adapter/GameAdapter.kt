package com.example.play4free.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.data.datamodels.Games
import com.example.play4free.databinding.GameListItemBinding
import com.example.play4free.ui.HomeFragmentDirections

class GameAdapter(
    private var viewModel : GameViewModel
): ListAdapter<Games, GameAdapter.ItemViewHolder>(UtilDiffDigimon()) {

    inner class ItemViewHolder(val binding: GameListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Games){
            with(binding){
                homeThumbIV.load(item.thumbnail)
                homeGameTitleTV.text = item.title
                homeShortDescTV.text = item.short_description
                homeGenreTV.text = item.genre
                homePlatformTV.text = item.platform

                if (viewModel.user.value != null ){
                    homeLikeBTN.visibility = View.VISIBLE
                if (item.isLiked) homeLikeBTN.setImageResource(R.drawable.baseline_thumb_up_24)
                else homeLikeBTN.setImageResource(R.drawable.unlike)

                homeLikeBTN.setOnClickListener {
                        if (!item.isLiked){
                            viewModel.addLikedItem(item.id)
                        }else viewModel.removeLikedItem(item.id)

                    viewModel.updateFav(!item.isLiked, item.id)
                }
                } else  homeLikeBTN.visibility = View.INVISIBLE

                homeCV.setOnClickListener {
                    it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(item.id))
                }
            }
        }
    }

    private class UtilDiffDigimon: DiffUtil.ItemCallback<Games>(){
        override fun areItemsTheSame(oldItem: Games, newItem: Games): Boolean {
            return  oldItem == newItem
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
        return ItemViewHolder(GameListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

}