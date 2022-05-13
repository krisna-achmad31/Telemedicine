package com.telemedicine.indihealth.ui.fragment.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemMedicalRecordsBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.MedicalRecords

class MedicalRecordsAdapter :
    RecyclerView.Adapter<MedicalRecordsAdapter.ViewHolder>() {

    private val items: MutableList<MedicalRecords> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<MedicalRecords>> by lazy {
        MutableLiveData<Event<MedicalRecords>>()
    }

    class ViewHolder(val binding: ItemMedicalRecordsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemMedicalRecordsBinding>(
                inflater,
                R.layout.item_medical_records, parent, false
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
            medicalRecords = item
            this.buttonRekammedisDetail.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<MedicalRecords>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
