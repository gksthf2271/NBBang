package com.khs.nbbang.group

import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DateUtils
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

    var mDummyMemberList: ArrayList<Member> = arrayListOf(
        Member(0, 0,"김한솔", 0, "월곡회"),
        Member(1, 1,"신상은", 0, "월곡회"),
        Member(2, 2,"정용인", 0, "월곡회"),
        Member(3, 3,"김진혁", 0, "월곡회"),
        Member(4, 4,"조현우", 0, "월곡회"),
        Member(5, 5,"최종휘", 0, "월곡회"),
        Member(6, 6,"김진근", 0, "월곡회"),
        Member(7, 7,"이진형", 0, "월곡회"),
        Member(8, 8,"배재룡", 0, "월곡회"),
        Member(9, 9, "정준호", 0, "월곡회"),
        Member(10, 10,"박소연", 0, "월곡회"),
        Member(11, 11,"장선형", 0, "월곡회"),
        Member(12, 12,"신주연", 0, "월곡회"),
        Member(13, 13,"주경애", 0, "월곡회")
    )

    override fun renderMembers(nbbangMemberresult: GetNBBangMemberResult) {
        var currentTime = System.currentTimeMillis()
        Log.v(TAG,"renderMembers(...) startTime : ${DateUtils().getDateByMillis(currentTime)}")
        var list = if (DEBUG) mDummyMemberList else nbbangMemberresult.nbbangMemberList
        _memberList.postValue(list)
    }

    fun saveMember(
        id: Long?, groupId: Long?, name: String, description: String, kakaoId: Long,
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
        handleShowAllMember(
            Schedulers.io(),
            AndroidSchedulers.mainThread())
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