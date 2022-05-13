package com.telemedicine.indihealth.ui.fragment.recipe.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemRecipeDoctorDetailBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.RecipeDetail

class RecipeDoctorDetailAdapter :
    RecyclerView.Adapter<RecipeDoctorDetailAdapter.ViewHolder>() {

    private val items: MutableList<RecipeDetail> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<RecipeDetail>> by lazy {
        MutableLiveData<Event<RecipeDetail>>()
    }

    class ViewHolder(val binding: ItemRecipeDoctorDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemRecipeDoctorDetailBinding>(
                inflater,
                R.layout.item_recipe_doctor_detail, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            recipeDetail = item
            root.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<RecipeDetail>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
