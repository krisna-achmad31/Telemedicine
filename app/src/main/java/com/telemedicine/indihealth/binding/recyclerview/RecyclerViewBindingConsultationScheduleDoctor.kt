package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule.ConsultationScheduleDoctorAdapter
import timber.log.Timber

@BindingAdapter("setAdapterConsultationScheduleDoctorList")
fun bindAdapterConsultationScheduleDoctorList(view: RecyclerView, list: List<ScheduleDoctorConsultation>?) {
    Timber.d("list payment = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationScheduleDoctorAdapter)?.addList(list)
    }
}