package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.DoctorHistoryLog
import com.telemedicine.indihealth.ui.fragment.doctor.history.DoctorHistoryLogAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDoctorHistoryLogList")
fun bindAdapterDoctorHistoryLogList(view: RecyclerView, list: List<DoctorHistoryLog>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DoctorHistoryLogAdapter)?.addList(list)
    }
}