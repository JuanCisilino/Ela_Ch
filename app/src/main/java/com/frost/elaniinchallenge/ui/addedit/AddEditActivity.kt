package com.frost.elaniinchallenge.ui.addedit

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
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.databinding.ActivityAddEditBinding
import com.frost.elaniinchallenge.models.Pokemon
import com.frost.elaniinchallenge.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var adapter: PokemonAdapter
    private val viewModel by viewModels<AddEditViewModel>()

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AddEditActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
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
            saveButton.setOnClickListener { viewModel.save(teamNameEditText.text.toString()) }
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
        viewModel.teamLiveData.observe(this) { binding.saveButton.isEnabled = true}
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