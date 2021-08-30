package com.khs.nbbang.page.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.user.Member

class SelectMemberViewModel(val mContext: Context) : BaseViewModel() {
    private val _selectedMemberHashMap: MutableLiveData<HashMap<String, Member>> = MutableLiveData()
    val gSelectedMemberHashMap: LiveData<HashMap<String, Member>> get() = _selectedMemberHashMap

    init {
        _selectedMemberHashMap.value = hashMapOf()
    }

    fun setSelectedMemberList(memberHashMap: HashMap<String,Member>) {
        _selectedMemberHashMap.postValue(memberHashMap)
    }


    fun addSelectedMember(member : Member) {
        _selectedMemberHashMap.value.let {
            _selectedMemberHashMap.postValue(
                it!!.apply {
                    Log.v(TAG, "addSelectedMember : $member")
                    if (member.kakaoId.isNullOrEmpty()) {
                        this[member.name] = member
                    } else {
                        this[member.kakaoId] = member
                    }
                }
            )
        }
    }

    fun removeSelectedMember(member: Member) {
        _selectedMemberHashMap.value.let {
            if (it!!.containsKey(member.kakaoId) || it.containsKey(member.name)) {
                _selectedMemberHashMap.postValue(
                    it.apply {
                        Log.v(TAG,"removeSelectedMember : $member")
                        if (member.kakaoId.isNullOrEmpty()) {
                            this.remove(member.name)
                        } else {
                            this.remove(member.kakaoId)
                        }
                    })
            }
        }
    }

    fun getSelectedMemberHashMap(): HashMap<String, Member> {
        Log.v(TAG,"getSelectedMemberHashMap : ${_selectedMemberHashMap.value!!}")
        return _selectedMemberHashMap.value!!
    }

    fun clearSelectedMemberHashMap() {
        _selectedMemberHashMap.value.let {
            _selectedMemberHashMap.postValue(it!!.apply {
                this.clear()
            })
        }
    }
}