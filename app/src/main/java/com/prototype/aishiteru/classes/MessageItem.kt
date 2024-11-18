package com.prototype.aishiteru.classes
import com.prototype.aishiteru.classes.CustomDate

class MessageItem(text:         String,
                  time:         CustomDate,
                  sender:       CastItem,
                  recipient:    CastItem)
{
    var text = text
        private set

    var time = time
        private set

    var sender = sender
        private set

    var avatar = sender.imageId
        private set

    var recipient = recipient
        private set
}