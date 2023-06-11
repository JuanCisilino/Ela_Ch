package com.frost.elaniinchallenge.ui.addedit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.pokemonListLiveData.observe(this) { handleList(it) }
        viewModel.errorLiveData.observe(this) { showToast(it) }
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
        val adapter = PokemonAdapter(this)
        binding.pokemonrecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.pokemonrecyclerView.adapter = adapter
        adapter.updateItems(list)
        adapter.onPokemonClickCallback = {  }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}