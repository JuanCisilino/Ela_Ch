package com.frost.elaniinchallenge.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun Activity.showId(id: Int){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(id.toString())
    builder.setMessage(getString(R.string.code))
    builder.setPositiveButton(R.string.ok, null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.getPref(): SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

fun Activity.savePref(region: String, email: String){
    val prefs = getPref().edit()
    prefs.putString(R.string.region.toString(), region)
    prefs.putString(R.string.email.toString(), email)
    prefs.apply()
}

fun Activity.getPairPref(): Pair<String, String>{
    val prefs = getPref()
    val region = prefs.getString(R.string.region.toString(), null)
    val email = prefs.getString(R.string.email.toString(), null)
    return Pair(region?:"", email?:"")
}

fun Activity.clearPrefs(){
    val prefs = getPref()
    prefs.edit()?.clear()?.apply()
}

fun Activity.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}