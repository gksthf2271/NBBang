package com.khs.nbbang.common

import android.view.KeyEvent

interface IKeyEvent {
    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
}