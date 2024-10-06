package com.vadimraspopin.mangotest.api.providers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreTokenProvider @Inject constructor(@ApplicationContext private val context: Context) :
    TokenProvider {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

        private val Context.dataStore by preferencesDataStore(name = "auth_tokens")
    }

    private val dataStore = context.dataStore

    private val accessTokenCache = AtomicReference<String?>(null)
    private val refreshTokenCache = AtomicReference<String?>(null)

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            dataStore.data
                .map { preferences ->
                    preferences[ACCESS_TOKEN_KEY] to preferences[REFRESH_TOKEN_KEY]
                }
                .collect { (accessToken, refreshToken) ->
                    accessTokenCache.set(accessToken)
                    refreshTokenCache.set(refreshToken)
                }
        }
    }

    override fun getAccessToken(): String? {
        return accessTokenCache.get()
    }

    override fun getRefreshToken(): String? {
        return refreshTokenCache.get()
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = accessToken
                preferences[REFRESH_TOKEN_KEY] = refreshToken
            }
            accessTokenCache.set(accessToken)
            refreshTokenCache.set(refreshToken)
        }
    }
}
