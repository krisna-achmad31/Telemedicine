package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.ProfileDoctor
import com.telemedicine.indihealth.ui.fragment.profile.doctor.ProfileDoctorAdapter
import timber.log.Timber

@BindingAdapter("setAdapterProfileDoctorList")
fun bindAdapterProfileDoctorList(view: RecyclerView, list: List<ProfileDoctor>?) {
	Timber.d("list profileDoctor = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? ProfileDoctorAdapter)?.addList(list)
    }
}