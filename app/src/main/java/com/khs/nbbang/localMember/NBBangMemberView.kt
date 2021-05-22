package com.khs.nbbang.localMember

import android.util.Log
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

interface NBBangMemberView : AddNBBangMember, GetNbbangMember, UpdateNBBangMember,
    DeleteNBBangMember, MemberController,
    MemberPresenter {

    val compositeDisposable: CompositeDisposable

    fun renderLocalMembers(nbbangHistory: GetNBBangMemberResult)

    fun renderKakaoMembers(nbbangHistory: GetNBBangMemberResult)

    fun handleShowAllMember(sub: Scheduler, ob: Scheduler) {
        val d = getNBBangAllMember()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderLocalMembers(r)
            }
        compositeDisposable.add(d)
    }

    fun handleShowMembersByType(sub: Scheduler, ob: Scheduler, type: MemberType) {
        val d = getNBBangAllMemberByType(type)
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                when (type) {
                    MemberType.TYPE_KAKAO -> {
                        renderKakaoMembers(r)
                    }
                    MemberType.TYPE_FREE_USER -> {
                        renderLocalMembers(r)
                    }
                }
            }
        compositeDisposable.add(d)
    }

    fun handleShowMembersByGroupId(sub: Scheduler, ob: Scheduler, groupId: Long) {
        val d = getNBBangAllMemberByGroupId(groupId)
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderLocalMembers(r)
            }
        compositeDisposable.add(d)
    }

    fun handleAddMember(sub: Scheduler, ob: Scheduler, member: Member) {
        val d = addNBBangMember(requestAddMember(member))
            .flatMap {
                var type = if (member.kakaoId.isEmpty()) {
                    MemberType.TYPE_FREE_USER
                } else {
                    MemberType.TYPE_KAKAO
                }
                getNBBangAllMemberByType(type)
            }
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                when {
                    member.kakaoId.isEmpty() -> {
                        renderLocalMembers(r)
                    }
                    else -> {
                        renderKakaoMembers(r)
                    }
                }
            }
        compositeDisposable.add(d)
    }

    fun handleDeleteMember(sub: Scheduler, ob: Scheduler, member: Member) {
        val d = deleteNBBangMember(requestAddMember(member))
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                Log.v(this.javaClass.simpleName,"delete return value : $r")
                when {
                    member.kakaoId.isEmpty() -> {
                        handleShowMembersByType(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread(), MemberType.TYPE_FREE_USER
                        )
                    }
                    else -> {
                        handleShowMembersByType(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread(), MemberType.TYPE_KAKAO
                        )
                    }
                }
            }
        compositeDisposable.add(d)
    }

    fun updateKakaoMember(sub: Scheduler, ob: Scheduler, memberType: MemberType, memberList: List<Member>) {
        val d = deleteNBBangMember(memberType)
            .subscribeOn(sub)
            .subscribe { r ->
                Log.v(this.javaClass.simpleName,"updateKakaoMember : $memberList")
                for (member in memberList) {
                    handleAddMember(sub, ob, member)
                }
            }
        compositeDisposable.add(d)
    }

    fun handleUpdateMember(sub: Scheduler, ob: Scheduler, targetMember : Member, updateMember : Member) {
        val d = updateNBBangMember(requestUpdateMember(targetMember, updateMember))
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                when {
                    targetMember.kakaoId.isEmpty() -> {
                        handleShowMembersByType(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread(), MemberType.TYPE_FREE_USER
                        )
                    }
                    else -> {
                        handleShowMembersByType(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread(), MemberType.TYPE_KAKAO
                        )
                    }
                }
            }
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}