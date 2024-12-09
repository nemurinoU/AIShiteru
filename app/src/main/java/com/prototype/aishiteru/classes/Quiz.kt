package com.prototype.aishiteru.classes

class Quiz {
    var id: Int
        private set
    var title: String
        private set
    var hiScore: Int
        private set
    var imageId: Int
        private set

    constructor(id: Int, title: String, hiScore: Int, imageId: Int) {
        this.id = id
        this.title = title
        this.hiScore = hiScore
        this.imageId = imageId
    }

}