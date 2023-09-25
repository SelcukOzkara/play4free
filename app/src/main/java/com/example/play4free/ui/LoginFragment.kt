package com.example.play4free.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.databinding.FragmentLoginBinding
import com.example.play4free.databinding.SignUpBinding


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
//            val email = binding.loginMailET.text.toString()
//            val pw = binding.loginPwET.text.toString()
//
//            viewModel.signUp(email,pw)
            showDialog()
        }

        binding.loginSignInBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val pw = binding.loginPwET.text.toString()

            viewModel.signIn(email,pw)
        }

        viewModel.user.observe(viewLifecycleOwner){
            if(it != null){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
            }
        }
    }

    private fun showDialog() {
        val signUpDialog : SignUpBinding = SignUpBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        dialog.setContentView(signUpDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setLayout((WindowManager.LayoutParams.MATCH_PARENT), (WindowManager.LayoutParams.MATCH_PARENT))
//        dialog.window?.setGravity(Gravity.BOTTOM)


        signUpDialog.signUpCreateBTN.setOnClickListener {

            dialog.dismiss()
        }

        signUpDialog.signUpAbortBTN.setOnClickListener {

            dialog.dismiss()
        }


        dialog.show()
    }

}