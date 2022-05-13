package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Drug
import com.telemedicine.indihealth.ui.fragment.doctor.records.detail.DoctorMedicalRecordsDetailAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDoctorMedicalRecordsDetailList")
fun bindAdapterDoctorMedicalRecordsDetailList(view: RecyclerView, list: List<Drug>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DoctorMedicalRecordsDetailAdapter)?.addList(list)
    }
}