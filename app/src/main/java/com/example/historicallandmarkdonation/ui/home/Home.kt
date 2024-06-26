package com.example.historicallandmarkdonation.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.historicallandmarkdonation.R
import com.example.historicallandmarkdonation.databinding.HomeBinding
import com.example.historicallandmarkdonation.databinding.NavHeaderBinding
import com.example.historicallandmarkdonation.firebase.FirebaseImageManager
import com.example.historicallandmarkdonation.ui.auth.LoggedInViewModel
import com.example.historicallandmarkdonation.ui.auth.Login
import com.example.historicallandmarkdonation.ui.map.MapsViewModel
import com.example.historicallandmarkdonation.utils.checkLocationPermissions
import com.example.historicallandmarkdonation.utils.isPermissionGranted
import com.example.historicallandmarkdonation.utils.readImageUri
import com.example.historicallandmarkdonation.utils.showImagePicker
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var nightModeSwitch: Switch //for switching to night mode
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var headerView : View
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel : MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.donateFragment, R.id.reportFragment, R.id.mapsFragment, R.id.aboutFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()

        if (checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }
    }

        public override fun onStart() {
            super.onStart()
            loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
            loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
                if (firebaseUser != null)
                    updateNavHeader(firebaseUser)
            })

            loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
                if (loggedout) {
                    startActivity(Intent(this, Login::class.java))
                }
            })
            registerImagePickerCallback()
        }

        private fun updateNavHeader(currentUser: FirebaseUser) {
            FirebaseImageManager.imageUri.observe(this) { result ->
                if (result == Uri.EMPTY) {
                    Timber.i("DX NO Existing imageUri")
                    if (currentUser.photoUrl != null) {
                        //if you're a google user
                        FirebaseImageManager.updateUserImage(
                            currentUser.uid,
                            currentUser.photoUrl,
                            navHeaderBinding.navHeaderImage,
                            false
                        )
                    } else {
                        Timber.i("DX Loading Existing Default imageUri")
                        FirebaseImageManager.updateDefaultImage(
                            currentUser.uid,
                            R.drawable.ic_launcher_knight,
                            navHeaderBinding.navHeaderImage
                        )
                    }
                } else // load existing image from firebase
                {
                    Timber.i("DX Loading Existing imageUri")
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        FirebaseImageManager.imageUri.value,
                        navHeaderBinding.navHeaderImage, false
                    )
                }
            }
            navHeaderBinding.navHeaderEmail.text = currentUser.email
            if (currentUser.displayName != null)
                navHeaderBinding.navHeaderName.text = currentUser.displayName
        }

        override fun onSupportNavigateUp(): Boolean {
            val navController = findNavController(R.id.nav_host_fragment)
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }

        fun signOut(item: MenuItem) {
            loggedInViewModel.logOut()
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        private fun initNavHeader() {
            Timber.i("DX Init Nav Header")
            headerView = homeBinding.navView.getHeaderView(0)
            navHeaderBinding = NavHeaderBinding.bind(headerView)
            navHeaderBinding.navHeaderImage.setOnClickListener {
                showImagePicker(intentLauncher)
            }
        }

        private fun registerImagePickerCallback() {
            intentLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    when (result.resultCode) {
                        RESULT_OK -> {
                            if (result.data != null) {
                                Timber.i(
                                    "DX registerPickerCallback() ${
                                        readImageUri(
                                            result.resultCode,
                                            result.data
                                        ).toString()
                                    }"
                                )
                                FirebaseImageManager
                                    .updateUserImage(
                                        loggedInViewModel.liveFirebaseUser.value!!.uid,
                                        readImageUri(result.resultCode, result.data),
                                        navHeaderBinding.navHeaderImage,
                                        true
                                    )
                            } // end of if
                        }

                        RESULT_CANCELED -> {}
                        else -> {}
                    }
                }
        }

        @SuppressLint("MissingPermission")
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (isPermissionGranted(requestCode, grantResults))
                mapsViewModel.updateCurrentLocation()
            else {
                // permissions denied, so use a default location
                mapsViewModel.currentLocation.value = Location("Default").apply {
                    latitude = 52.245696
                    longitude = -7.139102
                }
            }
            Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
        }
}