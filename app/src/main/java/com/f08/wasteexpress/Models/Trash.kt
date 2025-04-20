package com.f08.wasteexpress.Models

class Trash {
    var id:String?=null
    var userId:String?=null
    var personalInfo:Personalnfo?=null
    var title:String?=null
    var description:String?=null
    var qty:String?=null
    var trash_type:Int?=null
    var trash_sub_type:Int?=null
    var unite:String?=null
    var price_per_kg:String?=null
    var total:String?=null
    var publishedDate:String?=null
    var publishedDateMs:String?=null
    var status:Int?=null
    var wilaya:Int?=null
    var images:ArrayList<String>?=null


    constructor(id:String,userId:String,personalInfo:Personalnfo,title:String,description:String,
                qty:String,trash_type:Int,trash_sub_type:Int,unite:String,price_per_kg:String
                ,total:String, publishedDate:String, publishedDateMs:String,status:Int,wilaya:Int,images:ArrayList<String>){
        this.id=id
        this.userId=userId
        this.personalInfo=personalInfo
        this.title=title
        this.description=description
        this.qty=qty
        this.trash_type=trash_type
        this.trash_sub_type=trash_sub_type
        this.unite=unite
        this.price_per_kg=price_per_kg
        this.total=total
        this.publishedDate=publishedDate
        this.publishedDateMs=publishedDateMs
        this.status=status
        this.wilaya=wilaya
        this.images=images

    }

    constructor()
}