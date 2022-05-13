package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Diagnosis
import com.telemedicine.indihealth.ui.activity.diagnosis.DiagnosisAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDiagnosisList")
fun bindAdapterDiagnosisList(view: RecyclerView, list: List<Diagnosis>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DiagnosisAdapter)?.addList(list)
    }
}
