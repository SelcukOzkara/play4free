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
import com.example.play4free.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginSignUpBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val pw = binding.loginPwET.text.toString()

            viewModel.signUp(email,pw)
        }

        binding.loginSignInBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val pw = binding.loginPwET.text.toString()

            viewModel.signIn(email,pw)
        }

        viewModel.user.observe(viewLifecycleOwner){
            if(it != null){
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }


}