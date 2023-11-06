package com.example.play4free.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.databinding.FragmentLoginBinding
import com.example.play4free.databinding.ResetPwBinding
import com.example.play4free.databinding.SignUpBinding
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class LoginFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginSignUpBTN.setOnClickListener {
            showDialog()
        }

        binding.loginPwResetTV.setOnClickListener {
            showResetDialog()
        }
        var toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)

        toolbar.visibility = View.GONE

        binding.loginSignInBTN.setOnClickListener {
            val email = binding.loginMailET.text.toString()
            val pw = binding.loginPwET.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
                binding.loginMailET.requestFocus()
                binding.loginMailET.error = "Enter a valid email address"
            } else if (pw.isEmpty()) {
                binding.loginPwET.requestFocus()
                binding.loginPwET.error = "Enter a password"
            } else viewModel.signIn(email, pw)
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
            }
        }
    }

    private fun showDialog() {
        val signUpBinding: SignUpBinding = SignUpBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        dialog.setContentView(signUpBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (WindowManager.LayoutParams.MATCH_PARENT),
            (WindowManager.LayoutParams.MATCH_PARENT)
        )

        signUpBinding.signUpDateET.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    signUpBinding.signUpDateET.setText(formattedDate)
                    Log.d("DatePicker", formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        signUpBinding.signUpCreateBTN.setOnClickListener {
            val email = signUpBinding.signUpEmailET.text.toString()
            val pw = signUpBinding.signUpPwET.text.toString()
            val pwCon = signUpBinding.signUpPwSecET.text.toString()
            val username = signUpBinding.signUpUsernameET.text.toString()
            val date = signUpBinding.signUpDateET.text.toString()


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
                signUpBinding.signUpEmailET.requestFocus()
                signUpBinding.signUpEmailET.error = "Enter a valid email address"
                return@setOnClickListener
            }
            if (date.isEmpty() || date.split(".").last().toInt() >= 2005) {
                signUpBinding.signUpDateET.error = "You must be 18 years old!"
            } else if (pw.isEmpty() || pw.length < 6) {
                signUpBinding.signUpPwET.error = "Password must be at least 6 characters"
            } else if (pw != pwCon) {
                signUpBinding.signUpPwSecET.error =
                    "Confirm password has to be the same as password"
            } else {
                viewModel.signUp(email, pw, username, date)

                viewModel.status.observe(viewLifecycleOwner) {
                    when (it) {
                        1 -> dialog.dismiss()
                        2 -> {
                            signUpBinding.signUpEmailET.requestFocus()
                            signUpBinding.signUpEmailET.error = "Email already taken"
                        }

                        3 -> Toast.makeText(requireContext(), "SignUp failed!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        }

        signUpBinding.signUpAbortBTN.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun showResetDialog() {
        val resetBinding: ResetPwBinding = ResetPwBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        dialog.setContentView(resetBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (WindowManager.LayoutParams.MATCH_PARENT),
            (WindowManager.LayoutParams.MATCH_PARENT)
        )


        resetBinding.resetBTN.setOnClickListener {
            val email = resetBinding.emailResetET.text.toString()


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
                resetBinding.emailResetET.requestFocus()
                resetBinding.emailResetET.error = "Enter a valid email address"
                return@setOnClickListener
            } else {
                viewModel.resetPw(email)
                Toast.makeText(requireContext(),"Reset email was send",Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }


        dialog.show()
    }


}