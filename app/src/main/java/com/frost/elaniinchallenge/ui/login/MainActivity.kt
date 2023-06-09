package com.frost.elaniinchallenge.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.databinding.ActivityMainBinding
import com.frost.elaniinchallenge.ui.home.HomeActivity
import com.frost.elaniinchallenge.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    companion object{
        const val GOOGLE_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate()
        binding.loginButton.setOnClickListener { setWidget() }
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.regionListLiveData.observe(this) { handleRegion(it) }
        viewModel.loginLiveData.observe(this) { hideLayout() }
        viewModel.errorLiveData.observe(this) { showToast(it) }
    }

    private fun handleRegion(regionList: List<Region>) {
        val adapter = RegionAdapter(regionList, this)
        binding.itemsrecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.itemsrecyclerView.adapter = adapter
        adapter.onRegionClickCallback = { goToNextActivity(it) }

    }

    private fun goToNextActivity(region: String) {
        saveRegionPref(region)
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
            else -> showToast("viajo un error")
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
                    viewModel.signIn(GoogleAuthProvider.getCredential(account.idToken, null))
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }
}