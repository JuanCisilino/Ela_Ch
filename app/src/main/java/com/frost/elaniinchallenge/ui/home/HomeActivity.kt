package com.frost.elaniinchallenge.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.databinding.ActivityHomeBinding
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.ui.adapters.PokemonAdapter
import com.frost.elaniinchallenge.ui.adapters.TeamListAdapter
import com.frost.elaniinchallenge.ui.add.AddActivity
import com.frost.elaniinchallenge.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: TeamListAdapter
    private lateinit var pokemonAdapter: PokemonAdapter

    private val viewModel by viewModels<HomeViewModel>()

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setValues(getPairPref())
        setAdapter()
        setTeamAdapter()
        setButtons()
        subscribeToLiveData()
    }

    private fun setAdapter() {
        adapter = TeamListAdapter(this)
        binding.itemsrecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemsrecyclerView.adapter = adapter
    }

    private fun setTeamAdapter() {
        pokemonAdapter = PokemonAdapter(this, true)
        binding.teamRecyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.teamRecyclerView.adapter = pokemonAdapter
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.errorLiveData.observe(this) { showToast(it) }
        viewModel.teamsLiveData.observe(this) { handleList(it) }
        viewModel.selectedTeamLiveData.observe(this) { handleSelectedTeam(it)}
        viewModel.saveLiveData.observe(this) { showTeamsLayout() }
    }

    private fun handleResponse(loadState: LoadState) {
        when(loadState){
            LoadState.Loading -> hideGridLayout()
            LoadState.Success -> showTeamsLayout()
            else -> showToast(getString(R.string.error))
        }
    }

    private fun showTeamsLayout() {
        with(binding){
            gridLayout.visibility = View.GONE
            teamLayout.visibility = View.GONE
            codeLayout.visibility = View.GONE
            showCreated.visibility = View.VISIBLE
            zonesLayout.visibility = View.VISIBLE
            itemsrecyclerView.visibility = View.VISIBLE
        }
    }

    private fun hideGridLayout() {
        with(binding){
            gridLayout.visibility = View.VISIBLE
            itemsrecyclerView.visibility = View.GONE
        }
    }

    private fun handleList(list: List<Team>) {
        adapter.updateItems(list)
        adapter.onTeamClickCallback = { viewModel.createTeamToShow(it) }
        adapter.onRemoveTeamClickCallback = { viewModel.removeTeam(it) }
    }

    private fun handleSelectedTeam(pair: Pair<String, List<Pokemon>>) {
        showTeam(pair.first)
        pokemonAdapter.updateItems(pair.second)
    }

    private fun showTeam(teamName: String) {
        with(binding){
            zonesLayout.visibility = View.GONE
            teamLayout.visibility = View.VISIBLE
            teamNameTextView.text = teamName
        }
    }

    private fun setButtons() {
        with(binding){
            createButton.setOnClickListener { AddActivity.start(this@HomeActivity) }
            showCreated.setOnClickListener { showCodeInsert() }
            shareButton.setOnClickListener { showId(viewModel.selectedTeam?.id?:0) }
            deleteButton.setOnClickListener { viewModel.removeTeam(viewModel.selectedTeam!!) }
        }
    }

    private fun showCodeInsert() {
        with(binding){
            showCreated.visibility = View.GONE
            codeLayout.visibility = View.VISIBLE
            codeEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) { checkIfFocus(view) }
            }
            codeEditText.setOnEditorActionListener { view, actionId, _ -> onActionDone(actionId, view) }
            saveButton.setOnClickListener {
                if (!codeEditText.text.isNullOrBlank()) viewModel.save(codeEditText.text.toString())
            }
        }
    }

    private fun onActionDone(actionId: Int, view: TextView) =
        if (actionId == EditorInfo.IME_ACTION_DONE){
            checkIfFocus(view)
            true
        } else {
            false
        }

    private fun checkIfFocus(view: View) {
        if (!binding.codeEditText.text.isNullOrEmpty()) {
            hideKeyboard(view)
        }
    }

    override fun onBackPressed() {
        if (binding.teamLayout.isVisible){
            showTeamsLayout()
        } else {
            super.onBackPressed()
            viewModel.signOut()
            clearPrefs()
            finish()
        }
    }
}