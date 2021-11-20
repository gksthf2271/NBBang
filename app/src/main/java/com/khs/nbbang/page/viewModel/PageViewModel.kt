package com.khs.nbbang.page.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.history.HistoryController
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBResultItem
import com.khs.nbbang.history.data.Place
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageViewModel(val mDB :AppDatabase) : ViewModel(), NBBangHistoryView,
    NBBangGatewayImpl, HistoryController {
    val TAG = this.javaClass.simpleName

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

    private val _NBBResultItem: MutableLiveData<NBBResultItem> = MutableLiveData()
    val gNBBResultItem : LiveData<NBBResultItem> get() = _NBBResultItem

    var gIsSavedResult : Boolean = false

    init {
        Log.v(TAG,"createPageViewModel : ${this}")
        clearPageViewModel()
    }

    fun clearPageViewModel() {
        Log.v(TAG, "clearPageViewModel(...)")
        CoroutineScope(Dispatchers.IO).launch {
            _NBBLiveData.postValue(NBB())
            _selectedPeopleMap.postValue(HashMap())
            _placeCount.postValue(0)
            _selectJoinPeople.postValue(Member())
            _selectPlace.postValue(0)
            mDutchPayMap = mutableMapOf()
            _NBBResultItem.postValue(NBBResultItem())
        }
    }

    fun updatePeopleList(joinPeopleList: MutableList<Member>) {
        _NBBLiveData.postValue(_NBBLiveData.value.apply {
            this!!.mMemberList = joinPeopleList
            this.mMemberCount = joinPeopleList.size
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
        Log.v(TAG,"selectPeople placeKey : $placeKey")
        _selectPlace.postValue(placeKey)
    }

    fun selectPeople(joinPeople: Member) {
        Log.v(TAG,"selectPeople joinPeople : ${joinPeople.name}")
        _selectJoinPeople.postValue(joinPeople)
    }


    fun addJoinPeople(member: Member) : Boolean{
        Log.v(TAG,"addJoinPeople(...) people : ${member}")
        _NBBLiveData.value?.let { nbb ->
            if (nbb.mMemberList.contains(member)){
                for (member in nbb.mMemberList) {
                    Log.v(TAG, "member : $member")
                }
                return false
            }

            _NBBLiveData.postValue(_NBBLiveData.value?.apply {
                val emptyIndex = getEmptyPeopleCircleView(this.mMemberList)
                if (emptyIndex == this.mMemberList.size) {
                    Log.v(TAG, "emptyPeopleCircle is null")
                    this.mMemberList.add(member)
                } else {
                    Log.v(TAG, "emptyPeopleCircle is not null")
                    this.mMemberList[emptyIndex] = member
                }
                this.mMemberCount = this.mMemberList.size
                updateJoinPlaceCount(this.mMemberCount)
            })
            return true
        }
        return false
    }

    private fun getEmptyPeopleCircleView(memberList : List<Member>) : Int {
        for (member in memberList) {
            if (member.id <= 0
                && member.name.isNullOrEmpty()
                && member.description.isNullOrEmpty()
                && member.profileImage.isNullOrEmpty()
                && member.thumbnailImage.isNullOrEmpty()
                && member.profileUri.isNullOrEmpty()
            ) {
                return memberList.indexOf(member)
            }
        }
        return memberList.size
    }

    fun deleteJoinPeople(member: Member) {
        Log.v(TAG,"deleteJoinPeople(...) people : ${member.name}")
        _NBBLiveData.value?.let {
            _NBBLiveData.postValue(_NBBLiveData.value.apply {
                it.mMemberList.remove(member)
                it.mMemberCount = it.mMemberList.size
                updateJoinPlaceCount(it.mMemberCount)
            })
        }
    }

    fun updateJoinPeople(member: Member) {
        Log.v(TAG,"updateJoinPeople(...) newMember : $member")
        val selectJoinPeople = mSelectJoinPeople.value
        _NBBLiveData.value?.let {
            val index = it.mMemberList.indexOf(selectJoinPeople)
            _NBBLiveData.postValue(_NBBLiveData.value.apply {
                it.mMemberList.get(index).run {
                    name = member.name
                    thumbnailImage = member.thumbnailImage
                    profileImage = member.profileImage
                    profileUri = member.profileUri
                    description = member.description
                    groupId = member.groupId
                }
            })
        }
    }

    fun increaseJoinPeopleCount() {
        _NBBLiveData.value?.let {
            _NBBLiveData.postValue(it.apply {
                it.mMemberCount += 1
                it.mMemberList.add(Member(index = it.mMemberList.size))
                Log.v(TAG, "increaseJoinPeopleCount(...) ${it.mMemberCount}")
            })
        }
    }

    fun updateJoinPlaceCount(placeCount: Int) {
        _placeCount.postValue(placeCount)
    }

    fun decreaseJoinPeopleCount() {
        Log.v(TAG, "decreaseJoinPeopleCount(...)")
        _NBBLiveData.value?.let {
            if (it.mMemberCount <= 0) return
            _NBBLiveData.postValue(it.apply {
                it.mMemberCount -= 1
                it.mMemberList.removeAt(it.mMemberList.lastIndex)
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

        _selectedPeopleMap.value?.let {
            if (it[placeId] == null) it[placeId] = NBB()
            _selectedPeopleMap.postValue(it.apply {
                it[placeId]!!.mPrice = price
            })
        }
    }

    fun savePlaceName(placeId: Int, placeName: String) {
        Log.v(TAG, "TAG : $placeId , savePlaceName : ${placeName}")
        _selectedPeopleMap.value?.let {
            if (it[placeId] == null) it[placeId] = NBB()
            _selectedPeopleMap.postValue(it.apply {
                it[placeId]!!.mPlaceName = placeName
            })
        }
    }

    fun saveSelectedPeople(placeId: Int, selectedJoinPeopleList: MutableList<Member>) {
        Log.v(TAG,"saveSelectedPeople(...) : ${selectedJoinPeopleList.count()}")
        _selectedPeopleMap.value?.let {
            if (it[placeId] == null) it[placeId] = NBB()
            _selectedPeopleMap.postValue(it.apply {
                this[placeId]!!.run {
                    mMemberList.clear()
                    mMemberList.addAll(selectedJoinPeopleList)
                    mPlaceIndex = placeId
                }
            })
        }
    }

    fun clearSelectedPeople() {
        Log.v(TAG,"clearSelectedPeople(...)")
        _selectedPeopleMap.let { selectedPeopleMap ->
            selectedPeopleMap.value?.let {
                selectedPeopleMap.postValue(it.apply {
                    for(key in it.keys) {
                        this[key]!!.mMemberList.clear()
                    }
                })
            }
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
            val peoplelist = _selectedPeopleMap.value!!.get(key)!!.mMemberList
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
            createDutchPayBill(peoplelist, priceInt / peoplelist.size)

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
            val price = mDutchPayMap.get(people)

            if (price == null) {
                Log.e(TAG, "더치페이 중 Price NULL 발생!!")
                continue
            }

            createNBBResultDutchPayPeople(DutchPayPeople(
                -1,
                people,
                price.toLong()
            ))
            result += "\n\t\t\t $people : ${NumberUtils().makeCommaNumber(true, price)}"
        }

        Log.v(TAG,"$result")
        return result
    }

    private fun createNBBResultDutchPayPeople(dutchPayPeople: DutchPayPeople){
        Log.v(TAG,"createNBBResultDutchPayPeople(...)" +
                "\n index  : ${dutchPayPeople.dutchPayPeopleIndex}" +
                "\n 이   름 : ${dutchPayPeople.dutchPayPeopleName}" +
                "\n 더치페이 : ${dutchPayPeople.dutchPay}" )
        _NBBResultItem.value!!.dutchPay.add(dutchPayPeople)
    }

    private fun createNBBResult(place: Place) {
        Log.v(TAG,"createNBBResult(...)" +
                "\n N차    : ${place.placeIndex}" +
                "\n 참석인원 : ${place.joinPeopleCount}" +
                "\n 장소 명 : ${place.placeName}" +
                "\n 참석자  : ${StringUtils().getPeopleList(place.joinPeopleList)}" +
                "\n 비   용 : ${place.price}" +
                "\n 더치페이 : ${place.dutchPay}" )

        _NBBResultItem.value!!.place.add(place)
    }

    fun clearDutchPayMap() {
        mDutchPayMap.clear()
        _NBBResultItem.value?.let {
            NBBResultItem(arrayListOf(), arrayListOf())
        }
    }

    fun clearNBBResultItem() {
        _NBBResultItem.value?.place?.clear()
        _NBBResultItem.value?.dutchPay?.clear()
    }

    fun loadNBBResult() : NBBResultItem {
        return _NBBResultItem.value!!
    }

    private fun createDutchPayBill(joinPeopleList: MutableList<Member>, payment:Int) {
        for (people in joinPeopleList) {
            if (mDutchPayMap[people.name] == null) {
                mDutchPayMap[people.name] = 0
            }
            val totalPayment = mDutchPayMap.get(people.name) as Int + payment
            mDutchPayMap[people.name] = totalPayment
        }
    }

    fun saveHistory() {
        _NBBLiveData.value.let {
            handleAddNBBangHistory(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                requestAddHistory(
                    System.currentTimeMillis(),
                    _NBBResultItem.value!!,
                    ""
                )
            ) {
                gIsSavedResult = it
            }
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
