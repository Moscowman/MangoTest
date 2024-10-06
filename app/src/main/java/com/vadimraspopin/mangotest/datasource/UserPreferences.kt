package com.vadimraspopin.mangotest.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.vadimraspopin.mangotest.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val USER_KEY = stringPreferencesKey("user_key")
        private val gson = Gson()
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = gson.toJson(user)
        }
    }

    val userFlow: Flow<User?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_KEY]?.let { gson.fromJson(it, User::class.java) }
        }
}
