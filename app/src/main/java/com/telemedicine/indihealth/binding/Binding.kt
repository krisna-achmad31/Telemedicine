package com.telemedicine.indihealth.binding

import android.view.View
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

@BindingAdapter("setVisibility")
fun bindVisibility(view: View, boolean: Boolean) {
    Timber.d("bindVisibility = $boolean")
    when(boolean){
        true -> view.visibility = View.VISIBLE
        false -> view.visibility = View.GONE
    }
}

fun checkIfTIETIsEmpty(til: TextInputLayout, boolean: Boolean) : Boolean {
    return if(boolean){
        til.isErrorEnabled = true
        til.requestFocus()
        til.error = "Kotak ini tidak boleh kosong"
        true
    } else {
        til.isErrorEnabled = false
        til.clearFocus()
        til.error = ""
        false
    }
}

fun checkIfRadioButtonIsEmpty(radioButton: RadioButton,boolean: Boolean): Boolean{
    return if(boolean){
        radioButton.error = "Pilih salah satu dari pilihan ini"
        radioButton.requestFocus()
        radioButton.requestFocusFromTouch()
        true
    } else {
        radioButton.clearFocus()
        false
    }
}