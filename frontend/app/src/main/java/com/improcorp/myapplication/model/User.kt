package com.improcorp.myapplication.model

class User{

    companion object {
        var mId: Int? = null
        var mPseudo: String? = null
        var mPassword: String? = null
        var mEmailAddress: String? = null
        var mLastName: String? = null
        var mFirstName: String? = null
        var mRegistrationDate: String? = null

        fun clear(){
            mId = null
            mPseudo = null
            mPassword = null
            mEmailAddress = null
            mLastName = null
            mFirstName = null
            mRegistrationDate = null
        }

        override fun toString(): String = "Id: $mId, Pseudo: $mPseudo, Password: $mPassword, " +
                    "Email Address: $mEmailAddress, Last Name: $mLastName, " +
                    "First Name: $mFirstName, Registration Date: $mRegistrationDate"
    }
}