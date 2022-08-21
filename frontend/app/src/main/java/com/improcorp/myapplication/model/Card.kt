package com.improcorp.myapplication.model

import android.service.autofill.FieldClassification
import java.io.Serializable

class Card(
        var mId: Int,
        var mEdition: String,
        var mCode: String,
        var mName: String,
        var mRarity: String,
        var mAttribute: String,
        var mLevel: String,
        var mAttack: String,
        var mDefense: String,
        var mType: String,
        var mText: String,
        var mTemplateId: Int,
        var mLinkClassification: String,
        var mImageUrl: String,
        var mUserQuantity: Int,
        var mQuantityInEdition: Int,
        var mInsertDate: String?,
        var mUpdateDate: String?): Serializable