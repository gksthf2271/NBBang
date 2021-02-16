package com.khs.nbbang.page.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.history.HistoryContoroller
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.StringUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PageViewModel(loginCookie: LoginCookie, db :AppDatabase) : ViewModel(), NBBangHistoryView,
    NBBangGatewayImpl, HistoryContoroller {
    val TAG = this.javaClass.name

    private val mDB : AppDatabase

    private val _NBBLiveData: MutableLiveData<NBB> = MutableLiveData()
    val mNBBLiveData : LiveData<NBB> get() = _NBBLiveData

    private val _selectedPeopleMap: MutableLiveData<HashMap<Int, NBB>> = MutableLiveData()
    val mSelectedPeopleMap : LiveData<HashMap<Int, NBB>> get() = _selectedPeopleMap

    private val _placeCount: MutableLiveData<Int> = MutableLiveData()
    val mPlaceCount : LiveData<Int> get() = _placeCount

    private var mDutchPayMap = mutableMapOf<String, Int>()

    init {
        mDB = db
        _NBBLiveData.value = NBB()
        _selectedPeopleMap.value = HashMap()
        _placeCount.value = 0
    }

    fun updatePeopleList(peopleList: MutableList<People>) {
        _NBBLiveData.postValue(_NBBLiveData.value.apply {
            this!!.mPeopleList = peopleList
        })
        Log.v(TAG, "updatePeopleList(...), ${_NBBLiveData.value!!.mPeopleList}")
    }

    fun setPeopleCount(peopleCount: Int) {
        Log.v(TAG,"setPeopleCount(...) peopleCount : $peopleCount")
        _NBBLiveData.postValue(_NBBLiveData.value.apply {
            this!!.mPeopleCount = peopleCount
        })
    }

    fun increasePeople() {
        _NBBLiveData.value.let {
            _NBBLiveData!!.postValue(it.apply {
                it!!.mPeopleCount += 1
                Log.v(TAG, "increasePeople(...) ${it!!.mPeopleCount}")
            })
        }
    }

    fun updatePlaceCount(placeCount: Int) {
        _placeCount.postValue(placeCount)
    }

    fun decreasePeople() {
        Log.v(TAG, "decreasePeople(...)")
        _NBBLiveData.value.let {
            if (it!!.mPeopleCount!! <= 0) return
            _NBBLiveData.postValue(it.apply {
                it!!.mPeopleCount -= 1
            })
        }
    }

    fun savePrice(placeId:Int, price: String) {
        Log.v(TAG, "TAG : $placeId , savePrice : ${price}")
        _selectedPeopleMap.value.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NBB())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPrice = price
            })
        }
    }

    fun savePlaceName(placeId: Int, placeName: String) {
        Log.v(TAG, "TAG : $placeId , savePlaceName : ${placeName}")
        _selectedPeopleMap.value.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NBB())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mPlaceName = placeName
            })
        }
    }

    fun saveSelectedPeople(placeId: Int, selectedPeopleList: MutableList<People>) {
        Log.v(TAG,"saveSelectedPeople(...)")
        _selectedPeopleMap.value!!.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NBB())
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
         * 고민필요
         * 해당 로직은 AddPeople 단계에서 People Name 수정될 때 마다 트리거로 발생하는 메소드.
         * 현 문제 정리
         *  1. 해당 메소드가 호출 될 때 NBB의 mPeopleList가 정의 되어있지 않음.
         *  2. '1'의 문제를 해결 하기 위해 List 형태가 아닌 Map형태로 데이터 관리 필요
         *      단, Map 형태의 데이터 관리 시 기존 로직이 대거 수정필요.
         *  3. Map 형태로 수정하는게 맞는것인가? 고민 필요.
         */

        _NBBLiveData.value!!.let{
            _NBBLiveData.postValue(it!!.apply {
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
        var result = ""
        var peopleMap = mutableMapOf<String, People>()
        result += "\t\t 전체 계산서 "
        for (key in _selectedPeopleMap.value!!.keys) {
            /**
             * TODO
             * 결과 페이지 예외처리 필요
             */
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

    private fun dutchPayBill(peopleList: MutableList<People>, payment:Int) {
        for (people in peopleList) {
            if (mDutchPayMap.get(people.mName) == null) {
                mDutchPayMap.put(people.mName, 0)
            }
            val totalPayment = mDutchPayMap.get(people.mName) as Int + payment
            mDutchPayMap.put(people.mName, totalPayment)
        }
    }

    fun saveHistory() {
        _NBBLiveData.value.let {
            handleAddNBBangHistory(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                requestAddHistory(System.currentTimeMillis(),
                    NBBangHistory(null, System.currentTimeMillis(), it!!.mPeopleCount, it!!.mPrice.toInt(), it!!.mPeopleList, "")
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderHistorys(nbbangHistory: GetNBBangHistoryResult) {
        Log.v(TAG,"renderHistorys(...) \n : nbbHistory : ${nbbangHistory.nbbangHistoryList.count()}")
    }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = mDB.nbbangDao()
}
