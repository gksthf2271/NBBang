package com.khs.nbbang.kakaoFriends

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.network.RxAuthOperations
import com.kakao.sdk.auth.rx
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.khs.nbbang.kakaoFriends.kakao_interface.ReturnType
import com.khs.nbbang.user.KaKaoMember
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface KakaoView {
    val compositeDisposable: CompositeDisposable

    fun renderKakaoMembers(kakaoFirends: ArrayList<KaKaoMember>)
    fun requestResult(resultCode : Int, result : Any?)

    fun handleLogin(context : Context, sub: Scheduler, ob: Scheduler) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        val d = Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(context)
                else LoginClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(ob)
            .subscribe({ token ->
                Log.i("KakaoView", "로그인 성공 ${token.accessToken}")
                requestResult(ReturnType().RETURN_TYPE_SUCCESS, token)
            }, { error ->
                Log.e("KakaoView", "로그인 실패", error)
                requestResult(ReturnType().RETURN_TYPE_FAILED, error)
            })
        // 카카오톡 로그인
        compositeDisposable.add(d)
    }

    fun handleLogout(sub: Scheduler, ob: Scheduler) {
        val d= UserApiClient.rx.logout()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe({
                Log.i("KakaoView", "로그아웃 성공. SDK에서 토큰 삭제 됨")
                requestResult(resultCode = ReturnType().RETURN_TYPE_SUCCESS, result =  null)
            }, { error ->
                Log.e("KakaoView", "로그아웃 실패. SDK에서 토큰 삭제 됨", error)
                requestResult(ReturnType().RETURN_TYPE_FAILED, error)
            })
        compositeDisposable.add(d)
    }

    fun handleDisconnectLoginSession(sub: Scheduler, ob: Scheduler) {
        val d = UserApiClient.rx.unlink()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe({
                Log.i("KakaoView", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                requestResult(resultCode = ReturnType().RETURN_TYPE_SUCCESS, result = null)
            }, { error ->
                Log.e("KakaoView", "연결 끊기 실패", error)
                requestResult(ReturnType().RETURN_TYPE_FAILED, error)
            })
        compositeDisposable.add(d)
    }

    fun handleLoadMyProfileInfo(sub: Scheduler, ob: Scheduler) {
        // 카카오톡 프로필 가져오기
        val d = TalkApiClient.rx.profile()
                .subscribeOn(sub)
                .observeOn(ob)
                .subscribe({ profile ->
                    Log.i("KakaoView", "카카오톡 프로필 가져오기 성공" +
                            "\n닉네임: ${profile.nickname}" +
                            "\n프로필사진: ${profile.thumbnailUrl}" +
                            "\n국가코드: ${profile.countryISO}")
                    requestResult(resultCode = ReturnType().RETURN_TYPE_SUCCESS, result = profile)
                }, { error ->
                    Log.e("KakaoView", "카카오톡 프로필 가져오기 실패", error)
                    requestResult(resultCode = ReturnType().RETURN_TYPE_FAILED, result = error)
                })
        compositeDisposable.add(d)
    }

    fun handleLoadKakaoFriendList(context: Context, sub: Scheduler, ob: Scheduler) {
        // 카카오톡 친구 목록 가져오기 (기본)
        val d = TalkApiClient.rx.friends()
                .retryWhen(
                    // InsufficientScope 에러에 대해 추가 동의 후 재요청
                    RxAuthOperations.instance.incrementalAuthorizationRequired(context)
                )
                .map { it ->
                    var kakaoFriendList = arrayListOf<Friend>()
                    for (friend in it.elements) {
                        kakaoFriendList.add(friend)
                    }
                    kakaoFriendList.map {
                        KaKaoMember(
                            id = it.id,
                            profileNickname = it.profileNickname,
                            uuId = it.uuid,
                            thumbnailImage = it.profileThumbnailImage,
                            isFavoriteByKakao = it.favorite
                        )
                    }
                }
                .subscribeOn(sub)
                .observeOn(ob)
                .subscribe({ friends ->
                    Log.i("KakaoView", "카카오톡 친구 목록 가져오기 성공 \n${friends.joinToString("\n")}")
                    // 친구의 UUID 로 메시지 보내기 가능
                    renderKakaoMembers(ArrayList(friends))
                }, { error ->
                    Log.e("KakaoView", "카카오톡 친구 목록 가져오기 실패", error)
                    requestResult(resultCode = ReturnType().RETURN_TYPE_FAILED, result = error)
                })
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}