package com.prototype.aishiteru.classes

// regular users have their userId as their email
// lvl is set to -1
// name is just their username
// imageId is 0
class CastItem (name: String, imageId : Int, userId : String) {
    var name = name
        private set

    var imageId = imageId
        private set

    var userId = userId
        private set
}