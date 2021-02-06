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
import com.khs.nbbang.utils.StringUtils
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.HashMap

class PageViewModel(fragmentManager: FragmentManager, application: Application) :
    AndroidViewModel(application) {
    val TAG = this.javaClass.name
    val _NNBLiveData: MutableLiveData<NNBObj> = MutableLiveData()
    val _viewPagerAdapter: MutableLiveData<CustomViewPagerAdapter> = MutableLiveData()
    val _selectedPeopleMap: MutableLiveData<HashMap<Int, NNBObj>> = MutableLiveData()
    val _placeCount: MutableLiveData<Int> = MutableLiveData()
    private var mDutchPayMap = mutableMapOf<String, Int>()

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
        Log.v(TAG,"setPeopleCount(...) peopleCount : $peopleCount")
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
        Log.v(TAG,"saveSelectedPeople(...)")
        _selectedPeopleMap.value!!.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NNBObj())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPeopleList = selectedPeopleList
            })
        }
    }

    fun clearSelectedPeople() {
        Log.v(TAG,"clearSelectedPeople(...)")
        _selectedPeopleMap.value!!.let {
            _selectedPeopleMap.postValue(it.apply {
                for(key in it.keys) {
                    this.get(key)!!.mPeopleList.clear()
                }
            })
        }
    }

    fun savePeopleName(peopleId: Int, name:String){
        /**
         * ODO : 고민필요
         * 해당 로직은 AddPeople 단계에서 People Name 수정될 때 마다 트리거로 발생하는 메소드.
         * 현 문제 정리
         *  1. 해당 메소드가 호출 될 때 NNBObj의 mPeopleList가 정의 되어있지 않음.
         *  2. '1'의 문제를 해결 하기 위해 List 형태가 아닌 Map형태로 데이터 관리 필요
         *      단, Map 형태의 데이터 관리 시 기존 로직이 대거 수정필요.
         *  3. Map 형태로 수정하는게 맞는것인가? 고민 필요.
         */

        _NNBLiveData.value!!.let{
            _NNBLiveData.postValue(it!!.apply {
                Log.v(TAG,"savePeopleName(...), index : $peopleId, name : $name")
                try {
                    it!!.mPeopleList.get(peopleId).mName = name
                } catch (ioobe : IndexOutOfBoundsException) {
                    Log.v(TAG, "Exception, \n $ioobe")
                    it!!.mPeopleList.add(peopleId, People(peopleId, name))
                }
            })
        }
    }

    fun resultNBB() : String {
        // no 1. 미아사거리 주막 : 155,000원 \n 참석자 : 김한솔, 정용인, 조현우, 김진혁, 최종휘 \n 5명"
        var result = ""
        var peopleMap = mutableMapOf<String, People>()
        result += "\t\t 전체 계산서 "
        for (key in _selectedPeopleMap.value!!.keys) {
            var peoplelist = _selectedPeopleMap.value!!.get(key)!!.mPeopleList
            var price = 0
            try {
                price = Integer.parseInt(_selectedPeopleMap.value!!.get(key)!!.mPrice)
            } catch (e : NumberFormatException) {
                result += "\n\t$key 차, 사용 금액 오류"
                continue
            }
            if (peoplelist.isEmpty()) {
                result += "\n\t$key 차, 참석자 명단 오류"
                continue
            }
            result += "\n\t\t\t\t\t"
            result += "\n\t\t ${key}차"
            result += "\n\t\t\t 참석 인원 수 : ${peoplelist.size}"
            result += "\n\t\t\t 참석 인원 : ${StringUtils().getPeopleList(peoplelist)}"
            result += "\n\t\t\t 장소 : ${_selectedPeopleMap.value!!.get(key)!!.mPlaceName}"
            result += "\n\t\t\t 사용 금액 : ${price}"
            result += "\n\t\t\t 더치페이 : ${price / peoplelist.size}"
            dutchPayBill(peoplelist, price / peoplelist.size)
            Log.v(TAG,"TEST, \n $result")
        }

        result += "\n\n\t\t 더치페이 정리 계산서\n"
        for (people in mDutchPayMap.keys) {
            result += "\n\t\t\t $people : ${mDutchPayMap.get(people)}"
        }

        return result
    }

    fun clearDutchPayMap() {
        mDutchPayMap.clear()
    }

    fun dutchPayBill(peopleList: MutableList<People>, payment:Int) {
        for (people in peopleList) {
            if (mDutchPayMap.get(people.mName) == null) {
                mDutchPayMap.put(people.mName, 0)
            }
            val totalPayment = mDutchPayMap.get(people.mName) as Int + payment
            mDutchPayMap.put(people.mName, totalPayment)
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
