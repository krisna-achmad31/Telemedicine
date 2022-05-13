package com.telemedicine.indihealth.ui.fragment.consultation.registration

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationRegistrationDoctorBinding
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.model.ConsultationDoctor
import timber.log.Timber
import java.util.*


class ConsultationRegistrationAdapter :
    RecyclerView.Adapter<ConsultationRegistrationAdapter.ViewHolder>() , Filterable {

    private var items: MutableList<ConsultationDoctor> = mutableListOf()

    private var fullItems: MutableList<ConsultationDoctor> = mutableListOf()

    val clickedItem: SingleLiveData<ConsultationDoctor> by lazy {
        SingleLiveData<ConsultationDoctor>()
    }

    class ViewHolder(val binding: ItemConsultationRegistrationDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationRegistrationDoctorBinding>(
                inflater,
                R.layout.item_consultation_registration_doctor, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        Timber.d("Consultation Items ${items.size}")
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            consultationDoctor = item
            button.setOnClickListener {
                clickedItem.postValue(item)
            }
        }
    }

    fun addList(list: List<ConsultationDoctor>) {
        items.clear()
        items.addAll(list)
        fullItems.clear()
        fullItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    items = fullItems
                } else {
                    items=fullItems
                    val resultList = mutableListOf<ConsultationDoctor>()
                    for (row in items) {
                        if (row.name.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    items = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<ConsultationDoctor>
                notifyDataSetChanged()
            }
        }
    }
}
