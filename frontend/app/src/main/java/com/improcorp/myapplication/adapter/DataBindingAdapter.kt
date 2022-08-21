package com.improcorp.myapplication.adapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("errorText")
fun setError(tInputLayout: TextInputLayout, str: String?) {
    tInputLayout.error = str
}
