package com.khs.nbbang.search

import com.khs.nbbang.base.BaseViewModel

class KakaoLocalViewModel(private val mLocalApi: KakaoLocalAPI) : BaseViewModel() {
    fun searchKeyword() {
        mLocalApi.searchKeyword("미아사거리 양꼬치")
    }
}