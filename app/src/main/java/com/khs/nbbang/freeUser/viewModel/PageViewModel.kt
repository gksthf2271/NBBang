package com.khs.nbbang.freeUser.viewModel

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.freeUser.pageFragment.AddPeopleFragment
import com.khs.nbbang.freeUser.pageFragment.AddPlaceFragment
import com.khs.nbbang.freeUser.pageFragment.PeopleCountFragment
import com.khs.nbbang.freeUser.pageFragment.ResultPageFragment
import com.khs.nbbang.page.CustomViewPagerAdapter
import com.khs.nbbang.page.ItemObj.PeopleListObj

class PageViewModel(fm:FragmentManager, application: Application) : AndroidViewModel(application) {
    val TAG = this.javaClass.name
    val _peopleListLiveData: MutableLiveData<PeopleListObj> = MutableLiveData()
    var _counter: MutableLiveData<Int> = MutableLiveData()
    val mViewPagerAdapter : CustomViewPagerAdapter

    val mPageViewList : MutableList<Fragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    init {
        mViewPagerAdapter = CustomViewPagerAdapter(fm, mPageViewList)
        _counter.value = 0
        _peopleListLiveData.value = PeopleListObj()
    }

    //Observe 를 이용한 데이터 수신을 위한 LiveData
    val mPeopleListObj: LiveData<PeopleListObj>
        get() = _peopleListLiveData

    val mPeopleCount: LiveData<Int> get() = _counter

    fun updatePeopleCircle(){
        _peopleListLiveData.value.let { _peopleListLiveData.value!!.mPeopleCount = _counter.value!! }
        Log.v(TAG,"updatePeopleCircle, peopleCount : ${_peopleListLiveData.value!!.mPeopleCount}")
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