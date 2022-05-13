package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.MedicalRecords
import com.telemedicine.indihealth.ui.fragment.records.MedicalRecordsAdapter
import timber.log.Timber

@BindingAdapter("setAdapterMedicalRecordsList")
fun bindAdapterMedicalRecordsList(view: RecyclerView, list: List<MedicalRecords>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? MedicalRecordsAdapter)?.addList(list)
    }
}