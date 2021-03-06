package com.khs.nbbang.group

import android.util.Log
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

interface NBBangMemberView : AddNBBangMember, GetNbbangMember, UpdateNBBangMember,
    DeleteNBBangMember, MemberController,
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

    fun handleShowMembersByGroupId(sub: Scheduler, ob: Scheduler, groupId: Long) {
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

    fun handleDeleteMember(sub: Scheduler, ob: Scheduler, member: Member) {
        val d = deleteNBBangMember(requestAddMember(member))
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                Log.v(this.javaClass.name,"delete return value : $r")
                handleShowAllMember(Schedulers.io(),
                    AndroidSchedulers.mainThread())
            }
        compositeDisposable.add(d)
    }

    fun handleUpdateMember(sub: Scheduler, ob: Scheduler, targetMember : Member, updateMember : Member) {
        val d = updateNBBangMember(requestUpdateMember(targetMember, updateMember))
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                handleShowAllMember(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread())
            }
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}