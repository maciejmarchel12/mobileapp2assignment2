package com.example.historicallandmarkdonation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.historicallandmarkdonation.models.DonationManager
import com.example.historicallandmarkdonation.models.DonationModel

class DonationDetailViewModel : ViewModel() {
    private val donation = MutableLiveData<DonationModel>()

    val observableDonation: LiveData<DonationModel>
        get() = donation

    fun getDonation(_id: String) {
        donation.value = DonationManager.findById(_id)
    }
}