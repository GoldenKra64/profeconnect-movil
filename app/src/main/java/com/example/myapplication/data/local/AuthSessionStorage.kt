package com.example.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.domain.model.AuthSession
import com.example.myapplication.domain.model.AuthUser

object AuthSessionStorage {
    private const val PREFS_NAME = "profeconnect_auth"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "institutional_email"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_ROLE = "role"
    private const val KEY_STATUS = "status"

    @Volatile
    private var preferences: SharedPreferences? = null

    fun initialize(context: Context) {
        if (preferences == null) {
            preferences = context.applicationContext.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    fun saveSession(session: AuthSession) {
        prefs().edit()
            .putString(KEY_TOKEN, session.token)
            .putInt(KEY_USER_ID, session.user.id)
            .putString(KEY_EMAIL, session.user.institutionalEmail)
            .putString(KEY_FIRST_NAME, session.user.firstName)
            .putString(KEY_LAST_NAME, session.user.lastName)
            .putString(KEY_ROLE, session.user.role)
            .putString(KEY_STATUS, session.user.status)
            .apply()
    }

    fun getToken(): String? = prefsOrNull()
        ?.getString(KEY_TOKEN, null)
        ?.takeIf { it.isNotBlank() }

    fun getSession(): AuthSession? {
        val prefs = prefsOrNull() ?: return null
        val token = prefs.getString(KEY_TOKEN, null)?.takeIf { it.isNotBlank() } ?: return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val firstName = prefs.getString(KEY_FIRST_NAME, null) ?: return null
        val lastName = prefs.getString(KEY_LAST_NAME, null) ?: return null
        val role = prefs.getString(KEY_ROLE, null) ?: return null
        val status = prefs.getString(KEY_STATUS, null) ?: return null

        return AuthSession(
            token = token,
            user = AuthUser(
                id = prefs.getInt(KEY_USER_ID, 0),
                institutionalEmail = email,
                firstName = firstName,
                lastName = lastName,
                role = role,
                status = status
            )
        )
    }

    fun clearSession() {
        prefsOrNull()?.edit()?.clear()?.apply()
    }

    private fun prefs(): SharedPreferences {
        return checkNotNull(preferences) {
            "AuthSessionStorage must be initialized before use."
        }
    }

    private fun prefsOrNull(): SharedPreferences? = preferences
}
