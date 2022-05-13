package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Schedule
import com.telemedicine.indihealth.ui.fragment.consultation.schedule.ConsultationScheduleAdapter
import timber.log.Timber

@BindingAdapter("setAdapterConsultationScheduleList")
fun bindAdapterConsultationScheduleList(view: RecyclerView, list: List<Schedule>?) {
    Timber.d("list payment = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ConsultationScheduleAdapter)?.addList(list)
    }
}