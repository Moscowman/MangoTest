package com.vadimraspopin.mangotest.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.vadimraspopin.mangotest.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfilePreferencesImpl @Inject constructor(
    val context: Context,
    private val gson: Gson
) : ProfilePreferences {

    companion object {
        private val PROFILE_KEY = stringPreferencesKey("profile_key")
        private const val DATASTORE_NAME = "profile_preferences"
    }

    private val dataStore: DataStore<Preferences> = createDataStore(context)

    override val profileFlow: Flow<User?> = dataStore.data
        .map { preferences ->
            preferences[PROFILE_KEY]?.let { json ->
                deserializeUser(json)
            }
        }

    override suspend fun saveProfile(user: User) {
        dataStore.edit { preferences ->
            preferences[PROFILE_KEY] = serializeUser(user)
        }
    }

    override suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences.remove(PROFILE_KEY)
        }
    }

    private fun serializeUser(user: User): String {
        return gson.toJson(user)
    }

    private fun deserializeUser(json: String): User {
        return gson.fromJson(json, User::class.java)
    }

    private fun createDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
        )
    }
}