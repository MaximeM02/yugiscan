package com.improcorp.myapplication.tool

import android.content.Context
import com.android.volley.RequestQueue
import com.improcorp.myapplication.model.UserCollection

class MyRepository(mContext: Context) {

    private var queue: RequestQueue = VolleySingleton.getInstance(mContext).requestQueue
    private var request: MyRequest = MyRequest(mContext, queue)

    fun register(confirmedPassword: String, callback: MyRequest.RegisterCallback){
        request.register(confirmedPassword, callback)
    }

    fun login(callback: MyRequest.LoginCallback){
        request.login(callback)
    }

    fun getInitializationList(callback: MyRequest.InitializationCallback){
        request.getInitializationList(callback)
    }

    fun getEditions(callback: MyRequest.EditionsCallback){
        request.getEditions(callback)
    }

    fun getCardsInEdition(edition_id: Int, callback: MyRequest.CardsInEditionCallback){
        request.getCardsInEdition(edition_id, callback)
    }

    fun addCardUser(userCollection: UserCollection, callback: MyRequest.AddCardUserCallback){
        request.addCardUser(userCollection, callback)
    }

    fun updateCardUser(userCollection: UserCollection, callback: MyRequest.UpdateCardUserCallback){
        request.updateCardUser(userCollection, callback)
    }

    fun deleteCardUser(userCollection: UserCollection, callback: MyRequest.DeleteCardUserCallback){
        request.deleteCardUser(userCollection, callback)
    }
}