package com.example.joyfulcampus.ui.food

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.joyfulcampus.R
import com.example.joyfulcampus.databinding.FragmentFoodBinding

class FoodFragment: Fragment(R.layout.fragment_food) {
    private lateinit var binding: FragmentFoodBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFoodBinding.bind(view)
    }
}