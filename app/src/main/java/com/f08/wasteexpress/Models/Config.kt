package com.f08.wasteexpress.Models

class Config {
    var buying_price_per_kg:String?=null
    var selling_fee_per_kg:String?=null
    constructor(buying_price_per_kg:String,selling_fee_per_kg:String){
        this.buying_price_per_kg=buying_price_per_kg
        this.selling_fee_per_kg=selling_fee_per_kg
    }
    constructor()
}