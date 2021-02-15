package com.khs.nbbang.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

open class KeyboardUtils() {
    fun hideKeyboard(view: View, context: Context) {
        view ?: return
        context ?: return
        var inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }
}