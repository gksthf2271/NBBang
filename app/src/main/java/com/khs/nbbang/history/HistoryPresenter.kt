package com.khs.nbbang.history

import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBangHistory

interface HistoryPresenter {

    /** TODO
     * 현재는 그대로 데이터를 넘겨주는 작업을 사실상 불필요 작업
     * 추후 추가되는 데이터 형태에 대한 parse 작업이 필요함.
     **/
    fun presentHistory(result: GetNBBangHistoryResult): GetNBBangHistoryResult =
        GetNBBangHistoryResult(
            result.nbbangHistoryList.map {
                NBBangHistory(
                    it.id,
                    it.date,
                    it.peopleCount,
                    it.totalPrice,
                    it.joinPeople,
                    it.place,
                    it.description,
                    it.done
                )
            }
        )

    companion object {
        private const val maxDescLength = 20
    }
}