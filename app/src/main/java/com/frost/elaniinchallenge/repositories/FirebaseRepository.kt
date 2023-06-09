package com.frost.elaniinchallenge.repositories

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val dbInstance: FirebaseDatabase) {

    private val teamReference = dbInstance.getReference("Teams")
}