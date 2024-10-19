package com.prototype.aishiteru.classes

class News {
    var difficulty: String
        private set
    var header: String
        private set
    var date: CustomDate
        private set
    var imageId: Int
        private set
    var content: String
        private set

    constructor(difficulty: String, header: String, date: CustomDate, imageId: Int, content: String) {
        this.difficulty = difficulty
        this.header = header
        this.date = date
        this.imageId = imageId
        this.content = content
    }



}