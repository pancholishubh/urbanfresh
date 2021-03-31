package com.urbanfresh.data.local

enum class PreferenceKeys(val key: String) {
    //Global values
    ACCESS_TOKEN("ACCESS_TOKEN"),
    DEVICE_ID("DEVICE_ID"),
    APP_LANGUAGE("APP_LANGUAGE"),
    LANGUAGE_CODE("LANGUAGE_CODE"),
    TO_USER_ID_CHAT("TO_USER_ID_CHAT"),
    PROFILE_DATA("PROFILE_DATA"),
    CHAT_USERJID("USERJID"),
    CHAT_CLINIC_ID("CLINIC_ID");
    override fun toString(): String {
        return key
    }
}
