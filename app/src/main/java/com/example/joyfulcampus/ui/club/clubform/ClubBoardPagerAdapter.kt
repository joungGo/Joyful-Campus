package com.example.joyfulcampus.ui.club.clubform

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ClubBoardPagerAdapter(fragment: Fragment, private val clubId: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClubIntroFragment.newInstance(clubId)
            1 -> ClubNoticeFragment.newInstance(clubId)
            2 -> ClubActivityFragment.newInstance(clubId)
            3 -> ClubFreeFragment.newInstance(clubId)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
