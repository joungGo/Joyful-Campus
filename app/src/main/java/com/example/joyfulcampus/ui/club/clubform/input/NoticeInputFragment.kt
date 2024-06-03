package com.example.joyfulcampus.ui.club.clubform.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.data.ClubViewModel
import com.example.joyfulcampus.data.NoticeData
import com.example.joyfulcampus.databinding.FragmentNoticeInputBinding

class NoticeInputFragment : Fragment(R.layout.fragment_notice_input) {

    private lateinit var binding: FragmentNoticeInputBinding
    private val viewModel: ClubViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val clubName = binding.inputNoticeClubName.text.toString()
            val recruitmentPeriod = binding.inputNoticeRecruitmentPeriod.text.toString()
            val interview = binding.inputNoticeInterview.text.toString()
            val noticeContent = binding.inputNoticeContent.text.toString()

            val noticeData = NoticeData(clubName, recruitmentPeriod, interview, noticeContent)
            viewModel.saveNoticeData(noticeData)

            findNavController().navigateUp()
        }
    }
}

