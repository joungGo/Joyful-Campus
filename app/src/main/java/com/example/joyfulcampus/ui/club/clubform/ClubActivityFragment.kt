package com.example.joyfulcampus.ui.club.clubform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentClubActivityBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClubActivityFragment : Fragment(R.layout.fragment_club_activity) {
    private lateinit var binding: FragmentClubActivityBinding
    private lateinit var adapter: ClubActivityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClubActivityBinding.bind(view)

        val clubId = arguments?.getString("clubId") ?: return

        setupRecyclerView()
        observeActivityData(clubId)

        binding.addActivityFab.setOnClickListener {
            val action = ClubBoardFragmentDirections.actionClubBoardFragmentToActivityInputFragment(clubId)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        adapter = ClubActivityAdapter()
        binding.activityRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.activityRecyclerView.adapter = adapter

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.activityRecyclerView.addItemDecoration(GridSpacingItemDecoration(3, spacingInPixels, true))
    }

    private fun observeActivityData(clubId: String) {
        Firebase.firestore.collection("bulletinBoard").document(clubId).collection("activities")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // 데이터 로드 실패 시 처리
                    return@addSnapshotListener
                }

                val activities = snapshots?.map { document ->
                    document.toObject(Activity::class.java)
                } ?: emptyList()

                adapter.submitList(activities)
            }
    }

    companion object {
        fun newInstance(clubId: String) = ClubActivityFragment().apply {
            arguments = Bundle().apply {
                putString("clubId", clubId)
            }
        }
    }
}
