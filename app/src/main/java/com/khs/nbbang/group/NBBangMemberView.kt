package com.khs.nbbang.group

import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

interface NBBangMemberView : AddNBBangMember, GetNbbangMember, MemberController,
    MemberPresenter {

    val compositeDisposable: CompositeDisposable

    fun renderMembers(nbbangHistory: GetNBBangMemberResult)

    fun handleShowAllMember(sub: Scheduler, ob: Scheduler) {
        val d = getNBBangAllMember()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderMembers(
                    presentMember(r)
                )
            }
        compositeDisposable.add(d)
    }

    fun handleShowHistoryByGroupId(sub: Scheduler, ob: Scheduler, groupId: Long) {
        val d = getNBBangAllMemberByGroupId(groupId)
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderMembers(
                    presentMember(r)
                )
            }
        compositeDisposable.add(d)
    }

    fun handleAddMember(sub: Scheduler, ob: Scheduler, member: Member) {
        val d = addNBBangMember(requestAddMember(member))
            .flatMap { getNBBangAllMember() }
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderMembers(
                    presentMember(r)
                )
            }
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}