package com.prototype.aishiteru.classes

class Achievement {
    var id: Int
        private set
    var title: String
        private set
    var description: String
        private set
    var imageId: Int
        private set
    var currentProgress: Int
        private set
    var maxProgress: Int
        private set
    var achieved: Boolean
        private set

    constructor(id: Int, title: String, description: String, imageId: Int, currentProgress: Int, maxProgress: Int, achieved: Boolean) {
        this.id = id
        this.title = title
        this.description = description
        this.imageId = imageId
        this.currentProgress = currentProgress
        this.maxProgress = maxProgress
        this.achieved = achieved
    }


}