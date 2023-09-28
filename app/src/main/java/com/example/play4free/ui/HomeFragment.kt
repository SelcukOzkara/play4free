package com.example.play4free.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.play4free.GameViewModel
import com.example.play4free.adapter.GameAdapter
import com.example.play4free.databinding.FragmentHomeBinding

private lateinit var binding: FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private  lateinit var binding: FragmentHomeBinding
    private val gameAdapter: GameAdapter by lazy { GameAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       viewModel.loadGameList()


        binding.homeRV.adapter = gameAdapter

        viewModel.gameList.observe(viewLifecycleOwner){
            gameAdapter.submitList(it)
        }
    }




}