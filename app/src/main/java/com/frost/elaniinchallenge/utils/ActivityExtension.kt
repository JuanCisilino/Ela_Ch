package com.frost.elaniinchallenge.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.frost.elaniinchallenge.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun Activity.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.buildGoogleConfig() =
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

fun Activity.showAlert(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error))
    builder.setMessage(getString(R.string.error_message))
    builder.setPositiveButton(R.string.ok, null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.getPref(): SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

fun Activity.saveRegionPref(region: String){
    val prefs = getPref().edit()
    prefs.putString(R.string.region.toString(), region)
    prefs.apply()
}

fun Activity.getRegionPref(): String?{
    val prefs = getPref()
    return prefs.getString(R.string.region.toString(), null)
}