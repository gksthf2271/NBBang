package com.khs.nbbang.freeUser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//ViewModel에 파라미터를 넘겨주기 위한 구현
class PageViewModelFactory(val viewModel: PageViewModel) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}