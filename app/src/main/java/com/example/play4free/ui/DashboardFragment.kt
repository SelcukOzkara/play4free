package com.example.play4free.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.FavAdapter
import com.example.play4free.databinding.FragmentDashboardBinding
import com.example.play4free.databinding.ProfilEditBinding
import com.example.play4free.databinding.SignUpBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    val viewModel: GameViewModel by activityViewModels()
    private val favAdapter: FavAdapter by lazy { FavAdapter(viewModel) }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    var dialogBinding: ProfilEditBinding = ProfilEditBinding.inflate(layoutInflater)
                    binding.dashboardPbIV.setImageURI(uri)
                    dialogBinding.editPbIV.setImageURI(uri)
                    binding.dashboardPbIV.visibility = View.VISIBLE
                    viewModel.uploadImage(uri)
                }
            }
        }



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

        val username = viewModel.currentUserProfile.value?.username
        val date = viewModel.currentUserProfile.value?.date
        val pb = viewModel.currentUserProfile.value?.pb

        binding.dashboardUsernameTV.text = username
        binding.dashboardMailTV.text = date
        binding.dashboardPbIV.load(pb)

        if (!pb.isNullOrEmpty()) {
            binding.dashboardPbIV.load(pb){
                crossfade(true)
                placeholder(R.drawable.baseline_account_box_24)
                error(R.drawable.baseline_error_outline_24)
                transformations(RoundedCornersTransformation(40f))
            }
        } else {
            binding.dashboardPbIV.setImageResource(R.drawable.baseline_account_box_24)
        }

        viewModel.gameList.observe(viewLifecycleOwner){
            favAdapter.submitList(it.filter { it.isLiked })
        }


        binding.dashboardSettingBTN.setOnClickListener{
            showDialog()
        }

        binding.dashboardLogOutBTN.setOnClickListener {
            viewModel.signOut()
            findNavController().popBackStack()
        }
    }

    private fun showDialog() {
        val binding: ProfilEditBinding = ProfilEditBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setLayout(
            (WindowManager.LayoutParams.MATCH_PARENT),
            (WindowManager.LayoutParams.MATCH_PARENT)
        )

        binding.usernameET.setText(viewModel.currentUserProfile.value?.username)
        binding.editPbIV.load(viewModel.currentUserProfile.value?.pb)

        binding.saveBTN.setOnClickListener {
                val updatedUsername = binding.usernameET.text.toString()

                try {
                    viewModel.profileRef.update(
                        "username",
                        updatedUsername,
                        "pb",


                    ).addOnSuccessListener {
                        dialog.dismiss()
                    }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

        }

        binding.editPbBTN.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            getContent.launch(intent)
        }

        binding.editPwBTN.setOnClickListener {
            viewModel.resetPw()
            Toast.makeText(requireContext(),"Reset email was send",Toast.LENGTH_LONG).show()
        }

        binding.cancleBTN.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}