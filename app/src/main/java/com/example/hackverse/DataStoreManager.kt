import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val UID_KEY = stringPreferencesKey("uid")
    }

    suspend fun saveUserData(name: String, email: String, uid: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[UID_KEY] = uid
        }
    }
    // Function to clear user data
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getUserData() = dataStore.data.map { preferences ->
        Triple(
            preferences[NAME_KEY] ?: "",
            preferences[EMAIL_KEY] ?: "",
            preferences[UID_KEY] ?: ""
        )
    }
}
