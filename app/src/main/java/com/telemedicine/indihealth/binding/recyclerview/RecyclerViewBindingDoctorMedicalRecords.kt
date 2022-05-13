package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.ui.fragment.doctor.records.DoctorMedicalRecordsAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDoctorMedicalRecordsList")
fun bindAdapterDoctorMedicalRecordsList(view: RecyclerView, list: List<MedicalRecords>?) {
    Timber.d("list medicalRecords = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DoctorMedicalRecordsAdapter)?.addList(list)
    }
}