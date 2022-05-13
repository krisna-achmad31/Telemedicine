package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.DoctorSchedule
import com.telemedicine.indihealth.ui.fragment.doctor.schedule.DoctorScheduleAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDoctorScheduleList")
fun bindAdapterDoctorScheduleList(view: RecyclerView, list: List<DoctorSchedule>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DoctorScheduleAdapter)?.addList(list)
    }
}