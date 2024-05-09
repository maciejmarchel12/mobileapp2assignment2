package com.example.historicallandmarkdonation.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.historicallandmarkdonation.R
import com.example.historicallandmarkdonation.models.DonationModel
import com.example.historicallandmarkdonation.ui.auth.LoggedInViewModel
import com.example.historicallandmarkdonation.ui.report.ReportViewModel
import com.example.historicallandmarkdonation.utils.createLoader
import com.example.historicallandmarkdonation.utils.hideLoader
import com.example.historicallandmarkdonation.utils.showLoader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            reportViewModel.observableDonationsList.observe(
                viewLifecycleOwner,
                Observer { donations ->
                    donations?.let {
                        render(donations as ArrayList<DonationModel>)
                        hideLoader(loader)
                    }
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        applyItemAnimations()
    }

    private fun render(donationsList: ArrayList<DonationModel>) {
        var markerColour: Float
        if (donationsList.isNotEmpty()) {
            mapsViewModel.map.clear()
            donationsList.forEach {
                markerColour = if(it.email.equals(this.reportViewModel.liveFirebaseUser.value!!.email))
                    BitmapDescriptorFactory.HUE_AZURE + 5
                else
                    BitmapDescriptorFactory.HUE_RED

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.paymentmethod} €${it.amount}")
                        .snippet(it.message)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour ))
                )        }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Donations")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) {
                firebaseUser -> if (firebaseUser != null) {
            reportViewModel.liveFirebaseUser.value = firebaseUser
            reportViewModel.load()
        }        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_report, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) reportViewModel.loadAll()
                    else reportViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun applyItemAnimations() {
        // Sets the custom animations for fragment transactions
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)

        // Replaces the current fragment with itself (no actual change, but it triggers the animation)
        transaction.replace(id, this)

        // Adds the transaction to the back stack
        transaction.addToBackStack(null)

        // Commits the transaction
        transaction.commit()
    }
}