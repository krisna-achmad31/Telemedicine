package com.telemedicine.indihealth.binding.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.model.RecipeDetail
import com.telemedicine.indihealth.ui.fragment.recipe.detail.RecipeDoctorDetailAdapter
import timber.log.Timber

@BindingAdapter("setAdapterRecipeDoctorDetailList")
fun bindAdapterRecipeDoctorDetailList(view: RecyclerView, list: List<RecipeDetail>?) {
    if(list != null ){
        Timber.d("NewsAdapter is not Null Or Empty = $list")
        (view.adapter as? RecipeDoctorDetailAdapter)?.addList(list)
    }
}