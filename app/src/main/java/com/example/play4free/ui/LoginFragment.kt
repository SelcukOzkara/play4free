package com.example.play4free.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.databinding.FragmentLoginBinding
import com.example.play4free.databinding.SignUpBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.sign


class LoginFragment : Fragment() {

    val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginSignUpBTN.setOnClickListener {
            showDialog()
        }

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
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setLayout(
            (WindowManager.LayoutParams.MATCH_PARENT),
            (WindowManager.LayoutParams.MATCH_PARENT)
        )

        signUpBinding.signUpDateET.setOnClickListener {
            showDatePicker()
        }


        signUpBinding.signUpCreateBTN.setOnClickListener {
            var email = signUpBinding.signUpEmailET.text.toString()
            var pw = signUpBinding.signUpPwET.text.toString()
            var pwCon = signUpBinding.signUpPwSecET.text.toString()
            var username = signUpBinding.signUpUsernameET.text.toString()
            var date = signUpBinding.signUpDateET.text.toString()


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
                signUpBinding.signUpEmailET.requestFocus()
                signUpBinding.signUpEmailET.error = "Enter a valid email address"
                return@setOnClickListener
            }

            if (pw.isEmpty() || pw.length < 6) {
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val signUpBinding: SignUpBinding = SignUpBinding.inflate(layoutInflater)
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/mm/yyyy"
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the " " prefix
                signUpBinding.signUpDateET.setText("Selected Date: $formattedDate")
                Log.d("DatePicker", formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}