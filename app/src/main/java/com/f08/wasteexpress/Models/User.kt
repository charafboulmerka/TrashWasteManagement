package com.f08.wasteexpress.Models

class User {
    var id:String?=null
    var email:String?=null
    var first_name:String?=null
    var last_name:String?=null
    var role:String?=null
    constructor(id:String,email:String,first_name:String,last_name:String,role:String){
        this.id=id
        this.email=email
        this.first_name=first_name
        this.last_name=last_name
        this.role=role
    }
    constructor()
}