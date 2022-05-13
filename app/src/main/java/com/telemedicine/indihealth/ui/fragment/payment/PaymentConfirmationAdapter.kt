package com.telemedicine.indihealth.ui.fragment.payment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemDoctorHistoryLogBinding
import com.telemedicine.indihealth.databinding.ItemPaymentListSelectedMethodBinding
import com.telemedicine.indihealth.helper.AppVar.BASE_WEB_URL
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.helper.SingleLiveData
import com.telemedicine.indihealth.model.DoctorHistoryLog
import com.telemedicine.indihealth.model.ListPaymetnMethod
import kotlinx.android.synthetic.main.item_payment_list_selected_method.view.*
import timber.log.Timber

class PaymentConfirmationAdapter : RecyclerView.Adapter<PaymentConfirmationAdapter.ViewHolder>() {

    private val items: MutableList<ListPaymetnMethod> by lazy {
        mutableListOf()
    }

    var select = -1;

    val clickedItem: SingleLiveData<Event<ListPaymetnMethod>> by lazy {
        SingleLiveData<Event<ListPaymetnMethod>>()
    }

    class ViewHolder(val binding: ItemPaymentListSelectedMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemPaymentListSelectedMethodBinding>(
                inflater,
                R.layout.item_payment_list_selected_method, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            if (item.selected){
                root.container.setBackgroundColor(Color.argb(255, 224, 224,255))
            } else {
                root.container.setBackgroundColor(Color.argb(255, 255, 252,252))
            }
            root.card.setOnClickListener {
                if (position != select && select > -1){
                    //select = position
                    item.selected = true
                    items[select].selected = false
                    select = position
                    notifyDataSetChanged()
                } else {
                    item.selected = true
                    select = position
                    notifyDataSetChanged()
                }

                clickedItem.postValue(Event(item))

            }
            Glide.with(root).load("${BASE_WEB_URL}/${item.logo}").into(root.img_va)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addList(list: List<ListPaymetnMethod>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}


