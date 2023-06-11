package com.frost.elaniinchallenge.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.frost.elaniinchallenge.databinding.ActivityHomeBinding
import com.frost.elaniinchallenge.ui.addedit.AddEditActivity
import com.frost.elaniinchallenge.utils.clearPrefs
import com.frost.elaniinchallenge.utils.getPairPref
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
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
        setButtons()
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