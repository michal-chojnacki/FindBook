package com.github.michalchojnacki.findbook.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.ui.camera.OcrCaptureActivity
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModel<MainViewModel>().value
        mainSearchWithTextBtn.setOnClickListener {
            mainSearchWithTextEt.text.toString().takeIf { it.isNotBlank() }?.let { viewModel.searchForBook(it) }
        }
        mainSearchWithOcrBtn.setOnClickListener {
            startActivity(Intent(context, OcrCaptureActivity::class.java))
        }
    }
}
