package com.telemedicine.indihealth.binding

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("setHtmlText")
fun setHtmlText(view: TextView, string: String) {
    view.text = HtmlCompat.fromHtml(string, 0)
}