package com.example.historicallandmarkdonation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.historicallandmarkdonation.R
import com.example.historicallandmarkdonation.databinding.CardDonationBinding
import com.example.historicallandmarkdonation.models.DonationModel
import com.example.historicallandmarkdonation.utils.customTransformation
import com.squareup.picasso.Picasso

interface DonationClickListener {
    fun onDonationClick(donation: DonationModel)
}

class DonationAdapter constructor(private var donations: ArrayList<DonationModel>,
                                  private val listener: DonationClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<DonationAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDonationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = donations[holder.adapterPosition]

        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_bottom)
        holder.itemView.startAnimation(animation)

        holder.bind(donation,listener)
    }

    override fun onViewDetachedFromWindow(holder: MainHolder) {
        super.onViewDetachedFromWindow(holder)
        // Resets the animation when view is detached
        holder.itemView.clearAnimation()
    }

    fun removeAt(position: Int) {
        donations.removeAt(position)
        notifyItemRemoved(position)
    }

    fun filter(query: String?) {
        query?.let { searchText ->
            val filteredList = if (searchText.isEmpty()) {
                ArrayList(donations) // returns original list if query is empty
            } else {
                donations.filter { donation ->
                    // Filter by email, amount, or payment method
                    donation.email?.contains(searchText, ignoreCase = true) ?: false ||
                            donation.amount?.toString()?.contains(searchText, ignoreCase = true) ?: false ||
                            donation.paymentmethod?.contains(searchText, ignoreCase = true) ?: false
                } as ArrayList<DonationModel>
            }
            donations.clear()
            donations.addAll(filteredList)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = donations.size

    inner class MainHolder(val binding : CardDonationBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(donation: DonationModel, listener: DonationClickListener) {
            binding.root.tag = donation
            binding.donation = donation
            Picasso.get().load(donation.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onDonationClick(donation) }
            binding.executePendingBindings()
        }
    }
}