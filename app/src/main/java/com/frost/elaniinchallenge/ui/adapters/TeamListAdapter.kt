package com.frost.elaniinchallenge.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frost.elaniinchallenge.databinding.TeamItemBinding
import com.frost.elaniinchallenge.models.Team

class TeamListAdapter(private val context: Context) : RecyclerView.Adapter<TeamListAdapter.ViewHolder>() {

    private var teamList = listOf<Team>()
    var onTeamClickCallback : ((team: Team) -> Unit)? = null
    var onRemoveTeamClickCallback : ((team: Team) -> Unit)? = null

    fun updateItems(newItems: List<Team>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = teamList.size

            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                teamList[oldItemPosition].id == newItems[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                teamList[oldItemPosition] == newItems[newItemPosition]

        })
        teamList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: TeamItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(teamList[position]) {
                val team = this
                with(binding) {
                    titleTextView.text = name
                    teamLayout.setOnClickListener { onTeamClickCallback?.invoke(team) }
                    deleteImage.setOnClickListener { onRemoveTeamClickCallback?.invoke(team) }
                }
            }
        }
    }

    override fun getItemCount() = teamList.size
}