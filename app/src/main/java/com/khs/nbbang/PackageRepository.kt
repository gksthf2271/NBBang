package com.khs.nbbang

import android.content.Context

class PackageRepository(context: Context) {
    val packageName: String
    val context: Context

    init {
        this.packageName = context.packageName
        this.context = context
    }
}