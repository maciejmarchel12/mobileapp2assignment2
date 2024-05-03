package com.example.historicallandmarkdonation.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.historicallandmarkdonation.models.DonationManager
import com.example.historicallandmarkdonation.models.DonationModel

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDonation(donation: DonationModel) {
        status.value = try {
            DonationManager.create(donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}