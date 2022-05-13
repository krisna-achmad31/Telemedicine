package com.telemedicine.indihealth.ui.fragment.history.consultation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemHistoryConsultationBinding
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.model.HistoryConsultation
import java.util.*

class HistoryConsultationAdapter :
    RecyclerView.Adapter<HistoryConsultationAdapter.ViewHolder>(), Filterable {

    private var items: MutableList<HistoryConsultation> = mutableListOf()

    private var fullItems: MutableList<HistoryConsultation> = mutableListOf()

    val clickedItem = SingleLiveData<HistoryConsultation>()

    class ViewHolder(val binding: ItemHistoryConsultationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemHistoryConsultationBinding>(
                inflater,
                R.layout.item_history_consultation, parent, false
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
            historyConsultation = item
            when(item.getStatusBukti){
                "0" -> {
                    itemHistoryConsultationTvStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.blue_600
                            )
                        )
                    }
                }
                "1" -> {
                    itemHistoryConsultationTvStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.green_900
                            )
                        )
                    }
                }
                "2" -> {
                    itemHistoryConsultationTvStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.red_a700
                            )
                        )
                    }
                }
                "3" -> {
                    itemHistoryConsultationTvStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.amber_800
                            )
                        )
                    }
                }
            }
//            button.setOnClickListener {
//                clickedItem.postValue(item)
//            }
        }
    }

    fun addList(list: List<HistoryConsultation>) {
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
                    val resultList = mutableListOf<HistoryConsultation>()
                    for (row in items) {
                        if (row.nama_dokter.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
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
                items = results?.values as MutableList<HistoryConsultation>
                notifyDataSetChanged()
            }
        }
    }
}
