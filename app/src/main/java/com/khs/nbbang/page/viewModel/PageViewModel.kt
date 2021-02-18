package com.khs.nbbang.page.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.history.HistoryContoroller
import com.khs.nbbang.history.data.*
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PageViewModel(loginCookie: LoginCookie, val mDB :AppDatabase) : ViewModel(), NBBangHistoryView,
    NBBangGatewayImpl, HistoryContoroller {
    val TAG = this.javaClass.name

    private val _NBBLiveData: MutableLiveData<NBB> = MutableLiveData()
    val mNBBLiveData : LiveData<NBB> get() = _NBBLiveData

    private val _selectedPeopleMap: MutableLiveData<HashMap<Int, NBB>> = MutableLiveData()
    val mSelectedPeopleMap : LiveData<HashMap<Int, NBB>> get() = _selectedPeopleMap

    private val _placeCount: MutableLiveData<Int> = MutableLiveData()
    val mPlaceCount : LiveData<Int> get() = _placeCount

    private var mDutchPayMap = mutableMapOf<String, Int>()

    private var mNBBResultItem : NBBResultItem = NBBResultItem(arrayListOf(), arrayListOf())

    init {
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
        try {
            price.replace(",","").toInt()
        } catch (numberFormatException : NumberFormatException) {
            Log.e(TAG, "numberFormatException : $numberFormatException")
            return
        }
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
                    it!!.mPeopleList.get(peopleId).name = name
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
        clearNBBResultItem()

        for (key in _selectedPeopleMap.value!!.keys) {
            /**
             * TODO
             * 결과 페이지 예외처리 필요
             */
            var peoplelist = _selectedPeopleMap.value!!.get(key)!!.mPeopleList
            var priceInt = 0
            try {
                priceInt = Integer.parseInt(_selectedPeopleMap.value!!.get(key)!!.mPrice.replace(",",""))
            } catch (e : NumberFormatException) {
                result += "\n\t$key 차, 사용 금액 오류 ${_selectedPeopleMap.value!!.get(key)!!.mPrice}"
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
            result += "\n\t\t\t 사용 금액 : ${_selectedPeopleMap.value!!.get(key)!!.mPrice} 원"
            result += "\n\t\t\t 더치페이 : ${NumberUtils().makeCommaNumber(true,priceInt / peoplelist.size)}"
            dutchPayBill(peoplelist, priceInt / peoplelist.size)

            createNBBResult(
                Place(
                    key,
                    peoplelist.size,
                    _selectedPeopleMap.value!!.get(key)!!.mPlaceName,
                    peoplelist as ArrayList<People>,
                    priceInt,
                    (priceInt / peoplelist.size).toLong()
                )
            )
        }

        result += "\n\n\t\t 더치페이 정리 계산서\n"
        for (people in mDutchPayMap.keys) {
            var price = mDutchPayMap.get(people)

            if (price == null) {
                Log.e(TAG, "더치페이 중 Price NULL 발생!!")
                continue
            }

            createNBBResultDutchPayPeople(DutchPayPeople(
                -1,
                people,
                price!!.toLong()
            ))
            result += "\n\t\t\t $people : ${NumberUtils().makeCommaNumber(true, price)}"
        }

        return result
    }

    private fun createNBBResultDutchPayPeople(dutchPayPeople: DutchPayPeople){
        Log.v(TAG,"createNBBResultDutchPayPeople(...)" +
                "\n index  : ${dutchPayPeople.index}" +
                "\n 이   름 : ${dutchPayPeople.name}" +
                "\n 더치페이 : ${dutchPayPeople.dutchPay}" )
        mNBBResultItem.dutchPay.add(dutchPayPeople)
    }

    private fun createNBBResult(place: Place) {
        Log.v(TAG,"createNBBResult(...)" +
                "\n N차    : ${place.placeIndex}" +
                "\n 참석인원 : ${place.joinPeopleCount}" +
                "\n 장소 명 : ${place.placeName}" +
                "\n 참석자  : ${StringUtils().getPeopleList(place.peopleList)}" +
                "\n 비   용 : ${place.price}" +
                "\n 더치페이 : ${place.dutchPay}" )

        mNBBResultItem.place.add(place)
    }

    fun clearDutchPayMap() {
        mDutchPayMap.clear()
        mNBBResultItem.apply {
            NBBResultItem(arrayListOf(), arrayListOf())
        }
    }

    fun clearNBBResultItem() {
        mNBBResultItem.place.clear()
        mNBBResultItem.dutchPay.clear()
    }

    private fun dutchPayBill(peopleList: MutableList<People>, payment:Int) {
        for (people in peopleList) {
            if (mDutchPayMap.get(people.name) == null) {
                mDutchPayMap.put(people.name, 0)
            }
            val totalPayment = mDutchPayMap.get(people.name) as Int + payment
            mDutchPayMap.put(people.name, totalPayment)
        }
    }

    fun saveHistory() {
        _NBBLiveData.value.let {
            handleAddNBBangHistory(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                requestAddHistory(
                    System.currentTimeMillis(),
                    mNBBResultItem,
                    ""
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
