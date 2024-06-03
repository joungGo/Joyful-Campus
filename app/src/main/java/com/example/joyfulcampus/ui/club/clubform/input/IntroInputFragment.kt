package com.example.joyfulcampus.ui.club.clubform.input

import com.example.joyfulcampus.data.ClubViewModel
import com.example.joyfulcampus.data.IntroData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentIntroInputBinding

class IntroInputFragment : Fragment(R.layout.fragment_intro_input) {

    private lateinit var binding: FragmentIntroInputBinding
    private val viewModel: ClubViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val clubName = binding.inputClubName.text.toString()
            val clubDescription = binding.inputClubDescription.text.toString()
            val clubLongDescription = binding.inputClubLongDescription.text.toString()

            val introData = IntroData(clubName, clubDescription, clubLongDescription)
            viewModel.saveIntroData(introData)

            findNavController().navigateUp()
        }
    }
}



