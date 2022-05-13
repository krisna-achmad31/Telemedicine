package com.telemedicine.indihealth.helper

import android.content.Context
import android.widget.Toast

object AppToast {

    fun set(context: Context,string: String){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
    }
}