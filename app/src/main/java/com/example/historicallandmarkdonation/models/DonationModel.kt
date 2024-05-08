package com.example.historicallandmarkdonation.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class DonationModel(
    var uid: String? = "",
    var paymentmethod: String = "N/A",
    var amount: Int = 0,
    var message: String = "a message",
    var upvotes: Int = 0,
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "paymentmethod" to paymentmethod,
            "amount" to amount,
            "message" to message,
            "upvotes" to upvotes,
            "email" to email
        )
    }
}