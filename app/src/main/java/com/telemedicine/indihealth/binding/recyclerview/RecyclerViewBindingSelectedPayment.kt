package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.ListPaymetnMethod
import com.telemedicine.indihealth.ui.fragment.payment.PaymentConfirmationAdapter
import timber.log.Timber

@BindingAdapter("setAdapterListPaymentMethod")
fun bindAdapterListPaymetnMethod(view: RecyclerView, list: List<ListPaymetnMethod>?){
    Timber.d("list va = $list")
    if (list != null){
        Timber.d("VaAdapter is not Null Or Empty = $list")
        (view.adapter as? PaymentConfirmationAdapter)?.addList(list)
    }
}