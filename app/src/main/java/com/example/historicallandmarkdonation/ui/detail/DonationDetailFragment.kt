package com.example.historicallandmarkdonation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.historicallandmarkdonation.R
import com.example.historicallandmarkdonation.databinding.FragmentDonationDetailBinding
import com.example.historicallandmarkdonation.ui.auth.LoggedInViewModel
import com.example.historicallandmarkdonation.ui.report.ReportViewModel
import timber.log.Timber

class DonationDetailFragment : Fragment() {

    private lateinit var detailViewModel: DonationDetailViewModel
    private val args by navArgs<DonationDetailFragmentArgs>()
    private var _fragBinding: FragmentDonationDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(DonationDetailViewModel::class.java)
        detailViewModel.observableDonation.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editDonationButton.setOnClickListener {
            detailViewModel.updateDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.donationid, fragBinding.donationvm?.observableDonation!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteDonationButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableDonation.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
        fragBinding.editMessage.setText("A Message")
        fragBinding.editCause.setText("A Cause")
        fragBinding.editUpvotes.setText("0")
        fragBinding.donationvm = detailViewModel
        Timber.i("Retrofit fragBinding.donationvm == $fragBinding.donationvm")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.donationid)

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