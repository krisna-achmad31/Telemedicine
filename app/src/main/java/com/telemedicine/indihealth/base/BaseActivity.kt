package com.telemedicine.indihealth.base

import android.content.Intent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.telemedicine.indihealth.ui.activity.HomeActivity
import com.telemedicine.indihealth.ui.activity.transition.TransitionViewModel

abstract class BaseActivity : AppCompatActivity(){

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

}