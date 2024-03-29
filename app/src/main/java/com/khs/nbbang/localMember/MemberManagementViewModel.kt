package com.khs.nbbang.localMember

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.database.room.*
import com.khs.nbbang.database.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.room.*
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DateUtils
import com.khs.nbbang.utils.DebugMemberList
import com.khs.nbbang.utils.LogUtil
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

    override val mNBBHistoryDao: NBBHistoryDao
        get() = _db.value.let { it!!.nbbHistoryDao() }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbPlaceDao() }

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value.let { it!!.nbbMemberDao()}

    override val mNBBKeywordsDao: NBBSearchKeywordsDao
        get() = _db.value.let { it!!.nbbSearchKeywordDao() }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    init {
        updateLoadingFlag(false)
        _db.value = mDatabase
    }

    override fun renderLocalMembers(nbbangMemberresult: GetNBBangMemberResult) {
        val currentTime = System.currentTimeMillis()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderMembers(...) startTime : ${DateUtils.getDateByMillis(currentTime)}")
        val list = if (DEBUG) DebugMemberList.mDummyMemberList else nbbangMemberresult.nbbangMemberList
        _memberList.postValue(list)
    }


    override fun renderKakaoMembers(nbbangMemberresult: GetNBBangMemberResult) {
        val currentTime = System.currentTimeMillis()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderKakaoMembers(...) startTime : ${DateUtils.getDateByMillis(currentTime)}")
        val list = if (DEBUG) DebugMemberList.mDummyMemberList else nbbangMemberresult.nbbangMemberList
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

    fun saveKakaoMember(memberList: List<Member>) {
        updateKakaoMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            MemberType.TYPE_KAKAO,
            memberList
        )
    }

    fun update(updateMember: Member) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateJoinPeople(...) beforeMember : ${mSelectMember.value}, afterMember : ${updateMember}")
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

    fun showFavoriteMemberListByType(type : MemberType) {
        updateLoadingFlag(true)
        handleShowMembersByType(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            type
        )
    }

    fun selectMember(member : Member?) {
        member?.let {
            _selectMember.postValue(it)
        }
    }

    fun updateLoadingFlag(isShown : Boolean) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateLoadingFlag : $isShown")
        _showLoadingView.value = isShown
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}