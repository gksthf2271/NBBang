package com.khs.nbbang.page.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.user.Member
import java.util.*
import kotlin.collections.ArrayList

class SelectMemberViewModel(val mContext: Context) : BaseViewModel() {
    private val _selectedMemberList: MutableLiveData<ArrayList<Member>> = MutableLiveData()
    val gSelectedMemberList: LiveData<ArrayList<Member>> get() = _selectedMemberList

    init {
        _selectedMemberList.value = arrayListOf()
    }

    fun setSelectedMemberList(memberList: ArrayList<Member>) {
        _selectedMemberList.postValue(memberList)
    }


    fun addSelectedMember(member : Member) {
        _selectedMemberList.value.let {
            _selectedMemberList.postValue(
                it!!.apply {
                    Log.v(TAG,"addSelectedMember : $member")
                    this.add(member)
                }
            )
        }
    }

    fun removeSelectedMember(member: Member) {
        _selectedMemberList.value.let {
            if (it!!.contains(member)) {
                _selectedMemberList.postValue(
                    it!!.apply {
                        Log.v(TAG,"removeSelectedMember : $member")
                        this.remove(member)
                    })
            }
        }
    }

    fun getSelectedMemberList(): ArrayList<Member> {
        Log.v(TAG,"getSelectedMemberList : ${_selectedMemberList.value!!}")
        return _selectedMemberList.value!!
    }

    fun clearSelectedMemberList() {
        _selectedMemberList.value.let {
            _selectedMemberList.postValue(it!!.apply {
                this.clear()
            })
        }
    }
}