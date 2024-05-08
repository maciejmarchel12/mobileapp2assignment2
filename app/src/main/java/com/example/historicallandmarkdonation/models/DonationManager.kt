package com.example.historicallandmarkdonation.models

import androidx.lifecycle.MutableLiveData
import com.example.historicallandmarkdonation.api.DonationClient
import com.example.historicallandmarkdonation.api.DonationWrapper
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object DonationManager : DonationStore {

    private var donations = ArrayList<DonationModel>()

    override fun findAll(donationsList: MutableLiveData<List<DonationModel>>) {

        val call = DonationClient.getApi().findall()

        call.enqueue(object : Callback<List<DonationModel>> {
            override fun onResponse(call: Call<List<DonationModel>>,
                                    response: Response<List<DonationModel>>
            ) {
                donationsList.value = response.body() as ArrayList<DonationModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<DonationModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findAll(uid: String, donationsList: MutableLiveData<List<DonationModel>>) {

        val call = DonationClient.getApi().findall(uid)

        call.enqueue(object : Callback<List<DonationModel>> {
            override fun onResponse(call: Call<List<DonationModel>>,
                                    response: Response<List<DonationModel>>
            ) {
                donationsList.value = response.body() as ArrayList<DonationModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<DonationModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findById(uid: String, id: String, donation: MutableLiveData<DonationModel>)   {

        val call = DonationClient.getApi().get(uid,id)

        call.enqueue(object : Callback<DonationModel> {
            override fun onResponse(call: Call<DonationModel>, response: Response<DonationModel>) {
                donation.value = response.body() as DonationModel
                Timber.i("Retrofit findById() = ${response.body()}")
            }

            override fun onFailure(call: Call<DonationModel>, t: Throwable) {
                Timber.i("Retrofit findById() Error : $t.message")
            }
        })
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, donation: DonationModel) {

        val call = DonationClient.getApi().post(donation.email,donation)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit ${donationWrapper.message}")
                    Timber.i("Retrofit ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }

    override fun delete(uid: String,id: String) {

        val call = DonationClient.getApi().delete(uid,id)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit Delete ${donationWrapper.message}")
                    Timber.i("Retrofit Delete ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Delete Error : $t.message")
            }
        })
    }

    override fun update(uid: String, id: String, donation: DonationModel) {

        val call = DonationClient.getApi().put(uid,id,donation)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit Update ${donationWrapper.message}")
                    Timber.i("Retrofit Update ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Update Error : $t.message")
            }
        })
    }
}