package com.frost.elaniinchallenge.ui.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.databinding.ActivityLoginBinding
import com.frost.elaniinchallenge.ui.adapters.RegionAdapter
import com.frost.elaniinchallenge.ui.home.HomeActivity
import com.frost.elaniinchallenge.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mediaController: MediaController
    private val viewModel by viewModels<LoginViewModel>()

    companion object{
        const val GOOGLE_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaController = MediaController(this)
        viewModel.initDatabase()
        viewModel.onCreate()
        binding.loginButton.setOnClickListener {
            if (isInternetAvailable()) setWidget()
            else showToast(getString(R.string.video_message))
        }
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.regionListLiveData.observe(this) { handleRegion(it) }
        viewModel.databaseLiveData.observe(this) { handleDatabase(it)}
        viewModel.loginLiveData.observe(this) { hideLayout() }
        viewModel.errorLiveData.observe(this) { showToast(it) }
    }

    private fun setSplash() {
        with(binding){
            videoLayout.visibility = View.VISIBLE
            simpleVideoView.setMediaController(mediaController)
            simpleVideoView.setVideoURI(
                Uri.parse("android.resource://"
                    + packageName + "/" + R.raw.splash))
            simpleVideoView.requestFocus()
            simpleVideoView.start()
            simpleVideoView.setOnCompletionListener {
                videoLayout.visibility = View.GONE
                initialLayout.visibility = View.VISIBLE
            }
            simpleVideoView.setOnErrorListener { _, _, _ ->
                videoLayout.visibility = View.GONE
                initialLayout.visibility = View.VISIBLE
                showToast(getString(R.string.error))
                false
            }
        }
    }

    private fun handleDatabase(unit: Unit?) {
        unit?.let { showToast("lista armada") }
            ?:run {
                showToast(getString(R.string.video_message))
                setSplash()
            }
    }

    private fun handleRegion(regionList: List<Region>) {
        val adapter = RegionAdapter(regionList, this)
        binding.itemsrecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.itemsrecyclerView.adapter = adapter
        adapter.onRegionClickCallback = {
            if (isInternetAvailable()) goToNextActivity(it)
            else showToast(getString(R.string.no_internet))
        }
    }

    private fun goToNextActivity(region: String) {
        savePref(region, viewModel.mail)
        showLayout()
        HomeActivity.start(this)
    }

    private fun showLayout() {
        with(binding){
            linearLayout.visibility = View.VISIBLE
            zonesLayout.visibility = View.GONE
        }
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
            itemsrecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showShimmer() {
        with(binding){
            gridLayout.visibility = View.VISIBLE
            itemsrecyclerView.visibility = View.GONE
        }
    }

    private fun hideLayout() {
        with(binding){
            linearLayout.visibility = View.GONE
            zonesLayout.visibility = View.VISIBLE
        }
    }

    private fun setWidget() {
        val googleConfig = buildGoogleConfig()
        val googleClient = GoogleSignIn.getClient(this, googleConfig)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                account?.let { account ->
                    viewModel.signIn(
                        GoogleAuthProvider.getCredential(account.idToken, null),
                        account.email?:"")
                }
            }catch (e: ApiException){
                viewModel.signOut()
                showAlert()
            }
        }
    }

    override fun onDestroy() {
        viewModel.signOut()
        super.onDestroy()
    }
}