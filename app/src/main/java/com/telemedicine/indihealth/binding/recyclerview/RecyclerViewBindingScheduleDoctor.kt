package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.ConsultationDoctor
import com.telemedicine.indihealth.model.FilterRegistrationDoctor
import com.telemedicine.indihealth.ui.fragment.schedule.ScheduleDoctorAdapter
import com.telemedicine.indihealth.ui.fragment.schedule.adapter.ScheduleDoctorAdapterFilterPoly
import timber.log.Timber

@BindingAdapter("setAdapterScheduleDoctorList")
fun bindAdapterScheduleDoctorList(view: RecyclerView, list: List<ConsultationDoctor>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ScheduleDoctorAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterScheduleDoctorFilterPolyList")
fun bindAdapterScheduleDoctorFilterPolyList(view: RecyclerView, list: List<FilterRegistrationDoctor>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ScheduleDoctorAdapterFilterPoly)?.addList(list)
    }
}