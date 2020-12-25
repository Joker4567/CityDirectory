package com.anufriev.data.storage

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Pref(context: Context) {

    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var sharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var authToken: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences.edit {
                putString(KEY_TOKEN, value)
            }
        }

    var city: String?
        get() = sharedPreferences.getString(KEY_CITY, null)
        set(value) {
            sharedPreferences.edit {
                putString(KEY_CITY, value)
            }
        }

    fun clearToken() {
        sharedPreferences.edit {
            remove(KEY_TOKEN)
        }
    }

    fun clearCity() {
        sharedPreferences.edit {
            remove(KEY_CITY)
        }
    }

    companion object {
        const val FILE_NAME = "CityPreference"
        const val KEY_TOKEN = "token"
        const val KEY_CITY = "city"
    }
}