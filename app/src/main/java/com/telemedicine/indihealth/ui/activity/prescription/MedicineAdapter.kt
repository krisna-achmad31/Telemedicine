package com.telemedicine.indihealth.ui.activity.prescription

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemMedicineBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Medicine
import timber.log.Timber


class MedicineAdapter : RecyclerView.Adapter<MedicineAdapter.ViewHolder>(), Filterable {

    private var items: MutableList<Medicine> = mutableListOf()
    private var itemList = items

    val clickedItem: MutableLiveData<Event<Medicine>> by lazy {
        MutableLiveData<Event<Medicine>>()
    }

    init {
        Timber.d("MedicineAdapter is called")
    }

    class ViewHolder(val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemMedicineBinding>(
                inflater,
                R.layout.item_medicine, parent, false
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
            medicine = item
            executePendingBindings()
            root.setOnClickListener {
                clickedItem.value = Event(item)
            }
        }
    }

    fun addList(list: List<Medicine>) {
        Timber.d("MedicineAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    items = itemList
                } else {
                    val filteredList = ArrayList<Medicine>()
                    for (row in itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        row.name?.let {
                            if(it.toLowerCase().contains(charString.toLowerCase())){
                                filteredList.add(row)
                            }
                        }
                    }
                    items = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                items = filterResults.values as ArrayList<Medicine>
                notifyDataSetChanged()
            }
        }
    }

}
