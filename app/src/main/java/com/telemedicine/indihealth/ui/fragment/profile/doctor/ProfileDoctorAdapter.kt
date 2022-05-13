package com.telemedicine.indihealth.ui.fragment.profile.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemProfileDoctorBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.ProfileDoctor


class ProfileDoctorAdapter :
    RecyclerView.Adapter<ProfileDoctorAdapter.ViewHolder>() {

    private val items: MutableList<ProfileDoctor> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<ProfileDoctor>> by lazy {
        MutableLiveData<Event<ProfileDoctor>>()
    }

    class ViewHolder(val binding: ItemProfileDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemProfileDoctorBinding>(
                inflater,
                R.layout.item_profile_doctor, parent, false
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
            profileDoctor = item
            button.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<ProfileDoctor>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
