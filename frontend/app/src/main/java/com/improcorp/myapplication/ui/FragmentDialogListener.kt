package com.improcorp.myapplication.ui

import com.improcorp.myapplication.model.Card
import com.improcorp.myapplication.model.Edition

interface FragmentDialogListener {

    fun handleDialogClose(edition: Edition)

}