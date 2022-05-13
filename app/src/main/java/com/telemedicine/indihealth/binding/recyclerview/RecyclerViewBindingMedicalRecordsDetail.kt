package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Drug
import com.telemedicine.indihealth.ui.fragment.records.detail.MedicalRecordsDetailAdapter
import timber.log.Timber

@BindingAdapter("setAdapterMedicalRecordsDetailList")
fun bindAdapterMedicalRecordsDetailList(view: RecyclerView, list: List<Drug>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? MedicalRecordsDetailAdapter)?.addList(list)
    }
}