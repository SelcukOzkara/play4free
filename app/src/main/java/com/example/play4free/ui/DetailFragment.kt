package com.example.play4free.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGameDetail(requireArguments().getLong("id"))

        viewModel.gameDetail.observe(viewLifecycleOwner){
            with(binding){
                detailThumbIV.load(it.thumbnail)
                detailTitleTV.text = it.title
                detailDescTV.text = it.description
                detailOsTV.text = it.minimum_system_requirements.os
                detailCpuTV.text = it.minimum_system_requirements.processor
                detailRamTV.text = it.minimum_system_requirements.memory
                detailGpuTV.text = it.minimum_system_requirements.graphics
                detailStorageTV.text = it.minimum_system_requirements.storage
                detailScreen1IV.load(it.screenshots[0].image)
                detailScreen2IV.load(it.screenshots[1].image)
                detailScreen3IV.load(it.screenshots[2].image)
                detailScreen4IV.load(it.screenshots[3].image)
            }
        }


    }

}