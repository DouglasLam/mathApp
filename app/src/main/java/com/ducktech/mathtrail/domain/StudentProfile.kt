package com.ducktech.mathtrail.domain

import java.util.UUID

data class StudentProfile(
    val id: String,
    val displayName: String,
    val avatar: String
)

object StudentProfileRules {
    const val MAX_NAME_LENGTH = 20
    val availableAvatars = listOf("🦊", "🐼", "🐸", "🦉", "🐢", "🐙")

    fun validateName(name: String): String? {
        val trimmedName = name.trim()
        return when {
            trimmedName.isEmpty() -> "Enter a name to continue."
            trimmedName.length > MAX_NAME_LENGTH -> "Use 20 characters or fewer."
            else -> null
        }
    }

    fun create(name: String, avatar: String): StudentProfile {
        require(validateName(name) == null) { "A profile needs a valid display name." }
        require(avatar in availableAvatars) { "Choose an available avatar." }
        return StudentProfile(UUID.randomUUID().toString(), name.trim(), avatar)
    }
}
