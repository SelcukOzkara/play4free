package com.example.play4free.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.GameAdapter
import com.example.play4free.databinding.FragmentHomeBinding
import com.example.play4free.databinding.FragmentLoginBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        showDialog()

        viewModel.gameList.observe(viewLifecycleOwner){
            gameAdapter.submitList(it)
        }
    }


    private fun showDialog() {
        val binding2 : FragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        dialog.setContentView(binding2.root)
        dialog.window?.setLayout((WindowManager.LayoutParams.MATCH_PARENT), (resources.displayMetrics.heightPixels*0.8).toInt())
//        dialog.window?.setGravity(Gravity.BOTTOM)
        binding.homeRV.alpha = 0.2f

        binding2.loginMailET.setText("Test")

        binding2.loginSignUpBTN.setOnClickListener {
            binding.homeRV.alpha = 1f
            dialog.dismiss()
        }

       binding2.loginSignInBTN.setOnClickListener {
           binding.homeRV.alpha = 1f
           dialog.dismiss()
       }


        dialog.show()
    }

}