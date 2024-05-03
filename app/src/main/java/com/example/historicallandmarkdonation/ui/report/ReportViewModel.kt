package com.example.historicallandmarkdonation.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.historicallandmarkdonation.models.DonationManager
import com.example.historicallandmarkdonation.models.DonationModel

class ReportViewModel : ViewModel() {

    private val donationsList = MutableLiveData<List<DonationModel>>()

    val observableDonationsList: LiveData<List<DonationModel>>
        get() = donationsList

    init {
        load()
    }

    fun load() {
        donationsList.value = DonationManager.findAll()
    }
}