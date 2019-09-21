package com.github.michalchojnacki.findbook.ui.typingsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.TypingSearchFragmentBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.activityViewModel
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector
import com.github.michalchojnacki.findbook.ui.main.MainNavigationViewModel
import com.github.michalchojnacki.findbook.util.exhaustive

class TypingSearchFragment : BaseFragment() {

    companion object {
        fun newInstance() = TypingSearchFragment()
    }

    private val viewModel: TypingSearchViewModel by viewModel { injector.typingSearchViewModel }
    private val navigationViewModel: MainNavigationViewModel by activityViewModel { injector.mainNavigationViewModel }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.typing_search_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TypingSearchFragmentBinding.bind(view).apply {
            lifecycleOwner = this@TypingSearchFragment
        }.viewModel = viewModel
        viewModel.uiResultLiveData.observe(this, EventObserver {
            when (it) {
                is TypingSearchViewModel.UiResult.ShowOcrScanner -> {
                    navigationViewModel.showOcrScanner()
                }
                is TypingSearchViewModel.UiResult.ShowBookList -> {
                    navigationViewModel.showBookList(it.query)

                }
            }.exhaustive
        })
    }
}
