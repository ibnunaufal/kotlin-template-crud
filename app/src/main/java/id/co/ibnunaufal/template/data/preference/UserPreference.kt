package id.co.ibnunaufal.template.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class UserPreference(context: Context) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )
    }

    inline fun <reified T> getUserObject(): T? {
        val value = runBlocking { userDataRaw.first() }
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    val userDataRaw: Flow<String?>
        get() = dataStore.data.map { pref ->
            pref[USER_OBJECT]
        }
    fun getAuthToken():String?{
        return runBlocking { authToken.first() }
    }
    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }
    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val USER_OBJECT = preferencesKey<String>(name = "userObj")
        private val KEY_AUTH = preferencesKey<String>("key_auth")
    }
}