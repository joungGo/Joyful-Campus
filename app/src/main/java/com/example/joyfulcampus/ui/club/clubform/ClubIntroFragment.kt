package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubIntroBinding
import com.example.joyfulcampus.viewmodel.ClubViewModel

class ClubIntroFragment : Fragment(R.layout.fragment_club_intro) {

    private lateinit var binding: FragmentClubIntroBinding
    private val viewModel: ClubViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clubId = arguments?.getString("clubId") ?: return

        viewModel.introData.observe(viewLifecycleOwner) { introData ->
            binding.clubName.text = introData.clubName
            binding.clubDescription.text = introData.clubDescription
            binding.clubLongDescription.text = introData.clubLongDescription

            // Glide를 사용하여 이미지 로드
            Glide.with(this)
                .load(introData.clubImageUrl)
                .into(binding.clubImage)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                ClubBoardFragmentDirections.actionClubBoardFragmentToIntroInputFragment(clubId)
            )
        }
    }
}
