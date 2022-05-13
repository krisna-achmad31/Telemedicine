package com.telemedicine.indihealth.ui.fragment.history.drug

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemHistoryDrugBinding
import com.telemedicine.indihealth.model.HistoryDrug
import java.util.*

class HistoryDrugAdapter :
    RecyclerView.Adapter<HistoryDrugAdapter.ViewHolder>(), Filterable {

    private var items: MutableList<HistoryDrug> = mutableListOf()

    private var fullItems: MutableList<HistoryDrug> = mutableListOf()

    val clickedItem: MutableLiveData<HistoryDrug> by lazy {
        MutableLiveData<HistoryDrug>()
    }

    class ViewHolder(val binding: ItemHistoryDrugBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemHistoryDrugBinding>(
                inflater,
                R.layout.item_history_drug, parent, false
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
            historyDrug = item
//            if (position == itemCount - 1) {
//                itemConsultationDoctorDivider2.visibility = View.GONE
//            } else {
//                itemConsultationDoctorDivider2.visibility = View.VISIBLE
//            }
            when(item.getStatusBukti){
                "0"-> {
                    itemPaymentStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.blue_600
                            )
                        )
                    }
                }
                "1"-> {
                    itemPaymentStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.green_900
                            )
                        )
                    }
                }
                "2"->{
                    itemPaymentStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.red_a700
                            )
                        )
                    }
                }
                "3"->{
                    itemPaymentStatus.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this.context,
                                R.color.amber_800
                            )
                        )
                    }
                }
            }
        }
    }

    fun addList(list: List<HistoryDrug>) {
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
                    val resultList = mutableListOf<HistoryDrug>()
                    for (row in items) {
                        for (item in row.detail_obat){
                            if (item?.toLowerCase(Locale.getDefault())!!.contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                                resultList.add(row)
                            }
                        }
                    }
                    items = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<HistoryDrug>
                notifyDataSetChanged()
            }
        }
    }
}
