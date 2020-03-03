package com.prueba.kotlinrecycler.Otros

class User {

    var name:String?=null

    var image:String?=null

    var status:String?=null


    constructor(){

    }

    constructor(name: String?, image: String?, status: String?) {
        this.name = name
        this.image = image
        this.status = status
    }
}