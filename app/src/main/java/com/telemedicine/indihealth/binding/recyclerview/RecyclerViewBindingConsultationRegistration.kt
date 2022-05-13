package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.ConsultationDoctor
import com.telemedicine.indihealth.model.FilterRegistrationDoctor
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationAdapter
import com.telemedicine.indihealth.ui.fragment.consultation.registration.adapter.ConsultationRegistrationAdapterFilterDay
import com.telemedicine.indihealth.ui.fragment.consultation.registration.adapter.ConsultationRegistrationAdapterFilterPoly
import timber.log.Timber

@BindingAdapter("setAdapterConsultationDoctorList")
fun bindAdapterConsultationDoctorList(view: RecyclerView, list: List<ConsultationDoctor>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationRegistrationAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterConsultationFilterPolyList")
fun bindAdapterConsultationFilterPolyList(view: RecyclerView, list: List<FilterRegistrationDoctor>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationRegistrationAdapterFilterPoly)?.addList(list)
    }
}

@BindingAdapter("setAdapterConsultationFilterDayList")
fun bindAdapterConsultationFilterDayList(view: RecyclerView, list: List<FilterRegistrationDoctor>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationRegistrationAdapterFilterDay)?.addList(list)
    }
}