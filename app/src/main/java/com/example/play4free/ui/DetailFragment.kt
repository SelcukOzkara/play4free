package com.example.play4free.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.play4free.GameViewModel
import com.example.play4free.R
import com.example.play4free.adapter.CommentAdapter
import com.example.play4free.data.datamodels.Comments
import com.example.play4free.databinding.FragmentDetailBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObjects


class DetailFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var commentIdentifier: String
    private lateinit var commentDocumentReference: DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGameDetail(requireArguments().getLong("id"))
        commentIdentifier = requireArguments().getLong("id").toString()
        commentDocumentReference = viewModel.getCommentDocumentReference(commentIdentifier)

        var toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        var icon = toolbar.findViewById<ImageButton>(R.id.imageButton)

        toolbar.visibility = View.VISIBLE
        icon.visibility = View.GONE

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

                Log.d("ListCheck", it.screenshots.toString())

                var count = it.screenshots.count() -1

                for (image in it.screenshots) {
                    if (!it.screenshots.isNullOrEmpty() && count >= 0) {
                        detailScreenshotsTTV.visibility = View.VISIBLE
                        when (count) {
                            0 -> {
                                detailScreen1IV.load(it.screenshots[count].image) {
                                    crossfade(true)
                                    placeholder(R.drawable.placeholder_image)
                                    error(R.drawable.baseline_error_outline_24)
                                    transformations(RoundedCornersTransformation(40f))
                                }
                                detailScreen1IV.visibility = View.VISIBLE
                            }
                            1 -> {
                                detailScreen2IV.load(it.screenshots[count].image){
                                    crossfade(true)
                                    placeholder(R.drawable.placeholder_image)
                                    error(R.drawable.baseline_error_outline_24)
                                    transformations(RoundedCornersTransformation(40f))
                                }
                                detailScreen2IV.visibility = View.VISIBLE
                            }
                            2 -> {
                                detailScreen3IV.load(it.screenshots[count].image){
                                    crossfade(true)
                                    placeholder(R.drawable.placeholder_image)
                                    error(R.drawable.baseline_error_outline_24)
                                    transformations(RoundedCornersTransformation(40f))
                                }
                                detailScreen3IV.visibility = View.VISIBLE
                            }
                            3 -> {
                                detailScreen4IV.load(it.screenshots[count].image){
                                    crossfade(true)
                                    placeholder(R.drawable.placeholder_image)
                                    error(R.drawable.baseline_error_outline_24)
                                    transformations(RoundedCornersTransformation(40f))
                                }
                                detailScreen4IV.visibility = View.VISIBLE
                            }
                        }
                    }
                    count--
                }

                when (it.screenshots.count()) {
                    0 -> {
                        detailScreenshotsTTV.visibility = View.GONE
                        detailScreen1IV.visibility = View.GONE
                        detailScreen2IV.visibility = View.GONE
                        detailScreen3IV.visibility = View.GONE
                        detailScreen4IV.visibility = View.GONE
                    }
                    1 -> {
                        detailScreen2IV.visibility = View.GONE
                        detailScreen3IV.visibility = View.GONE
                        detailScreen4IV.visibility = View.GONE
                    }
                    2 -> {
                        detailScreen3IV.visibility = View.GONE
                        detailScreen4IV.visibility = View.GONE
                    }
                    3 -> detailScreen4IV.visibility = View.GONE
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

                commentDocumentReference.collection("comment").addSnapshotListener { value, error ->
                    if (error == null && value != null) {
                        val comments = value.toObjects<Comments>().sortedBy { it.timestamp }.takeLast(5)
                        commentRV.adapter = CommentAdapter(comments, viewModel.user.value?.uid , requireContext(), viewModel)
                    } else {
                        Log.e("FirebaseLog",
                            "Error retrieving chat with identifier: $commentIdentifier $value $error"
                        )
                    }
                }

                addCommentBTN.setOnClickListener {
                    val userPb = viewModel.currentUserProfile.value?.pb
                    val userId = viewModel.user.value?.uid
                    val comment = Comments( userId, userPb, binding.commentET.text.toString(), System.currentTimeMillis())
                    viewModel.addCommentToComments(commentIdentifier, comment)
                    commentET.text?.clear()
                }

                viewModel.user.observe(viewLifecycleOwner){
                    if(it?.uid == null){
                        addCommentBTN.visibility = View.GONE
                        commentET.visibility = View.GONE
                    } else{
                        addCommentBTN.visibility = View.VISIBLE
                        commentET.visibility = View.VISIBLE
                    }
                }
            }
        }

    }



    private fun share(textToShare: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}