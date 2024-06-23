package com.flowbyte.core

import android.content.Context
import android.content.SharedPreferences
import com.flowbyte.data.models.SpotifyToken

object AppPreferences {
    private lateinit var pref: SharedPreferences
    private const val PREF_NAME = "FlowByte"
    private const val PREF_MODE = Context.MODE_PRIVATE

    fun init(context: Context) {
        pref = context.getSharedPreferences(PREF_NAME, PREF_MODE)
    }

    var token: SpotifyToken?
        get() {
            val token = pref.getString(TOKEN, "")
            val expiresAt = pref.getInt(EXPIRES_AT.toString(), 0)
            return if (token != "" && expiresAt != 0) {
                SpotifyToken(token!!, TOKEN_TYPE, expiresAt)
            } else {
                null
            }
        }
        set(value) = pref.edit {
            it.putString(TOKEN, value?.accessToken ?: "")
            it.putString(TOKEN_TYPE, value?.tokenType ?: "")
            it.putInt(EXPIRES_AT.toString(), value?.expiresAt ?: 0)
        }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val TOKEN = "spotify_token"
    private const val TOKEN_TYPE = "Bearer"
    private const val EXPIRES_AT = 0
}