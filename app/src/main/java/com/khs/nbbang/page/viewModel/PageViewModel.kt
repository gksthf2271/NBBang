package com.khs.nbbang.page.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.R
import com.khs.nbbang.history.HistoryController
import com.khs.nbbang.history.data.*
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PageViewModel(val mDB :AppDatabase) : ViewModel(), NBBangHistoryView,
    NBBangGatewayImpl, HistoryController {
    val TAG = this.javaClass.name

    private val _NBBLiveData: MutableLiveData<NBB> = MutableLiveData()
    val mNBBLiveData : LiveData<NBB> get() = _NBBLiveData

    private val _selectedPeopleMap: MutableLiveData<HashMap<Int, NBB>> = MutableLiveData()
    val mSelectedPeopleMap : LiveData<HashMap<Int, NBB>> get() = _selectedPeopleMap

    private val _placeCount: MutableLiveData<Int> = MutableLiveData()
    val mPlaceCount : LiveData<Int> get() = _placeCount

    private val _selectJoinPeople: MutableLiveData<Member> = MutableLiveData()
    val mSelectJoinPeople : LiveData<Member> get() = _selectJoinPeople

    private val _selectPlace: MutableLiveData<Int> = MutableLiveData()
    val mSelectPlace : LiveData<Int> get() = _selectPlace

    private var mDutchPayMap = mutableMapOf<String, Int>()

    private var mNBBResultItem : NBBResultItem = NBBResultItem(arrayListOf(), arrayListOf())

    init {
        clearPageViewModel()
    }

    fun clearPageViewModel() {
        _NBBLiveData.value = NBB()
        _selectedPeopleMap.value = HashMap()
        _placeCount.value = 0
    }

    fun updatePeopleList(joinPeopleList: MutableList<Member>) {
        _NBBLiveData.postValue(_NBBLiveData.value.apply {
            this!!.mMemberList = joinPeopleList
            this!!.mMemberCount = joinPeopleList.size
        })
        Log.v(TAG, "updatePeopleList(...), ${_NBBLiveData.value!!.mMemberList}")
    }

    fun setPeopleCount(peopleCount: Int) {
        Log.v(TAG,"setPeopleCount(...) peopleCount : $peopleCount")
        _NBBLiveData.postValue(_NBBLiveData.value.apply {
            this!!.mMemberCount = peopleCount
        })
    }

    fun selectPlace(placeKey : Int) {
        _selectPlace.postValue(placeKey)
    }

    fun selectPeople(joinPeople: Member) {
        _selectJoinPeople.postValue(joinPeople)
    }

    fun addJoinPeople(member: Member) {
        Log.v(TAG,"addJoinPeople(...) people : ${member.name}")
        _NBBLiveData.value.let { nbb ->
            _NBBLiveData.postValue(_NBBLiveData.value.apply {
                var emptyIndex = getEmptyPeopleCircleView(nbb!!.mMemberList)
                if (emptyIndex == nbb!!.mMemberList.size) {
                    nbb!!.mMemberList.add(emptyIndex, member)
                } else {
                    nbb!!.mMemberList.set(emptyIndex, member)
                }
                nbb!!.mMemberCount = nbb!!.mMemberList.size
                updateJoinPlaceCount(nbb!!.mMemberCount)
            })
        }
    }

    fun getEmptyPeopleCircleView(memberList : List<Member>) : Int {
        for (member in memberList) {
            if (member.id <= 0 && member.name.isEmpty()) {
                return memberList.indexOf(member)
            }
        }
        return memberList.size
    }

    fun deleteJoinPeople(member: Member) {
        Log.v(TAG,"deleteJoinPeople(...) people : ${member.name}")
        _NBBLiveData.value.let {
            _NBBLiveData.postValue(_NBBLiveData.value.apply {
                it!!.mMemberList.remove(member)
                it!!.mMemberCount = it!!.mMemberList.size
                updateJoinPlaceCount(it!!.mMemberCount)
            })
        }
    }

    fun updateJoinPeople(member: Member) {
        Log.v(TAG,"updateJoinPeople(...) newMember : $member")
        var selectJoinPeople = mSelectJoinPeople.value
        _NBBLiveData.value.let {
            var index = it!!.mMemberList.indexOf(selectJoinPeople)
            _NBBLiveData.postValue(_NBBLiveData.value.apply {
                it!!.mMemberList.get(index).name = member.name
                it!!.mMemberList.get(index).thumbnailImage = member.thumbnailImage
                it!!.mMemberList.get(index).profileImage = member.profileImage
                it!!.mMemberList.get(index).profileUri = member.profileUri
                it!!.mMemberList.get(index).description = member.description
                it!!.mMemberList.get(index).groupId = member.groupId
            })
        }
    }

    fun increaseJoinPeopleCount() {
        _NBBLiveData.value.let {
            _NBBLiveData!!.postValue(it.apply {
                it!!.mMemberCount += 1
                it!!.mMemberList.add(Member(index = it!!.mMemberList.size))
                Log.v(TAG, "increaseJoinPeopleCount(...) ${it!!.mMemberCount}")
            })
        }
    }

    fun updateJoinPlaceCount(placeCount: Int) {
        _placeCount.postValue(placeCount)
    }

    fun decreaseJoinPeopleCount() {
        Log.v(TAG, "decreaseJoinPeopleCount(...)")
        _NBBLiveData.value.let {
            if (it!!.mMemberCount!! <= 0) return
            _NBBLiveData.postValue(it.apply {
                it!!.mMemberCount -= 1
                it!!.mMemberList.removeAt(it!!.mMemberList.lastIndex)
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

    fun saveSelectedPeople(placeId: Int, selectedJoinPeopleList: MutableList<Member>) {
        Log.v(TAG,"saveSelectedPeople(...)")
        _selectedPeopleMap.value!!.let {
            if (it!!.get(placeId) == null) it!!.put(placeId, NBB())
            _selectedPeopleMap.postValue(it!!.apply {
                it!!.get(placeId)!!.mMemberList.clear()
                it!!.get(placeId)!!.mMemberList.addAll(selectedJoinPeopleList)
                it!!.get(placeId)!!.mPlaceIndex = placeId
            })
        }
    }

    fun clearSelectedPeople() {
        Log.v(TAG,"clearSelectedPeople(...)")
        _selectedPeopleMap.value!!.let {
            _selectedPeopleMap.postValue(it.apply {
                for(key in it.keys) {
                    this.get(key)!!.mMemberList.clear()
                }
            })
        }
    }

    fun resultNBB() : String {
        var result = ""
        var peopleMap = mutableMapOf<String, Member>()
        result += "\t\t 전체 계산서 "
        clearNBBResultItem()

        for (key in _selectedPeopleMap.value!!.keys) {
            /**
             * TODO
             * 결과 페이지 예외처리 필요
             */
            var peoplelist = _selectedPeopleMap.value!!.get(key)!!.mMemberList
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
                    peoplelist as ArrayList<Member>,
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
                "\n index  : ${dutchPayPeople.dutchPayPeopleIndex}" +
                "\n 이   름 : ${dutchPayPeople.dutchPayPeopleName}" +
                "\n 더치페이 : ${dutchPayPeople.dutchPay}" )
        mNBBResultItem.dutchPay.add(dutchPayPeople)
    }

    private fun createNBBResult(place: Place) {
        Log.v(TAG,"createNBBResult(...)" +
                "\n N차    : ${place.placeIndex}" +
                "\n 참석인원 : ${place.joinPeopleCount}" +
                "\n 장소 명 : ${place.placeName}" +
                "\n 참석자  : ${StringUtils().getPeopleList(place.joinPeopleList)}" +
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

    private fun dutchPayBill(joinPeopleList: MutableList<Member>, payment:Int) {
        for (people in joinPeopleList) {
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

    override val mNBBMemberDao: NBBMemberDao
        get() = mDB.nbbMemberDao()
}
