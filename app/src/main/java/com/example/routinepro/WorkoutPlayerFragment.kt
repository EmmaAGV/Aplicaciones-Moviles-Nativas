package com.example.routinepro

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.routinepro.databinding.FragmentWorkoutPlayerBinding

class WorkoutPlayerFragment : Fragment() {

    private var _binding: FragmentWorkoutPlayerBinding? = null
    private val binding get() = _binding!!
    
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isResting = false
    private var timeLeftInMillis: Long = 30000 // 30 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        startExercise()

        binding.btnPlayPause.setOnClickListener {
            if (isRunning) pauseTimer() else resumeTimer()
        }
    }

    private fun startExercise() {
        isResting = false
        timeLeftInMillis = 30000
        binding.tvTimerLabel.text = "¡A entrenar!"
        binding.tvExerciseName.text = "Flexiones de Brazo"
        binding.layoutControls.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
        startTimer()
    }

    private fun startRest() {
        playFinishSound()
        isResting = true
        timeLeftInMillis = 15000 // 15 seconds rest
        binding.tvTimerLabel.text = "DESCANSO"
        binding.tvExerciseName.text = "Próximo: Sentadillas"
        // Cambiar color para indicar descanso
        binding.tvTimer.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLeftInMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                isRunning = false
                if (!isResting) {
                    startRest()
                } else {
                    playFinishSound()
                    startExercise() // Loop or move to next
                }
            }
        }.start()

        isRunning = true
        binding.btnPlayPause.setIconResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
        binding.btnPlayPause.setIconResource(android.R.drawable.ic_media_play)
    }

    private fun resumeTimer() {
        startTimer()
    }

    private fun updateTimerUI() {
        val seconds = (timeLeftInMillis / 1000).toInt()
        binding.tvTimer.text = seconds.toString()
        binding.workoutProgress.progress = if (isResting) {
            ((15000 - timeLeftInMillis) * 100 / 15000).toInt()
        } else {
            ((30000 - timeLeftInMillis) * 100 / 30000).toInt()
        }
    }

    private fun playFinishSound() {
        try {
            val toneG = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
            toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }
}
