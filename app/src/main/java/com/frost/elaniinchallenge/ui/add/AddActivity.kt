package com.frost.elaniinchallenge.ui.add

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.databinding.ActivityAddBinding
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.ui.adapters.PokemonAdapter
import com.frost.elaniinchallenge.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var adapter: PokemonAdapter
    private val viewModel by viewModels<AddViewModel>()

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AddActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setPair(getPairPref())
        setupAdapter()
        setEditableComponents()
        subscribeToLiveData()
    }

    private fun setupAdapter() {
        adapter = PokemonAdapter(this)
        binding.pokemonrecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.pokemonrecyclerView.adapter = adapter
    }

    private fun setEditableComponents() {
        with(binding){
            teamNameEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) { checkIfFocus(view) }
            }
            teamNameEditText.setOnEditorActionListener { view, actionId, _ -> onActionDone(actionId, view) }
            saveButton.setOnClickListener {
                if (isInternetAvailable()) viewModel.save(teamNameEditText.text.toString())
                else showToast(getString(R.string.no_internet))  }
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
        if (!binding.teamNameEditText.text.isNullOrEmpty()) {
            viewModel.checkIfValid()
            hideKeyboard(view)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.pokemonListLiveData.observe(this) { handleList(it) }
        viewModel.errorLiveData.observe(this) { showToast(it) }
        viewModel.teamLiveData.observe(this) {
            if (!binding.teamNameEditText.text.isNullOrEmpty()) binding.saveButton.isEnabled = true
        }
        viewModel.saveLiveData.observe(this) { finish() }
    }

    private fun handleResponse(loadState: LoadState) {
        when(loadState){
            LoadState.Loading -> showShimmer()
            LoadState.Success -> hideShimmer()
            else -> showToast(getString(R.string.error))
        }
    }

    private fun hideShimmer() {
        with(binding){
            gridLayout.visibility = View.GONE
            pokemonrecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showShimmer() {
        with(binding){
            gridLayout.visibility = View.VISIBLE
            pokemonrecyclerView.visibility = View.GONE
        }
    }

    private fun handleList(list: List<Pokemon>) {
        adapter.updateItems(list)
        adapter.onPokemonAddedClickCallback = { viewModel.addToSelected(it) }
        adapter.onPokemonRemovedClickCallback = { viewModel.removeFromSelected(it) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}