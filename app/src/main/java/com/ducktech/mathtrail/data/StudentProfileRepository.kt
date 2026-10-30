package com.ducktech.mathtrail.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.ducktech.mathtrail.domain.StudentProfile

interface StudentProfileRepository {
    fun getProfiles(): List<StudentProfile>
    fun saveProfiles(profiles: List<StudentProfile>)
    fun getActiveProfileId(): String?
    fun setActiveProfileId(profileId: String)
}

class SharedPreferencesStudentProfileRepository(context: Context) : StudentProfileRepository {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getProfiles(): List<StudentProfile> {
        val storedProfiles = preferences.getString(PROFILES_KEY, null) ?: return emptyList()
        val jsonProfiles = runCatching { JSONArray(storedProfiles) }.getOrNull() ?: return emptyList()
        return buildList {
            for (index in 0 until jsonProfiles.length()) {
                val profile = jsonProfiles.optJSONObject(index) ?: continue
                val id = profile.optString(ID_KEY)
                val name = profile.optString(NAME_KEY)
                val avatar = profile.optString(AVATAR_KEY)
                if (id.isNotBlank() && name.isNotBlank() && avatar.isNotBlank()) {
                    add(StudentProfile(id, name, avatar))
                }
            }
        }
    }

    override fun saveProfiles(profiles: List<StudentProfile>) {
        val jsonProfiles = JSONArray()
        profiles.forEach { profile ->
            jsonProfiles.put(
                JSONObject()
                    .put(ID_KEY, profile.id)
                    .put(NAME_KEY, profile.displayName)
                    .put(AVATAR_KEY, profile.avatar)
            )
        }
        preferences.edit().putString(PROFILES_KEY, jsonProfiles.toString()).apply()
    }

    override fun getActiveProfileId(): String? = preferences.getString(ACTIVE_PROFILE_KEY, null)

    override fun setActiveProfileId(profileId: String) {
        preferences.edit().putString(ACTIVE_PROFILE_KEY, profileId).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "math_trail_profiles"
        const val PROFILES_KEY = "profiles"
        const val ACTIVE_PROFILE_KEY = "active_profile_id"
        const val ID_KEY = "id"
        const val NAME_KEY = "display_name"
        const val AVATAR_KEY = "avatar"
    }
}
