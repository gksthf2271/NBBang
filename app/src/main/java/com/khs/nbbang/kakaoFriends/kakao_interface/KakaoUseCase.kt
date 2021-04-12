package com.khs.nbbang.kakaoFriends.kakao_interface

import android.content.Context
import com.khs.nbbang.group.GetNBBangMemberResult
import io.reactivex.rxjava3.core.Single

interface Login : KakaoGateway {
    fun login(context : Context) = login(context) { token , error ->

    }

    fun reLogin()

    fun logout()

    fun disconnectLoginSession()
}

interface UpdateMyData : KakaoGateway {
    fun getNBBangAllMember(): Single<GetNBBangMemberResult> = getMembers().map {
        GetNBBangMemberResult(it)
    }

    fun getNBBangAllMemberByGroupId(groupId: Long): Single<GetNBBangMemberResult> =
        getMemberByGroupId(groupId).map {
            GetNBBangMemberResult(it)
        }
}

interface LoadInfo : KakaoGateway {
    fun loadMyProfileInfo () {

    }

    fun loadFirends() {

    }
}