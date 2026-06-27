package com.example.routinepro.model

import java.io.Serializable

data class UserProfile(
    val age: Int,
    val weight: Float,
    val height: Int,
    val gender: String,
    val size: String,
    val bodyGoal: String,
    val mainGoal: String,
    val zones: String,
    val history: String,
    val activityLevel: String,
    val habits: String
) : Serializable
