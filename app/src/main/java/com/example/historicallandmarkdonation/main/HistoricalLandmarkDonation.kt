package com.example.historicallandmarkdonation.main

import android.app.Application
import com.example.historicallandmarkdonation.models.DonationStore
import timber.log.Timber

class HistoricalLandmarkDonation : Application() {

    lateinit var donationsStore: DonationStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
      //  donationsStore = DonationMemStore()
        Timber.i("DonationX Application Started")
    }
}