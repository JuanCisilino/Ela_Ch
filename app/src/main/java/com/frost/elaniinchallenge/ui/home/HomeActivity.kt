package com.frost.elaniinchallenge.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.databinding.ActivityHomeBinding
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.models.Team
import com.frost.elaniinchallenge.ui.addedit.AddEditActivity
import com.frost.elaniinchallenge.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: TeamsAdapter
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
        setButtons()
        subscribeToLiveData()
    }

    private fun setAdapter() {
        adapter = TeamsAdapter(this)
        binding.itemsrecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemsrecyclerView.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.errorLiveData.observe(this) { showToast(it) }
        viewModel.teamsLiveData.observe(this) { handleList(it) }
    }

    private fun handleResponse(loadState: LoadState) {
        when(loadState){
            LoadState.Loading -> showLayout()
            LoadState.Success -> hideLayout()
            else -> showToast(getString(R.string.error))
        }
    }

    private fun hideLayout() {
        with(binding){
            gridLayout.visibility = View.GONE
            itemsrecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showLayout() {
        with(binding){
            gridLayout.visibility = View.VISIBLE
            itemsrecyclerView.visibility = View.GONE
        }
    }

    private fun handleList(list: List<Team>) {
        adapter.updateItems(list)
        adapter.onTeamClickCallback = {  }
        adapter.onRemoveTeamClickCallback = { viewModel.removeTeam(it) }
    }

    private fun setButtons() {
        with(binding){
            createButton.setOnClickListener { AddEditActivity.start(this@HomeActivity) }
            showCreated.setOnClickListener { viewModel.getCreated() }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.signOut()
        clearPrefs()
        finish()
    }
}