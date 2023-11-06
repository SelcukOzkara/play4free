package com.example.play4free.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.GiveawayAdapter
import com.example.play4free.databinding.FragmentGiveawayBinding
import com.google.android.material.appbar.MaterialToolbar

class GiveawayFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentGiveawayBinding
    private val giveawayAdapter: GiveawayAdapter by lazy { GiveawayAdapter(viewModel,requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiveawayBinding.inflate(inflater,container,false)
        viewModel.loadGiveawayList()
        binding.giveawayRV.adapter = giveawayAdapter

        var toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        var icon = toolbar.findViewById<ImageButton>(R.id.imageButton)

        toolbar.visibility = View.VISIBLE
        icon.visibility = View.GONE

        viewModel.giveawayList.observe(viewLifecycleOwner){
            giveawayAdapter.submitList(it)
        }

        return binding.root
    }

}