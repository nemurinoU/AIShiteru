package com.prototype.aishiteru.classes

class News {
    var difficulty: String
    var header: String
    var date: CustomDate
    var imageUrl: String
    var content: String

    constructor(difficulty: String, header: String, date: CustomDate, imageUrl: String, content: String) {
        this.difficulty = difficulty
        this.header = header
        this.date = date
        this.imageUrl = imageUrl
        this.content = content
    }
}