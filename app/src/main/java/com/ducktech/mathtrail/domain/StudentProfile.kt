package com.ducktech.mathtrail.domain

import java.util.UUID

/**
 * Identifies one child on this device. The stable [id] lets future practice data
 * belong to a profile without depending on a name that could change.
 */
data class StudentProfile(
    val id: String,
    val displayName: String,
    val avatar: String
)

/** Profile creation rules kept independent of Android and Compose for easy testing. */
object StudentProfileRules {
    const val MAX_NAME_LENGTH = 20
    val availableAvatars = listOf("🦊", "🐼", "🐸", "🦉", "🐢", "🐙")

    /** Returns a message when [name] is blank or too long, and null when it is valid. */
    fun validateName(name: String): String? {
        val trimmedName = name.trim()
        return when {
            trimmedName.isEmpty() -> "Enter a name to continue."
            trimmedName.length > MAX_NAME_LENGTH -> "Use 20 characters or fewer."
            else -> null
        }
    }

    /** Builds a profile after checking its name and avatar against the supported choices. */
    fun create(name: String, avatar: String): StudentProfile {
        require(validateName(name) == null) { "A profile needs a valid display name." }
        require(avatar in availableAvatars) { "Choose an available avatar." }
        return StudentProfile(UUID.randomUUID().toString(), name.trim(), avatar)
    }
}
