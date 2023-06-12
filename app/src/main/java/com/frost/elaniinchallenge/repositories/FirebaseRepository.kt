package com.frost.elaniinchallenge.repositories

import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FirebaseRepository @Inject constructor(private val dbInstance: FirebaseDatabase) {

    private val teamReference = dbInstance.getReference("teams")

    fun saveTeam(team: Team) {
        teamReference.child("${team.id}").setValue(team)
    }

    fun getTeams(): ResponseData{
        return ResponseData(teamReference = teamReference)
    }

    fun removeTeam(teamId: Int): ResponseData {
        teamReference.child("$teamId").removeValue()
        return ResponseData(instance = dbInstance)
    }
}