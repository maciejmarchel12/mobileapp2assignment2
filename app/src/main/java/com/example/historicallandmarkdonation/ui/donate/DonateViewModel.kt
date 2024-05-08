package com.example.historicallandmarkdonation.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.historicallandmarkdonation.firebase.FirebaseDBManager
import com.example.historicallandmarkdonation.models.DonationModel
import com.google.firebase.auth.FirebaseUser

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDonation(firebaseUser: MutableLiveData<FirebaseUser>,
                    donation: DonationModel) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}