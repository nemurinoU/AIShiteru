package com.prototype.aishiteru

import com.prototype.aishiteru.classes.CastItem

interface DataListener {
    fun onDataReceived(data: CastItem)
}