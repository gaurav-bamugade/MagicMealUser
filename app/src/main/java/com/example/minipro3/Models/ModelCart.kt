package com.example.minipro3.Models

import java.io.Serializable

class ModelCart(
    var imageoffood : String,
    var nameoffood : String,
    var priceoffood : String,
    var offeroffood : String,
    val qtyoffood : String,
    val descoffood : String,
    val categoryoffood : String
) : Serializable
{
    var foodid:String=""

    fun set(id:String){
        this.foodid=id
    }
}