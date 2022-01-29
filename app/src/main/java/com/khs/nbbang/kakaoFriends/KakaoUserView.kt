package com.khs.nbbang.kakaoFriends

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.network.RxAuthOperations
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.utils.LogUtil
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface KakaoUserView {
    val compositeDisposable: CompositeDisposable

    fun renderKakaoMembers(kakaoFirends: ArrayList<KaKaoMember>){}
    fun requestResult(resultCode : Int, result : Any?){}

    fun handleLogin(context : Context, sub: Scheduler, ob: Scheduler) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        val d = Single.just(UserApiClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available) UserApiClient.rx.loginWithKakaoTalk(context)
                else UserApiClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(ob)
            .subscribe({ token ->
                LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"로그인 성공 ${token.accessToken}")
                requestResult(ReturnType().RETURN_TYPE_LOGIN_SUCCESS, token)
            }, { error ->
                LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"로그인 실패 $error")
                requestResult(ReturnType().RETURN_TYPE_LOGIN_FAILED, error)
            })
        // 카카오톡 로그인
        compositeDisposable.add(d)
    }

    fun handleLogout(sub: Scheduler, ob: Scheduler) {
        val d= UserApiClient.rx.logout()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe({
                LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"로그아웃 성공. SDK에서 토큰 삭제 됨")
                requestResult(resultCode = ReturnType().RETURN_TYPE_LOGOUT_SUCCESS, result =  null)
            }, { error ->
                LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"로그아웃 실패. SDK에서 토큰 삭제 됨 $error")
                requestResult(ReturnType().RETURN_TYPE_NONE_FAILED, error)
            })
        compositeDisposable.add(d)
    }

    fun handleDisconnectLoginSession(sub: Scheduler, ob: Scheduler) {
        val d = UserApiClient.rx.unlink()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe({
                LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"연결 끊기 성공. SDK에서 토큰 삭제 됨")
                requestResult(resultCode = ReturnType().RETURN_TYPE_NONE_SUCCESS, result = null)
            }, { error ->
                LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"연결 끊기 실패 $error")
                requestResult(ReturnType().RETURN_TYPE_NONE_FAILED, error)
            })
        compositeDisposable.add(d)
    }

    fun handleMyInfoUpdate(sub: Scheduler, ob: Scheduler) {
        val d = UserApiClient.rx.me()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe({ myInfo ->
                requestResult(resultCode = ReturnType().RETURN_TYPE_MY_INFO_SUCCESS, result = myInfo)
            }, { error ->
                LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"내 프로필 가지고 오기 실패 $error")
                requestResult(resultCode = ReturnType().RETURN_TYPE_NONE_FAILED, result = error)
            })
        compositeDisposable.add(d)
    }

    fun handleLoadMyProfileInfo(sub: Scheduler, ob: Scheduler) {
        // 카카오톡 프로필 가져오기
        val d = TalkApiClient.rx.profile()
                .subscribeOn(sub)
                .observeOn(ob)
                .subscribe({ profile ->
                    LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"카카오톡 프로필 가져오기 성공" +
                            "\n닉네임: ${profile.nickname}" +
                            "\n프로필사진: ${profile.thumbnailUrl}" +
                            "\n국가코드: ${profile.countryISO}")
                    requestResult(resultCode = ReturnType().RETURN_TYPE_PROFILE_SUCCESS, result = profile)
                }, { error ->
                    LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"카카오톡 프로필 가져오기 실패 $error")
                    requestResult(resultCode = ReturnType().RETURN_TYPE_NONE_FAILED, result = error)
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
                    val kakaoFriendList = arrayListOf<Friend>()
                    for (friend in it.elements!!) {
                        kakaoFriendList.add(friend)
                    }
                    kakaoFriendList.map {
                        KaKaoMember(
                            id = it.id!!,
                            profileNickname = it.profileNickname!!,
                            uuId = it.uuid,
                            thumbnailImage = it.profileThumbnailImage,
                            isFavoriteByKakao = if (it.favorite!!) 1 else 0
                        )
                    }
                }
                .subscribeOn(sub)
                .observeOn(ob)
                .subscribe({ friends ->
                    LogUtil.iLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"카카오톡 친구 목록 가져오기 성공 \n${friends.joinToString("\n")}")
                    // 친구의 UUID 로 메시지 보내기 가능
                    renderKakaoMembers(ArrayList(friends))
                }, { error ->
                    LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"카카오톡 친구 목록 가져오기 실패 $error")
                    requestResult(resultCode = ReturnType().RETURN_TYPE_NONE_FAILED, result = error)
                })
        compositeDisposable.add(d)
    }

    fun handleCheckHasToken(context: Context, sub: Scheduler, ob: Scheduler) {
        if (AuthApiClient.instance.hasToken()) {
            val d = UserApiClient.rx.accessTokenInfo()
                .subscribeOn(sub)
                .observeOn(ob)
                .subscribe({ tokenInfo ->
                    LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"handleCheckHasToken, onSuccess :: tokenInfo : $tokenInfo ")
                    requestResult(ReturnType().RETURN_TYPE_CHECK_TOKEN_SUCCESS, tokenInfo)
                }, { error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {
                            LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"handleCheckHasToken error is SdkError")
                            requestResult(ReturnType().RETURN_TYPE_CHECK_TOKEN_FAILED, error)
                        }
                        else {
                            LogUtil.vLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName,"handleCheckHasToken error is not SdkError")
                            requestResult(ReturnType().RETURN_TYPE_CHECK_TOKEN_FAILED, error)
                        }
                    }
                })
            compositeDisposable.add(d)
        }
        else {
            requestResult(ReturnType().RETURN_TYPE_CHECK_TOKEN_FAILED, null)
        }
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}