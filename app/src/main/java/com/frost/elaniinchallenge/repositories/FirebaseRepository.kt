package com.frost.elaniinchallenge.repositories

import com.frost.elaniinchallenge.models.ResponseData
import com.frost.elaniinchallenge.models.Team
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val dbInstance: FirebaseDatabase) {

    private val teamReference = dbInstance.getReference("Teams")

    fun saveTeam(team: Team) {
        teamReference.setValue(team)
    }

    fun getTeams(email: String): ResponseData{
        var responseData = ResponseData()
        teamReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val teamList = mutableListOf<Team>()
                for (teamSnapshot in dataSnapshot.children) {
                    val id = teamSnapshot.child("id").value as Long
                    val name = teamSnapshot.child("name").value as String
                    val mail = teamSnapshot.child("email").value as String
                    val pokemonIds = teamSnapshot.child("pokemonIds").value as String
                    val team = Team(id.toInt(), name, mail, pokemonIds)
                    teamList.add(team)
                }
                responseData = ResponseData(teamList = teamList.filter { it.email == email } as ArrayList<Team>)
            }

            override fun onCancelled(error: DatabaseError) {
                responseData = ResponseData(errorMessage = "Error from db")
            }
        })
        return responseData
    }

    fun removeTeam(index: Int): ResponseData {
        var responseData = ResponseData()
        val teamRef = dbInstance.getReference("Teams/${index}")
        teamRef.removeValue()
            .addOnSuccessListener {
                responseData = ResponseData(teamRemoved = Unit)
            }
            .addOnFailureListener { error ->
                responseData = ResponseData(errorMessage = "Error from db")
            }
        return responseData
    }
}