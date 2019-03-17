package com.github.michalchojnacki.findbook.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onStart() {
        super.onStart()
        configureToolbar()
    }

    private fun configureToolbar() {
        val toolbar = (activity as? AppCompatActivity)?.supportActionBar
        if (toolbarTitle != null) {
            toolbar?.show()
            activity?.title = toolbarTitle
        } else {
            toolbar?.hide()
        }
        toolbar?.setDisplayHomeAsUpEnabled(backButtonVisible)
    }

    open val toolbarTitle: String? = null

    open val backButtonVisible: Boolean = false
}