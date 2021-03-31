package com.urbanfresh.data.local

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*


object AppPreference {

    private const val prefName = "demo"
    var sharedPreferences: SharedPreferences? = null
    @RequiresApi(Build.VERSION_CODES.M)
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    @RequiresApi(Build.VERSION_CODES.M)
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    /**
     *  This method is used for create LoginPreference instance.
     *
     *  @param context is a instance of current class.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun getInstance(context: Context) {
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        sharedPreferences = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * This method is used for add string values to shared preference
     *
     * @param PreferenceKeys is unik key,
     * @param String holds value type of string.
     */
    fun addValue(preferencesKey: PreferenceKeys, value: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(preferencesKey.toString(), value)
        editor.apply()
    }

    /**
     * This method is used for add boolean value to shared preference
     *
     * @param PreferenceKeys is unik key,
     * @param Boolean holds value type of boolean.
     */
    fun addBoolean(preferencesKey: PreferenceKeys, value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(preferencesKey.toString(), value)
        editor.apply()
    }

    /**
     * This method is used for add Integer value to shared preference
     *
     * @param PreferenceKeys is unik key,
     * @param Int holds value type of integer.
     */
    fun addInt(preferencesKey: PreferenceKeys, value: Int) {
        val editor = sharedPreferences!!.edit()
        editor.putInt(preferencesKey.toString(), value)
        editor.apply()
    }

    /**
     * This method is used for get String value from shared preference
     * @param PreferenceKeys is unik key.
     */
    fun getValue(key: PreferenceKeys): String? {
        return sharedPreferences!!.getString(key.toString(), "")
    }

    /**
     * This method is used for get Int value from shared preference
     * @param PreferenceKeys is unik key.
     */
    fun getInt(key: PreferenceKeys): Int {
        return sharedPreferences!!.getInt(key.toString(), 0)
    }

    /**
     * This method is used for get Boolean value from shared preference
     * @param PreferenceKeys is unik key.
     */
    fun getBoolean(key: PreferenceKeys): Boolean {
        return sharedPreferences!!.getBoolean(key.toString(), false)
    }

    /**
     * Clear SharedPreference.
     */
    fun clearSharedPreference() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * This method is used for convert Object into Gson.
     *
     * @param PreferenceKeys is unik key,
     * @param Any holds any type of object.
     */
    open fun putByGSON(preferencesKey: PreferenceKeys?, `object`: Any?) {
        val editor = sharedPreferences!!.edit()
        val gson = Gson()
        val json = gson.toJson(`object`)
        editor.putString(preferencesKey.toString(), json)
        editor.commit()
        editor.apply()

    }

    /**
     *
     * Return ArrayList from shared preference using @param "PreferenceKeys"
     *
     */
    fun getArrayList(key: PreferenceKeys?): ArrayList<Any>? {
        val gson = Gson()
        val json: String? = sharedPreferences!!.getString(key.toString(), null)
        val type = object : TypeToken<ArrayList<Any?>?>() {}.type
        return gson.fromJson<ArrayList<Any>>(json, type)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        sharedPreferences!!.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences!!.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}
