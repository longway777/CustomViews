package com.example.customviews.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.customviews.R
import kotlinx.android.synthetic.main.fragment_custom_draw.*


/**
 * A simple [Fragment] subclass.
 * Use the [CustomDrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomDrawFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_draw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(customDrawView)
    }
}