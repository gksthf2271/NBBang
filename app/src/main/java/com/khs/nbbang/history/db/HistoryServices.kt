package com.khs.nbbang.history.db

interface HistoryServices {
    fun calculateProgress(historys: List<NBBangHistory>): List<NBBangHistory> {
        //TODO : data 가공
        return historys
    }
}