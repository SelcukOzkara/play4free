package com.example.play4free.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.FavAdapter
import com.example.play4free.adapter.GameAdapter
import com.example.play4free.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    val viewModel: GameViewModel by activityViewModels()
    private val favAdapter: FavAdapter by lazy { FavAdapter(viewModel) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favRV.adapter = favAdapter

        viewModel.gameList.observe(viewLifecycleOwner){
            favAdapter.submitList(it.filter { it.isLiked })
        }

        binding.dashboardLogOutBTN.setOnClickListener {
            viewModel.signOut()
            findNavController().popBackStack()
        }
    }

}