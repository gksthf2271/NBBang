package com.khs.nbbang.group

import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DateUtils
import com.khs.nbbang.utils.DebugMemberList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MemberManagementViewModel (private val mDatabase: AppDatabase) : BaseViewModel(), NBBangMemberView,
    NBBangGatewayImpl {
    private val DEBUG = false

    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    private val _memberList : MutableLiveData<List<Member>> = MutableLiveData()
    val mMemberList : LiveData<List<Member>> get() = _memberList

    private val _kakaoFriendList : MutableLiveData<List<Member>> = MutableLiveData()
    val gKakaoFriendList : LiveData<List<Member>> get() = _kakaoFriendList

    private val _selectMember : MutableLiveData<Member> = MutableLiveData()
    val mSelectMember : LiveData<Member> get() = _selectMember

    private val _showLoadingView: MutableLiveData<Boolean> = MutableLiveData()
    val mShowLoadingView : LiveData<Boolean> get() = _showLoadingView

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbangDao() }

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value.let { it!!.nbbMemberDao()}

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    init {
        updateLoadingFlag(false)
        _db.value = mDatabase
    }

    override fun renderLocalMembers(nbbangMemberresult: GetNBBangMemberResult) {
        var currentTime = System.currentTimeMillis()
        Log.v(TAG,"renderMembers(...) startTime : ${DateUtils().getDateByMillis(currentTime)}")
        var list = if (DEBUG) DebugMemberList.mDummyMemberList else nbbangMemberresult.nbbangMemberList
        _memberList.postValue(list)
    }


    override fun renderKakaoMembers(nbbangMemberresult: GetNBBangMemberResult) {
        var currentTime = System.currentTimeMillis()
        Log.v(TAG,"renderKakaoMembers(...) startTime : ${DateUtils().getDateByMillis(currentTime)}")
        var list = if (DEBUG) DebugMemberList.mDummyMemberList else nbbangMemberresult.nbbangMemberList
        _kakaoFriendList.postValue(list)
    }

    fun saveMember(
        id: Long?, groupId: Long?, name: String, description: String, kakaoId: String,
        thumbnailImage: String?,
        profileImage: String?,
        profileUri: String?
    ) {
        updateLoadingFlag(true)
        handleAddMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Member(
                name = name,
                description = description,
                kakaoId = kakaoId,
                thumbnailImage = thumbnailImage,
                profileImage = profileImage,
                profileUri = profileUri
            )
        )
    }

    fun deleteMember(member: Member) {
        updateLoadingFlag(true)
        handleDeleteMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            member
        )
    }

    fun update(updateMember: Member) {
        Log.v(TAG,"updateJoinPeople(...) beforeMember : ${mSelectMember.value}, afterMember : ${updateMember}")
        updateLoadingFlag(true)
        handleUpdateMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            mSelectMember.value ?: return,
            Member(
                -1,
                -1,
                updateMember.name,
                updateMember.groupId,
                updateMember.description,
                updateMember.kakaoId,
                updateMember.thumbnailImage,
                updateMember.profileImage,
                updateMember.profileUri
            )
        )
//        var selectMember = mSelectMember.value
//        _memberList.value.let {
//            var index = it!!.indexOf(selectMember)
//            _memberList.postValue(_memberList.value.apply {
//                it!!.get(index).name = name
//                it!!.get(index).description = description
//            })
//        }
    }

    fun showMemberList() {
        updateLoadingFlag(true)
        handleShowMembersByType(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            MemberType.TYPE_FREE_USER
        )
    }

    fun showKakaoFriends() {
        updateLoadingFlag(true)
        handleShowMembersByType(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            MemberType.TYPE_KAKAO
        )
    }

    fun selectMember(member : Member?) {
        _selectMember.postValue(member)
    }

    fun updateLoadingFlag(isShown : Boolean) {
        Log.v(TAG,"updateLoadingFlag : $isShown")
        _showLoadingView.value = isShown
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}