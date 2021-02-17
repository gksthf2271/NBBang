package com.khs.nbbang.utils

import com.khs.nbbang.history.data.GetNBBangHistoryResult

class ServiceUtils() {

    //TODO: name으로 검색하지만 추후 인원 등록 후 사용할 수 있도록 할 예정
    fun getTotalAmountOfPayment(name: String, nbbangHistoryResult: GetNBBangHistoryResult) : String{
        var totalAmountOfPayment = 0
        for(nbbangHistory in nbbangHistoryResult.nbbangHistoryList) {
            for(dutchPayPeople in nbbangHistory.dutchPay){
                if (dutchPayPeople.name.equals(name)){
                    totalAmountOfPayment += dutchPayPeople.dutchPay.toInt()
                }
            }
        }
        return NumberUtils().makeCommaNumber(totalAmountOfPayment)
    }
}