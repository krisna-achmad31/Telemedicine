package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.ui.activity.prescription.MedicineAdapter
import timber.log.Timber

@BindingAdapter("setAdapterMedicineList")
fun bindAdapterMedicineList(view: RecyclerView, list: List<Medicine>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? MedicineAdapter)?.addList(list)
    }
}
