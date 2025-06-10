
package com.example.doosen.network

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)
