package com.example.play4free.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.GameAdapter
import com.example.play4free.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private val gameAdapter: GameAdapter by lazy { GameAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGameList()

        binding.refreshSwipe.setOnRefreshListener {
            viewModel.refreshList()
            if (binding.refreshSwipe.isRefreshing) binding.refreshSwipe.isRefreshing = false
        }

        binding.homeSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.homeRV.scrollToPosition(0)
                binding.homeSV.clearFocus()
                return true
            }


            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) viewModel.search(p0)

                if (p0.isNullOrEmpty()) gameAdapter.submitList(viewModel.gameList.value)
                else gameAdapter.submitList(viewModel.searchResult.value)

                binding.homeRV.scrollToPosition(0)

                return true
            }
        })

        binding.homeSV.setOnSearchClickListener {
            binding.homeRV.scrollToPosition(0)
        }


        val spinner = binding.homeFilterSPN
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_spinner,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                when (p2) {
                    0 -> gameAdapter.submitList(viewModel.gameList.value)
                    1 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "Card Game"
                    })

                    2 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "Fighting"
                    })

                    3 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "MMORPG"
                    })

                    4 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "MMO"
                    })

                    5 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "MOBA"
                    })

                    6 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "Shooter"
                    })

                    7 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "Social"
                    })

                    8 -> gameAdapter.submitList(viewModel.gameList.value!!.filter {
                        it.genre == "Strategy"
                    })
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                gameAdapter.submitList(viewModel.gameList.value)
            }
        }

        binding.homeFilterBTN.setOnClickListener {
            if (binding.homeFilterTV.visibility == View.GONE) {
                binding.homeFilterTV.visibility = View.VISIBLE
                binding.homeFilterSPN.visibility = View.VISIBLE
            } else {
                binding.homeFilterTV.visibility = View.GONE
                binding.homeFilterSPN.visibility = View.GONE
            }
        }

        binding.homeRV.adapter = gameAdapter


        binding.upFAB.setOnClickListener {
            binding.homeRV.scrollToPosition(0)
        }

        viewModel.gameList.observe(viewLifecycleOwner) {
            gameAdapter.submitList(it)
            binding.homeSV.clearFocus()
            binding.homeSV.setQuery("", true)
        }
    }


}