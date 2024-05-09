package com.example.historicallandmarkdonation.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.historicallandmarkdonation.R

class AboutFragment : Fragment() {

    private lateinit var aboutViewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutViewModel =
            ViewModelProvider(this).get(AboutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        aboutViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
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