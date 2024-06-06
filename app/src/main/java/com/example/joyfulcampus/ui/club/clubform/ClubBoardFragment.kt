package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubBoardBinding
import com.google.android.material.tabs.TabLayoutMediator

class ClubBoardFragment : Fragment(R.layout.fragment_club_board) {

    private lateinit var binding: FragmentClubBoardBinding
    private val args: ClubBoardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clubName = args.clubName
        val clubId = args.clubId

        // 툴바 설정
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = clubName
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // ViewPager2와 TabLayout 설정
        val viewPager = binding.viewPager
        viewPager.adapter = ClubBoardPagerAdapter(this, clubId)

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "소개"
                1 -> "자유"
                2 -> "활동"
                3 -> "공고문"
                else -> null
            }
        }.attach()
    }
}

class ClubBoardPagerAdapter(fragment: Fragment, private val clubId: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClubIntroFragment().apply {
                arguments = Bundle().apply { putString("clubId", clubId) }
            }
            1 -> ClubFreeFragment().apply {
                arguments = Bundle().apply { putString("clubId", clubId) }
            }
            2 -> ClubActivityFragment().apply {
                arguments = Bundle().apply { putString("clubId", clubId) }
            }
            3 -> ClubNoticeFragment().apply {
                arguments = Bundle().apply { putString("clubId", clubId) }
            }
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
