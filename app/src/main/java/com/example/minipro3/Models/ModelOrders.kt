package com.example.minipro3.Models

class ModelOrders(
    var city : String,
    var deliverytime : String,
    var home : String,
    var landmark : String,
    var mobno : String,
    var name : String,
    var orderedat : String,
    var pin : String,
    var road : String,
    var state : String,
    var totalprice : String,
    var uid : String,
    var statusoforder : String
)
{
    //Class def
    var foodid :String = ""

    fun set(id:String){
        this.foodid=id
    }
}