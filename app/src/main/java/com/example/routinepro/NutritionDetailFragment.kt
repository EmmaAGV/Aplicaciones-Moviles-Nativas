package com.example.routinepro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.routinepro.databinding.FragmentNutritionDetailBinding

class NutritionDetailFragment : Fragment() {

    private var _binding: FragmentNutritionDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("TITLE") ?: "Guía Nutricional"
        val content = arguments?.getString("CONTENT") ?: ""
        val imageRes = arguments?.getInt("IMAGE_RES") ?: R.drawable.carbohidratos

        binding.tvDetailTitle.text = title
        binding.tvDetailContent.text = content
        binding.ivDetailImage.setImageResource(imageRes)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
