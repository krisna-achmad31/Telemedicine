package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.HistoryConsultation
import com.telemedicine.indihealth.model.HistoryDrug
import com.telemedicine.indihealth.ui.fragment.history.consultation.HistoryConsultationAdapter
import com.telemedicine.indihealth.ui.fragment.history.drug.HistoryDrugAdapter
import timber.log.Timber

@BindingAdapter("setAdapterHistoryConsultationList")
fun bindAdapterHistoryConsultationList(view: RecyclerView, list: List<HistoryConsultation>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? HistoryConsultationAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterHistoryDrugList")
fun bindAdapterHistoryDrugList(view: RecyclerView, list: List<HistoryDrug>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? HistoryDrugAdapter)?.addList(list)
    }
}