package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubNoticeBinding

class ClubNoticeFragment : Fragment(R.layout.fragment_club_notice) {

    private lateinit var binding: FragmentClubNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*viewModel.noticeData.observe(viewLifecycleOwner) { noticeData ->
            binding.noticeClubName.text = noticeData.clubName
            binding.noticeRecruitmentPeriod.text = noticeData.recruitmentPeriod
            binding.noticeInterview.text = noticeData.interview
            binding.noticeContent.text = noticeData.noticeContent
        }*/

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_clubBoardFragment_to_noticeInputFragment)
        }
    }
}



