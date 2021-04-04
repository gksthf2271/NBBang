//package com.khs.nbbang.kakaoFriends
//
//import com.khs.nbbang.group.GetNBBangMemberResult
//import io.reactivex.rxjava3.core.Scheduler
//import io.reactivex.rxjava3.disposables.CompositeDisposable
//
//interface KakaoView {
//
//    val compositeDisposable: CompositeDisposable
//
//    fun renderMembers(kakaoFirends: GetKakaoFirends)
//
//    fun handleLoadKakaoFirends(sub: Scheduler, ob: Scheduler) {
//        compositeDisposable.add(d)
//    }
//
//    fun handleLogin(sub: Scheduler, ob: Scheduler) {
//        val d = addNBBangMember(requestAddMember(member))
//            .flatMap { getNBBangAllMember() }
//            .subscribeOn(sub)
//            .observeOn(ob)
//            .subscribe { r ->
//                renderMembers(
//                    presentMember(r)
//                )
//            }
//        compositeDisposable.add(d)
//    }
//
//    fun handleLogout(sub: Scheduler, ob: Scheduler) {
//        compositeDisposable.add(d)
//    }
//
//    fun handleReLogin(sub: Scheduler, ob: Scheduler) {
//        compositeDisposable.add(d)
//    }
//
//    fun handleDisconnectLoginSession(sub: Scheduler, ob: Scheduler) {
//
//        compositeDisposable.add(d)
//    }
//
//    fun handleCheckLoginState(sub: Scheduler, ob: Scheduler) {
//
//        compositeDisposable.add(d)
//    }
//
//    fun handleUpdateMyData(sub: Scheduler, ob: Scheduler) {
//        compositeDisposable.add(d)
//    }
//
//    fun handleDestroy() {
//        compositeDisposable.dispose()
//    }
//}