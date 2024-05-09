package com.example.historicallandmarkdonation.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.historicallandmarkdonation.R
import com.example.historicallandmarkdonation.adapters.DonationAdapter
import com.example.historicallandmarkdonation.adapters.DonationClickListener
import com.example.historicallandmarkdonation.databinding.FragmentReportBinding
import com.example.historicallandmarkdonation.models.DonationModel
import com.example.historicallandmarkdonation.ui.auth.LoggedInViewModel
import com.example.historicallandmarkdonation.utils.SwipeToDeleteCallback
import com.example.historicallandmarkdonation.utils.SwipeToEditCallback
import com.example.historicallandmarkdonation.utils.createLoader
import com.example.historicallandmarkdonation.utils.hideLoader
import com.example.historicallandmarkdonation.utils.showLoader

class ReportFragment : Fragment(), DonationClickListener {

    private var _fragBinding: FragmentReportBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var donationAdapter: DonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToDonateFragment()
            findNavController().navigate(action)
        }
        showLoader(loader,"Downloading Donations")
        reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                donations ->
            donations?.let {
                render(donations as ArrayList<DonationModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Donation")
                val adapter = fragBinding.recyclerView.adapter as DonationAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                reportViewModel.delete(reportViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as DonationModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDonationClick(viewHolder.itemView.tag as DonationModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        donationAdapter = DonationAdapter(ArrayList(), this, reportViewModel.readOnly.value!!)

        render(ArrayList())

        return root
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

                //Added search functionality
                val searchItem = menu.findItem(R.id.app_bar_search)
                val searchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        donationAdapter.filter(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(donationsList: ArrayList<DonationModel>) {
        donationAdapter = DonationAdapter(donationsList,this, reportViewModel.readOnly.value!!)
        fragBinding.recyclerView.adapter = donationAdapter
        if (donationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.donationsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onDonationClick(donation: DonationModel) {
        val action = ReportFragmentDirections.actionReportFragmentToDonationDetailFragment(donation.uid!!)
        if(!reportViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Donations")
            if(reportViewModel.readOnly.value!!)
                reportViewModel.loadAll()
            else
                reportViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Donations")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                reportViewModel.liveFirebaseUser.value = firebaseUser
                reportViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyItemAnimations()
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