package com.frost.elaniinchallenge.models

import androidx.recyclerview.widget.DiffUtil

data class Team(
    val id: Int,
    val name: String,
    val email: String,
    val pokemonIds: String
): DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem == newItem
    }
}

