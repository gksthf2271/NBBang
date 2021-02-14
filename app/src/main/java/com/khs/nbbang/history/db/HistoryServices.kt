package com.khs.nbbang.history.db

import com.khs.nbbang.history.data.NBBangHistory

interface HistoryServices {
    fun calculateProgress(historys: List<NBBangHistory>): List<NBBangHistory> {
        //TODO : data 가공
        return historys
    }
}