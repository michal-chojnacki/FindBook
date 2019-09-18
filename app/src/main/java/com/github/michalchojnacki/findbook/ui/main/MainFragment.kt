package com.github.michalchojnacki.findbook.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.MainFragmentBinding
import com.github.michalchojnacki.findbook.ui.booklist.BookListFragment
import com.github.michalchojnacki.findbook.ui.camera.OcrCaptureActivity
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.util.exhaustive
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainFragmentBinding.bind(view).apply {
            lifecycleOwner = this@MainFragment
        }.viewModel = viewModel
        viewModel.uiResultLiveData.observe(this, EventObserver {
            when (it) {
                is MainViewModel.UiResult.ShowOcrScanner -> {
                    startActivity(Intent(context, OcrCaptureActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                }
                is MainViewModel.UiResult.ShowBookList -> {
                    fragmentManager?.commit {
                        addToBackStack(null)
                        replace(R.id.container, BookListFragment.newInstance(it.query))
                    }
                }
            }.exhaustive
        })
    }
}
