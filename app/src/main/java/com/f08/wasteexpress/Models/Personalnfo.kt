package com.f08.wasteexpress.Models

class Personalnfo {
    var country:String?=null
    var social_status:String?=null
    var age:String?=null
    var gender:String?=null
    var job_status:String?=null
    //MAYBE I'LL ADD ORDER TYPE HERE
    constructor(country:String,social_status:String,age:String,gender:String,job_status:String){
        this.country=country
        this.social_status=social_status
        this.age=age
        this.gender=gender
        this.job_status=job_status
    }

    constructor()
}