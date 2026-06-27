package com.example.routinepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.routinepro.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                binding.ivProfile.setImageURI(imageUri)
                saveProfileImage(imageUri.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()
        setupListeners()
    }

    private fun setupListeners() {
        binding.ivProfile.setOnClickListener { openGallery() }

        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Editar Perfil: Próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnNotifications.setOnClickListener {
            Toast.makeText(requireContext(), "Ajustes de Notificaciones", Toast.LENGTH_SHORT).show()
        }

        binding.btnPrivacy.setOnClickListener {
            Toast.makeText(requireContext(), "Privacidad y Seguridad", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            // Reiniciar registro para volver a la encuesta (simulación de logout)
            val prefs = requireContext().getSharedPreferences("RoutineProPrefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            
            // Reiniciar la aplicación o ir a la encuesta
            requireActivity().recreate()
        }
    }

    private fun loadUserData() {
        val prefs = requireContext().getSharedPreferences("RoutineProPrefs", Context.MODE_PRIVATE)
        val gender = prefs.getString("user_gender", "Mujer")
        val goal = prefs.getString("user_goal", "Saludable")
        val age = prefs.getInt("user_age", 25)
        val savedImageUri = prefs.getString("profile_image_uri", null)

        binding.tvUserName.text = "Usuario ($age años)"
        binding.tvUserGoal.text = "Meta principal: $goal"

        if (savedImageUri != null) {
            try {
                binding.ivProfile.setImageURI(Uri.parse(savedImageUri))
            } catch (_: Exception) {
                setDefaultAvatar(gender)
            }
        } else {
            setDefaultAvatar(gender)
        }
    }

    private fun setDefaultAvatar(gender: String?) {
        val avatarRes = if (gender == "Hombre") {
            R.drawable.images_de_hombre
        } else {
            R.drawable.images_de_mujer
        }
        binding.ivProfile.setImageResource(avatarRes)
    }

    private fun saveProfileImage(uri: String) {
        val prefs = requireContext().getSharedPreferences("RoutineProPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("profile_image_uri", uri).apply()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
