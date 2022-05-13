package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.ui.fragment.recipe.RecipeDoctorAdapter
import timber.log.Timber

@BindingAdapter("setAdapterRecipeDoctorList")
fun bindAdapterRecipeDoctorList(view: RecyclerView, list: List<Recipe>?) {
    Timber.d("list recipe = $list")
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? RecipeDoctorAdapter)?.addList(list)
    }
}