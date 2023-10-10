package com.example.play4free.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.play4free.GameViewModel
import com.example.play4free.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGameDetail(requireArguments().getLong("id"))

        viewModel.gameDetail.observe(viewLifecycleOwner) {
            with(binding) {
                detailThumbIV.load(it.thumbnail)
                detailTitleTV.text = it.title
                detailDescTV.text = it.description
                detailOsTV.text = it.minimum_system_requirements?.os ?: "n/a"
                detailCpuTV.text = it.minimum_system_requirements?.processor ?: "n/a"
                detailRamTV.text = it.minimum_system_requirements?.memory ?: "n/a"
                detailGpuTV.text = it.minimum_system_requirements?.graphics ?: "n/a"
                detailStorageTV.text = it.minimum_system_requirements?.storage ?: "n/a"
                try {
                    detailScreen1IV.load(it.screenshots[0].image)
                    detailScreen2IV.load(it.screenshots[1].image)
                    detailScreen3IV.load(it.screenshots[2].image)
                    detailScreen4IV.load(it.screenshots[3].image)
                }catch (e:Exception){

                }

                detailShareBTN.setOnClickListener {
                    share(viewModel.gameDetail.value!!.game_url)
                }

                detailPlayNowBTN.setOnClickListener {
                    val playNow = Intent(Intent.ACTION_VIEW)
                    playNow.data = Uri.parse(viewModel.gameDetail.value!!.game_url)
                    startActivity(playNow)
                }


                detailDescTV.setInterpolator(OvershootInterpolator())


                detailReadMoreTV.setOnClickListener {
                        detailDescTV.toggle()
                        if (!detailDescTV.isExpanded) detailReadMoreTV.text = "read less"
                        else detailReadMoreTV.text = "read more"
                }


            }
        }
    }

    fun share(textToShare: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}