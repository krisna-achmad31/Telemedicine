package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.MenuKonsultasi
import com.telemedicine.indihealth.model.News
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorMenuAdapter
import com.telemedicine.indihealth.ui.fragment.main.adapter.MenuKonsultasiAdapter
import com.telemedicine.indihealth.ui.fragment.main.adapter.MenuLainnyaAdapter
import com.telemedicine.indihealth.ui.fragment.main.adapter.NewsAdapter
import timber.log.Timber

@BindingAdapter("setAdapterMenuKonsultasi")
fun bindAdapterMenuKonsultasi(view: RecyclerView, list: List<MenuKonsultasi>?) {
    if(!list.isNullOrEmpty()){
        Timber.d("MenuKonsultasiList is not Null Or Empty = $list")
        (view.adapter as? MenuKonsultasiAdapter)?.addList(list)
    }
}


@BindingAdapter("setAdapterMenuLainnya")
fun bindAdapterMenuLainnya(view: RecyclerView, list: List<MenuKonsultasi>?) {
    if(!list.isNullOrEmpty()){
        Timber.d("MenuLainnyaList is not Null Or Empty = $list")
        (view.adapter as? MenuLainnyaAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterNews")
fun bindAdapterNews(view: RecyclerView, list: List<News>?) {
    if(!list.isNullOrEmpty()){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? NewsAdapter)?.addList(list)
    }
}

@BindingAdapter("setAdapterConsultationMenuDoctor")
fun bindAdapterConsultationMenuDoctor(view: RecyclerView, list: List<MenuKonsultasi>?) {
    if(!list.isNullOrEmpty()){
        Timber.d("MenuKonsultasiList is not Null Or Empty = $list")
        (view.adapter as? MainDoctorMenuAdapter)?.addList(list)
    }
}