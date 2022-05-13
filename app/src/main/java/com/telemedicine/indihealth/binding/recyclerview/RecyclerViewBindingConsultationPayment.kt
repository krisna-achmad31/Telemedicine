package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.ui.fragment.consultation.payment.ConsultationPaymentAdapter
import timber.log.Timber

@BindingAdapter("setAdapterConsultationPaymentList")
fun bindAdapterConsultationPaymentList(view: RecyclerView, list: List<Payment>?) {
    Timber.d("list payment = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationPaymentAdapter)?.addList(list)
    }
}