package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubBoardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ClubBoardFragment : Fragment(R.layout.fragment_club_board) {
    private lateinit var binding: FragmentClubBoardBinding
    private val args: ClubBoardFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubBoardBinding.bind(view)

        val clubId = args.clubId

        setupViewPager(clubId)
    }

    private fun setupViewPager(clubId: String) {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        val adapter = ClubBoardPagerAdapter(this, clubId)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "소개"
                1 -> "공지"
                2 -> "활동"
                3 -> "자유"
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }.attach()
    }
}
