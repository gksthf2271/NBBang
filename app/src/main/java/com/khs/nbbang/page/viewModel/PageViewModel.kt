package com.khs.nbbang.page.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.HistoryController
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBResultItem
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.*
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageViewModel(private val mDataBase :AppDatabase) : BaseViewModel(), NBBangHistoryView,
    NBBangGatewayImpl, HistoryController {

    private val _nbbLiveData: MutableLiveData<NBB> = MutableLiveData()
    val mNBBLiveData : LiveData<NBB> get() = _nbbLiveData

    private val _selectedPeopleMap: MutableLiveData<HashMap<Int, NBB>> = MutableLiveData()
    val mSelectedPeopleMap : LiveData<HashMap<Int, NBB>> get() = _selectedPeopleMap

    private val _placeCount: MutableLiveData<Int> = MutableLiveData()
    val mPlaceCount : LiveData<Int> get() = _placeCount

    private val _selectJoinPeople: MutableLiveData<Member> = MutableLiveData()
    val mSelectJoinPeople : LiveData<Member> get() = _selectJoinPeople

    private val _selectPlace: MutableLiveData<Int> = MutableLiveData()
    val mSelectPlace : LiveData<Int> get() = _selectPlace

    private var mDutchPayMap = mutableMapOf<String, Int>()

    private val _nbbResultItem: MutableLiveData<NBBResultItem> = MutableLiveData()
    val gNBBResultItem : LiveData<NBBResultItem> get() = _nbbResultItem

    var gIsSavedResult : Boolean = false

    init {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "createPageViewModel : $this")
        clearPageViewModel()
    }

    fun clearPageViewModel() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "clearPageViewModel(...)")
        CoroutineScope(Dispatchers.IO).launch {
            _nbbLiveData.postValue(NBB())
            _selectedPeopleMap.postValue(HashMap())
            _placeCount.postValue(0)
            _selectJoinPeople.postValue(Member())
            _selectPlace.postValue(0)

            mDutchPayMap = mutableMapOf()
            _nbbResultItem.postValue(NBBResultItem())
            gIsSavedResult = false
        }
    }

    fun updatePeopleList(joinPeopleList: MutableList<Member>) {
        _nbbLiveData.postValue(_nbbLiveData.value?.apply {
            this.mMemberList = joinPeopleList
            this.mMemberCount = joinPeopleList.size
        })
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updatePeopleList(...), ${_nbbLiveData.value!!.mMemberList}")
    }

    fun setPeopleCount(peopleCount: Int) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setPeopleCount(...) peopleCount : $peopleCount")
        _nbbLiveData.postValue(_nbbLiveData.value?.apply {
            this.mMemberCount = peopleCount
        })
    }

    fun selectPlace(placeKey : Int) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "selectPeople placeKey : $placeKey")
        _selectPlace.postValue(placeKey)
    }

    fun selectPeople(joinPeople: Member) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "selectPeople joinPeople : ${joinPeople.name}")
        _selectJoinPeople.postValue(joinPeople)
    }


    fun addJoinPeople(joinMember: Member) : Boolean{
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "addJoinPeople(...) people : $joinMember")
        _nbbLiveData.value?.let { nbb ->
            if (nbb.mMemberList.contains(joinMember)){
                for (member in nbb.mMemberList) {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "member : $member")
                }
                return false
            }

            _nbbLiveData.postValue(_nbbLiveData.value?.apply {
                val emptyIndex = getEmptyPeopleCircleView(this.mMemberList)
                if (emptyIndex == this.mMemberList.size) {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "emptyPeopleCircle is null")
                    this.mMemberList.add(joinMember)
                } else {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "emptyPeopleCircle is not null")
                    this.mMemberList[emptyIndex] = joinMember
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
                && member.name.isEmpty()
                && member.description.isEmpty()
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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "deleteJoinPeople(...) people : ${member.name}")
        _nbbLiveData.value?.let {
            _nbbLiveData.postValue(_nbbLiveData.value.apply {
                it.mMemberList.remove(member)
                it.mMemberCount = it.mMemberList.size
                updateJoinPlaceCount(it.mMemberCount)
            })
        }
    }

    fun updateJoinPeople(member: Member) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateJoinPeople(...) newMember : $member")
        val selectJoinPeople = mSelectJoinPeople.value
        _nbbLiveData.value?.let {
            val index = it.mMemberList.indexOf(selectJoinPeople)
            _nbbLiveData.postValue(_nbbLiveData.value.apply {
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
        _nbbLiveData.value?.let {
            _nbbLiveData.postValue(it.apply {
                it.mMemberCount += 1
                it.mMemberList.add(Member(index = it.mMemberList.size))
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "increaseJoinPeopleCount(...) ${it.mMemberCount}")
            })
        }
    }

    fun updateJoinPlaceCount(placeCount: Int) {
        _placeCount.postValue(placeCount)
    }

    fun decreaseJoinPeopleCount() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "decreaseJoinPeopleCount(...)")
        _nbbLiveData.value?.let {
            if (it.mMemberCount <= 0) return
            _nbbLiveData.postValue(it.apply {
                it.mMemberCount -= 1
                it.mMemberList.removeAt(it.mMemberList.lastIndex)
            })
        }
    }

    fun savePrice(placeId:Int, price: String) {
        try {
            price.replace(",","").toInt()
        } catch (numberFormatException : NumberFormatException) {
            LogUtil.eLog(LOG_TAG, TAG_CLASS, "numberFormatException : $numberFormatException")
            return
        }
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "TAG : $placeId , savePrice : $price")

        _selectedPeopleMap.value?.let {
            if (it[placeId] == null) it[placeId] = NBB()
            _selectedPeopleMap.postValue(it.apply {
                it[placeId]!!.mPrice = price
            })
        }
    }

    fun savePlaceName(placeId: Int, placeName: String) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "savePlaceName > placeId : $placeId , savePlaceName : $placeName")
        _selectedPeopleMap.value?.let {
            if (it[placeId] == null) it[placeId] = NBB()
            _selectedPeopleMap.postValue(it.apply {
                it[placeId]!!.mPlaceName = placeName
            })
        }
    }

    fun saveSelectedPeople(placeId: Int, selectedJoinPeopleList: MutableList<Member>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "saveSelectedPeople > selectedJoinPeopleList count : ${selectedJoinPeopleList.count()}")
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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "clearSelectedPeople(...)")
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
        result += "\t\t 전체 계산서 "
        clearDutchPayMap()
        clearNBBResultItem()

        _selectedPeopleMap.value?.let { peopleMap ->
            for (key in peopleMap.keys) {
                if (peopleMap[key] == null) {
                    LogUtil.dLog(LOG_TAG, TAG_CLASS, "잘못된 멤버 데이터 확인 필요!")
                    continue
                }
                val peopleList = peopleMap[key]?.mMemberList ?: mutableListOf()
                var priceInt = 0
                try {
                    priceInt = Integer.parseInt(peopleMap[key]!!.mPrice.replace(",",""))
                } catch (e : NumberFormatException) {
                    result += "\n\t$key 차, 사용 금액 오류 ${peopleMap[key]!!.mPrice}"
                    continue
                }
                if (peopleList.isEmpty()) {
                    result += "\n\t$key 차, 참석자 명단 오류"
                    continue
                }
                result += "\n\t\t\t\t\t"
                result += "\n\t\t ${key}차"
                result += "\n\t\t\t 참석 인원 수 : ${peopleList.size}"
                result += "\n\t\t\t 참석 인원 : ${StringUtils.getPeopleList(peopleList)}"
                result += "\n\t\t\t 장소 : ${peopleMap[key]!!.mPlaceName}"
                result += "\n\t\t\t 사용 금액 : ${peopleMap[key]!!.mPrice} 원"
                result += "\n\t\t\t 더치페이 : ${NumberUtils().makeCommaNumber(true,priceInt / peopleList.size)}"
                createDutchPayBill(peopleList, priceInt / peopleList.size)

                createNBBResult(
                    Place(
                        key,
                        peopleList.size,
                        peopleMap[key]!!.mPlaceName,
                        peopleList as ArrayList<Member>,
                        priceInt,
                        (priceInt / peopleList.size).toLong()
                    )
                )
            }
        }

        result += "\n\n\t\t 더치페이 정리 계산서\n"
        for (people in mDutchPayMap.keys) {
            val price = mDutchPayMap[people]

            if (price == null) {
                LogUtil.eLog(LOG_TAG, TAG_CLASS, "더치페이 중 Price NULL 발생!!")
                continue
            }

            createNBBResultDutchPayPeople(DutchPayPeople(
                -1,
                people,
                price.toLong()
            ))
            result += "\n\t\t\t $people : ${NumberUtils().makeCommaNumber(true, price)}"
        }

        LogUtil.vLog(LOG_TAG, TAG_CLASS, result)
        _nbbResultItem.postValue(_nbbResultItem.value)
        return result
    }

    private fun createNBBResultDutchPayPeople(dutchPayPeople: DutchPayPeople){
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "createNBBResultDutchPayPeople(...)" +
                "\n index  : ${dutchPayPeople.dutchPayPeopleIndex}" +
                "\n 이   름 : ${dutchPayPeople.dutchPayPeopleName}" +
                "\n 더치페이 : ${dutchPayPeople.dutchPay}" )
        _nbbResultItem.value!!.dutchPay.add(dutchPayPeople)
    }

    private fun createNBBResult(place: Place) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "createNBBResult(...)" +
                "\n N차    : ${place.placeIndex}" +
                "\n 참석인원 : ${place.joinPeopleCount}" +
                "\n 장소 명 : ${place.placeName}" +
                "\n 참석자  : ${StringUtils.getPeopleList(place.joinPeopleList)}" +
                "\n 비   용 : ${place.price}" +
                "\n 더치페이 : ${place.dutchPay}" )

        _nbbResultItem.value!!.place.add(place)
    }

    private fun clearDutchPayMap() {
        mDutchPayMap.clear()
        _nbbResultItem.postValue(NBBResultItem(arrayListOf(), arrayListOf()))
    }

    private fun clearNBBResultItem() {
        _nbbResultItem.value?.place?.clear()
        _nbbResultItem.value?.dutchPay?.clear()
    }

    fun loadNBBResult() : NBBResultItem {
        return _nbbResultItem.value!!
    }

    private fun createDutchPayBill(joinPeopleList: MutableList<Member>, payment:Int) {
        for (people in joinPeopleList) {
            if (mDutchPayMap[people.name] == null) {
                mDutchPayMap[people.name] = 0
            }
            val totalPayment = mDutchPayMap[people.name] as Int + payment
            mDutchPayMap[people.name] = totalPayment
        }
    }

    fun saveHistory() {
        _nbbLiveData.value.let {
            handleAddNBBangHistory(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                requestAddHistory(
                    System.currentTimeMillis(),
                    _nbbResultItem.value!!,
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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderHistorys(...) \n : nbbHistory : ${nbbangHistory.nbbangHistoryList.count()}")
    }

    override val mNBBHistoryDao: NBBHistoryDao
        get() = mDataBase.nbbHistoryDao()

    override val mNBBPlaceDao: NBBPlaceDao
        get() = mDataBase.nbbPlaceDao()

    override val mNBBMemberDao: NBBMemberDao
        get() = mDataBase.nbbMemberDao()

    override val mNBBKeywordsDao: NBBSearchKeywordsDao
        get() = mDataBase.nbbSearchKeywordDao()
}
