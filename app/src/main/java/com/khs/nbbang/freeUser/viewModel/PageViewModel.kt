package com.khs.nbbang.freeUser.viewModel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.freeUser.pageFragment.AddPeopleFragment
import com.khs.nbbang.freeUser.pageFragment.AddPlaceFragment
import com.khs.nbbang.freeUser.pageFragment.PeopleCountFragment
import com.khs.nbbang.freeUser.pageFragment.ResultPageFragment
import com.khs.nbbang.page.CustomViewPagerAdapter
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.PeopleListObj

class PageViewModel(fragmentManager: FragmentManager, application: Application) : AndroidViewModel(application) {
    val TAG = this.javaClass.name
    val _peopleListLiveData: MutableLiveData<PeopleListObj> = MutableLiveData()
    var _counter: MutableLiveData<Int> = MutableLiveData()
    var _peopleList: MutableLiveData<MutableList<People>> = MutableLiveData()
    val _viewPagerAdapter : MutableLiveData<CustomViewPagerAdapter> = MutableLiveData()

    val mPageViewList : MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    init {
        _viewPagerAdapter.value = CustomViewPagerAdapter(fragmentManager, mPageViewList)
        _peopleListLiveData.value = PeopleListObj()
        _peopleList.value = _peopleListLiveData.value!!.mPeopleList
        _counter.value = _peopleListLiveData.value!!.mPeopleCount
    }

    fun updatePeopleCircle(){
        _peopleListLiveData.value.let {
            _peopleListLiveData.value!!.mPeopleList = _peopleList.value!!
            _peopleListLiveData.value!!.mPeopleCount = _counter.value!!
        }
        Log.v(TAG,"updatePeopleCircle, peopleCount : ${_peopleListLiveData.value!!.mPeopleCount}")
    }

    fun updatePeopleList(peopleList : MutableList<People>) {
        _peopleList.value = peopleList
        Log.v(TAG,"updatePeopleList(...), ${_peopleList.value}")
    }

    fun increasePeople() {
        Log.v(TAG,"increasePeople(...)")
        _counter.value = _counter.value!!.plus(1)
    }

    fun decreasePeople() {
        Log.v(TAG,"decreasePeople(...)")
        if (_counter.value!! <= 0) return
        _counter.value = _counter.value!!.minus(1)
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(this.javaClass.name, ">>> onCleared")
    }


    //ViewModel에 파라미터를 넘겨주기 위한 구현
    class PageViewModelFactory(val fragmentManager: FragmentManager, val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PageViewModel(fragmentManager, application) as T
        }

    }
}