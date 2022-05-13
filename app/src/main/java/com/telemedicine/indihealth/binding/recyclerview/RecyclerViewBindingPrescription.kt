package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Prescription
import com.telemedicine.indihealth.ui.bottomsheet.prescription.ConsultationPrescriptionAdapter
import timber.log.Timber

@BindingAdapter("setAdapterPrescriptionList")
fun bindAdapterPrescriptionList(view: RecyclerView, list: List<Prescription>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationPrescriptionAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterPrescription")
fun bindAdapterPrescription(view: RecyclerView, list: Prescription) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationPrescriptionAdapter)?.add(list)
    }
}
