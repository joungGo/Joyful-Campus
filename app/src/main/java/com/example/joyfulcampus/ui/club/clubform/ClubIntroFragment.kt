package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubIntroBinding

class ClubIntroFragment : Fragment(R.layout.fragment_club_intro) {

    private lateinit var binding: FragmentClubIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*viewModel.introData.observe(viewLifecycleOwner) { introData ->
            binding.clubName.text = introData.clubName
            binding.clubDescription.text = introData.clubDescription
            binding.clubLongDescription.text = introData.clubLongDescription
        }*/

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_clubBoardFragment_to_introInputFragment)
        }
    }
}
