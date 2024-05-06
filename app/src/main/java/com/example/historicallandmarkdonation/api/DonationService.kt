package com.example.historicallandmarkdonation.api

import com.example.historicallandmarkdonation.models.DonationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DonationService {
    @GET("/donations")
    fun findall(): Call<List<DonationModel>>

    @GET("/donations/{email}")
    fun findall(@Path("email") email: String?)
            : Call<List<DonationModel>>

    @GET("/donations/{email}/{id}")
    fun get(@Path("email") email: String?,
            @Path("id") id: String): Call<DonationModel>

    @DELETE("/donations/{email}/{id}")
    fun delete(@Path("email") email: String?,
               @Path("id") id: String): Call<DonationWrapper>

    @POST("/donations/{email}")
    fun post(@Path("email") email: String?,
             @Body donation: DonationModel)
            : Call<DonationWrapper>

    @PUT("/donations/{email}/{id}")
    fun put(@Path("email") email: String?,
            @Path("id") id: String,
            @Body donation: DonationModel
    ): Call<DonationWrapper>
}