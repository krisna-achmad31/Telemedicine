package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.DoctorAssesment
import com.telemedicine.indihealth.ui.fragment.doctor.assesment.DoctorAssesmentPatientAdapter
import timber.log.Timber

@BindingAdapter("setAdapterDoctorAssesmentPatientList")
fun bindAdapterDoctorAssesmentPatientList(view: RecyclerView, list: List<DoctorAssesment>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? DoctorAssesmentPatientAdapter)?.addList(list)
    }
}