package com.telemedicine.indihealth.ui.fragment.doctor.assesment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDoctorAssesmentPatientBinding
import com.telemedicine.indihealth.model.DoctorAssesment


class DoctorAssesmentPatientAdapter :
    RecyclerView.Adapter<DoctorAssesmentPatientAdapter.ViewHolder>() {

    private val items: MutableList<DoctorAssesment> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<DoctorAssesment> by lazy {
        MutableLiveData<DoctorAssesment>()
    }

    class ViewHolder(val binding: ItemDoctorAssesmentPatientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDoctorAssesmentPatientBinding>(
                inflater,
                R.layout.item_doctor_assesment_patient, parent, false
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
            doctorAssesment = item
            root.setOnClickListener {
                clickedItem.postValue(item)
            }
        }
    }

    fun addList(list: List<DoctorAssesment>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
