package com.improcorp.myapplication.model

import kotlin.math.round
import kotlin.math.roundToLong

data class Edition(
        val mId: Int,
        val mCode: String,
        val mName: String,
        val mReleaseDate: String,
        val mTotalNumberOfCards: Int,
        val mImageUrl: String,
        var mCategory: String,
        var mUserNumberOfCards: Int,
        var mCompletion: Double,
        var mIsCardsLoaded: Boolean,
        var mCardList: ArrayList<Card>){

        init { calculateCompletion() }

        fun calculateCompletion(){
                mCompletion = "%.2f".format(mUserNumberOfCards.toDouble() /mTotalNumberOfCards.toDouble()).replace(',', '.').toDouble() * 100
        }
}


