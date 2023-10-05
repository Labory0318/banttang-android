package com.hproject.core.data.data_source.local.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    val context : Context
) {
    companion object {
        private const val TOKEN_DATASTORE ="TOKEN_DATASTORE"
        private const val LOGIN_CHECK_DATASTORE ="LOGIN_CHECK_DATASTORE"
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
    }

    private val Context.tokenDataStore by preferencesDataStore(TOKEN_DATASTORE)
    private val Context.loginCheckDataStore by preferencesDataStore(LOGIN_CHECK_DATASTORE)

    suspend fun saveToken(accessToken : String, refreshToken : String, fcmToken : String){
        context.tokenDataStore.edit { dataStore ->
            dataStore[ACCESS_TOKEN] = accessToken
            dataStore[REFRESH_TOKEN] = refreshToken
            dataStore[FCM_TOKEN] = fcmToken
        }
        context.loginCheckDataStore.edit {dataStore ->
            dataStore[LOGIN_CHECK] = true
        }
    }

    suspend fun getToken(): Flow<List<String>> = context.tokenDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { dataStore ->
            dataStore.asMap().values.toList().map { token ->
                token.toString()
            }
        }

    suspend fun getIsLogin(): Flow<Boolean> =
        context.loginCheckDataStore.data.map { prefs ->
            prefs[LOGIN_CHECK] ?: false
        }

    suspend fun logout(){
        context.loginCheckDataStore.edit {dataStore ->
            dataStore[LOGIN_CHECK] = false
        }
    }

}