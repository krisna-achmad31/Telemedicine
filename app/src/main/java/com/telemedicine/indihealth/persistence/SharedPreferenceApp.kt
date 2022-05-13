package com.telemedicine.indihealth.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.Assessment
import com.telemedicine.indihealth.model.User
import javax.inject.Inject

class SharedPreferenceApp @Inject constructor(
    private val mSharedPreferences: SharedPreferences,
    private val mGson: Gson
) {
    fun setUserValue(user: User?) =
        mSharedPreferences.edit(commit = true) {
            putString("loginData", mGson.toJson(user))
        }

    fun setBoolean(key: String?, boolean: Boolean) =
        mSharedPreferences.edit(commit = true) {
            putBoolean(key, boolean)
        }

    fun setString(key: String?, string: String) =
        mSharedPreferences.edit(commit = true) {
            putString(key, string)
        }

    fun getBoolean(key: String?): Boolean? {
        return mSharedPreferences.getBoolean(key, false)
    }

    fun getBooleanDefaultTrue(key: String?): Boolean? {
        return mSharedPreferences.getBoolean(key, true)
    }

    fun getUserValue(): User? {
        val mPref = mSharedPreferences.getString("loginData", "")
        return mGson.fromJson(mPref, User().javaClass)
    }


    fun getString(string: String): String? {
        return mSharedPreferences.getString(string, "")
    }

    fun clearSharedPreference() {
        mSharedPreferences.edit().apply {
            clear()
            apply()
        }
    }

    fun clearCallNotif() {
        mSharedPreferences.edit().apply {
            setBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST, false)
        }
    }

    fun setLoggedInStatus(status: Boolean) {
        mSharedPreferences.edit(commit = true) {
            putBoolean("loggedInStatus", status)
        }
    }

    fun getLoggedInStatus(): Boolean {
        return mSharedPreferences.getBoolean("loggedInStatus", false)
    }

    fun getAssessment(): Assessment? {
        val mPref = mSharedPreferences.getString("assessmentData", "")
        return mGson.fromJson(mPref, Assessment().javaClass)
    }

    fun setAssessment(assessment: Assessment) =
        mSharedPreferences.edit(commit = true) {
            putString("assessmentData", mGson.toJson(assessment))
        }

    fun getBoardingStatus(): Boolean {
        return mSharedPreferences.getBoolean("boardingStatus", false)
    }

    fun setBoardingStatus(status: Boolean) =
        mSharedPreferences.edit(commit = true) {
            putBoolean("boardingStatus", status)
        }

    fun getLogOutStatus(): Boolean {
        return mSharedPreferences.getBoolean("logOutStatus", false)
    }

    fun setLogOutStatus(status: Boolean) =
        mSharedPreferences.edit(commit = true) {
            putBoolean("logOutStatus", status)
        }
}
