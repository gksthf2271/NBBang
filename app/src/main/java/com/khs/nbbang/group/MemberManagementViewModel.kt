package com.khs.nbbang.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MemberManagementViewModel (private val mDatabase: AppDatabase) : BaseViewModel(), NBBangMemberView,
    NBBangGatewayImpl {
    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    private val _memberList : MutableLiveData<GetNBBangMemberResult> = MutableLiveData()
    val mMemberList : LiveData<GetNBBangMemberResult> get() = _memberList

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbangDao() }

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value.let { it!!.nbbMemberDao()}

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    init {
        _db.value = mDatabase
    }

    override fun renderMembers(nbbangMemberresult: GetNBBangMemberResult) {
        Log.v(TAG,"renderMembers(...)")
        _memberList.postValue(nbbangMemberresult)
    }

    fun saveMember(id : Long?, groupId : Long?, name : String, description : String, resId: Int?) {
        handleAddMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Member(id ?: -1, name, groupId ?: 0, description, resId ?: R.drawable.icon_user)
        )
    }

    fun showMemberList() {
        handleShowAllMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread())
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}