package com.prototype.aishiteru.classes
import com.prototype.aishiteru.classes.CustomDate

class MessageItem(avatar:  Int,
                  text:     String,
                  time:     CustomDate,
                  senderId: String)
{
    var avatar = avatar
        private set

    var text = text
        private set

    var time = time
        private set

    var senderId = senderId
        private set
}