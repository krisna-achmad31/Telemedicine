package com.telemedicine.indihealth.ui.fragment.consultation.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemConsultationPaymentBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.model.Payment

class ConsultationPaymentAdapter :
    RecyclerView.Adapter<ConsultationPaymentAdapter.ViewHolder>() {

    private val items: MutableList<Payment> by lazy {
        mutableListOf()
    }

    val clickedItem: SingleLiveData<Event<Payment>> by lazy {
        SingleLiveData<Event<Payment>>()
    }

    val clickedCanceledItem: SingleLiveData<Event<Payment>> by lazy {
        SingleLiveData<Event<Payment>>()
    }

    class ViewHolder(val binding: ItemConsultationPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemConsultationPaymentBinding>(
                inflater,
                R.layout.item_consultation_payment, parent, false
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
            payment = item
            when (item.id_status_pembayaran) {
                "1" -> {
                    buttonNegative.visibility = View.GONE
                    buttonPositive.visibility = View.GONE
                    buttonNeutral.visibility = View.VISIBLE
                }
                "2"->{
                    buttonNegative.text = "Batalkan Pembayaran"
                    buttonPositive.text = item.keterangan
                }
                "0"->{
                    buttonPositive.text = "Bayar"
                    buttonNegative.text = "Hapus Pendaftaran"
                    buttonNegative.visibility = View.VISIBLE
                    buttonPositive.visibility = View.VISIBLE
                    buttonNeutral.visibility = View.GONE
                }
            }
            buttonPositive.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
            buttonNegative.setOnClickListener {
                clickedCanceledItem.postValue(Event(item))
            }
            buttonNeutral.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<Payment>) {
        items.clear()
        notifyDataSetChanged()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
