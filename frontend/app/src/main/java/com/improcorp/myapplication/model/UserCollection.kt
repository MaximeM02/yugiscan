package com.improcorp.myapplication.model

data class UserCollection(
    var userId: Int,
    var cardId: Int,
    var quantity: Int,
    var dateInsert: String,
    var dateUpdate: String
)