package com.khs.nbbang.freeUser.viewModel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.freeUser.pageFragment.AddPeopleFragment
import com.khs.nbbang.freeUser.pageFragment.AddPlaceFragment
import com.khs.nbbang.freeUser.pageFragment.PeopleCountFragment
import com.khs.nbbang.freeUser.pageFragment.ResultPageFragment
import com.khs.nbbang.page.CustomViewPagerAdapter
import com.khs.nbbang.page.ItemObj.NNBObj
import com.khs.nbbang.page.ItemObj.People

class PageViewModel(fragmentManager: FragmentManager, application: Application) :
    AndroidViewModel(application) {
    val TAG = this.javaClass.name
    val _NNBLiveData: MutableLiveData<NNBObj> = MutableLiveData()
    val _viewPagerAdapter: MutableLiveData<CustomViewPagerAdapter> = MutableLiveData()
    val _selectedPeopleMap: MutableLiveData<HashMap<Int, NNBObj>> = MutableLiveData()
    val _placeCount: MutableLiveData<Int> = MutableLiveData()

    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    init {
        _viewPagerAdapter.value = CustomViewPagerAdapter(fragmentManager, mPageViewList)
        _NNBLiveData.value = NNBObj()
        _selectedPeopleMap.value = HashMap()
        _placeCount.value = 0
    }

    fun updatePeopleList(peopleList: MutableList<People>) {
        _NNBLiveData.postValue(_NNBLiveData.value.apply {
            this!!.mPeopleList = peopleList
        })
        Log.v(TAG, "updatePeopleList(...), ${_NNBLiveData.value!!.mPeopleList}")
    }

    fun setPeopleCount(peopleCount: Int) {
        _NNBLiveData.postValue(_NNBLiveData.value.apply {
            this!!.mPeopleCount = peopleCount
        })
    }

    fun increasePeople() {
        _NNBLiveData.value.let {
            _NNBLiveData!!.postValue(it.apply {
                it!!.mPeopleCount += 1
                Log.v(TAG, "increasePeople(...) ${it!!.mPeopleCount}")
            })
        }
    }

    fun decreasePeople() {
        Log.v(TAG, "decreasePeople(...)")
        _NNBLiveData.value.let {
            if (it!!.mPeopleCount!! <= 0) return
            _NNBLiveData.postValue(it.apply {
                it!!.mPeopleCount -= 1
            })
        }
    }

    fun savePrice(placeId:Int, price: String) {
        Log.v(TAG, "TAG : $placeId , savePrice : ${price}")
        _selectedPeopleMap.value.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NNBObj())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPrice = price
            })
        }
    }

    fun savePlaceName(placeId: Int, placeName: String) {
        Log.v(TAG, "TAG : $placeId , savePlaceName : ${placeName}")
        _selectedPeopleMap.value.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NNBObj())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPlaceName = placeName
            })
        }
    }

    fun saveSelectedPeople(placeId: Int, selectedPeopleList: MutableList<People>) {
        _selectedPeopleMap.value!!.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NNBObj())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPeopleList = selectedPeopleList
            })
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.v(this.javaClass.name, ">>> onCleared")
    }


    //ViewModel에 파라미터를 넘겨주기 위한 구현
    class PageViewModelFactory(val fragmentManager: FragmentManager, val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PageViewModel(fragmentManager, application) as T
        }
    }
}
