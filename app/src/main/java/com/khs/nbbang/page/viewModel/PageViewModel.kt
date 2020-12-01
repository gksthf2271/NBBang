package com.khs.nbbang.page.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.page.ItemObj.PeopleListObj

class PageViewModel : BaseViewModel() {

    val _peopleListLiveData: MutableLiveData<PeopleListObj> = MutableLiveData()
    var _counter: MutableLiveData<Int> = MutableLiveData()

    init {
        _counter.value = 0
    }

    //Observe 를 이용한 데이터 수신을 위한 LiveData
    val mPeopleListObj: LiveData<PeopleListObj>
        get() = _peopleListLiveData

    val mPeopleCount: LiveData<Int> get() = _counter

    fun getPeopleListObj() {
        //송신 데이터 생성
        val peopleListObj = PeopleListObj().apply {
        }

        _peopleListLiveData.value = peopleListObj
    }

    fun increasePeople() {
        _counter.value = _counter.value!!.plus(1)
    }

    fun decreasePeople() {
        if (_counter.value!! <= 0) return
        _counter.value = _counter.value!!.minus(1)
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(this.javaClass.name, ">>> onCleared")
    }
}