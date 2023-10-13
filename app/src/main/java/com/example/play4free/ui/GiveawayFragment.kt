package com.example.play4free.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.play4free.GameViewModel
import com.example.play4free.adapter.GiveawayAdapter
import com.example.play4free.databinding.FragmentGiveawayBinding

class GiveawayFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentGiveawayBinding
    private val giveawayAdapter: GiveawayAdapter by lazy { GiveawayAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiveawayBinding.inflate(inflater,container,false)
        viewModel.loadGiveawayList()
        binding.giveawayRV.adapter = giveawayAdapter

        viewModel.giveawayList.observe(viewLifecycleOwner){
            giveawayAdapter.submitList(it)
        }

        return binding.root
    }

}