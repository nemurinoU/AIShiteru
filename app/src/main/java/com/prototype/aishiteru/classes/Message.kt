package com.prototype.aishiteru.classes

import java.util.Date

data class Message( val text: String,
                    val time: Date,
                    val senderId: String) {
    constructor() : this("", Date(0), "")
}