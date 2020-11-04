package com.khs.nbbang.page

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.TalkApiClient
import com.khs.nbbang.base.BaseViewModel

class SelectPageViewModel : BaseViewModel() {
    fun selectCategory() {

    }

    fun selectFriends() {

    }

    fun requestKakaoMyProfile() {
        // 카카오톡 프로필 가져오기
        TalkApiClient.instance.profile { profile, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 프로필 가져오기 실패", error)
            }
            else if (profile != null) {
                Log.i(TAG, "카카오톡 프로필 가져오기 성공" +
                        "\n닉네임: ${profile.nickname}" +
                        "\n프로필사진: ${profile.thumbnailUrl}" +
                        "\n국가코드: ${profile.countryISO}")
            }
        }
    }

    fun requestKakaoFriendsList() {
        // 카카오톡 친구 목록 가져오기 (기본)
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
            }
            else if (friends != null) {
                Log.i(TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends.elements.joinToString("\n")}")

                // 친구의 UUID 로 메시지 보내기 가능
            }
        }
    }
}