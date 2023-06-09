package com.frost.elaniinchallenge.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.frost.elaniinchallenge.databinding.ActivityHomeBinding
import com.frost.elaniinchallenge.utils.getRegionPref
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
        binding.homeText.text = getRegionPref()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.signOut()
        finish()
    }
}