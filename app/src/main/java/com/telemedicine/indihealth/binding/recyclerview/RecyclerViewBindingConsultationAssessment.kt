package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Assessment
import com.telemedicine.indihealth.ui.fragment.consultation.assessment.list.AssessmentListAdapter
import timber.log.Timber

@BindingAdapter("setAdapterAssessmentList")
fun bindAdapterAssessmentList(view: RecyclerView, list: List<Assessment>?){
    if (list != null){
        Timber.d("AssessmentAdapter is not Null Or Empty = $list")
        (view.adapter as AssessmentListAdapter).addList(list)
    }
}