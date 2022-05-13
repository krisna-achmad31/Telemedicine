package com.telemedicine.indihealth.ui.bottomsheet.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemChat1Binding
import com.telemedicine.indihealth.databinding.ItemChat2Binding
import com.telemedicine.indihealth.model.Chat
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.activity.image.ImageActivity
import timber.log.Timber


class ConsultationChatAdapter(
    private val options: FirebaseRecyclerOptions<Chat>,
    private val role: String?
) : FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder>(options) {

    private lateinit var items: ScheduleDoctorConsultation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Timber.d("onCreateViewHolder ViewType = $viewType")
        val binding1 =
            DataBindingUtil.inflate<ItemChat1Binding>(
                inflater,
                R.layout.item_chat_1, parent, false
            )
        val binding2 =
            DataBindingUtil.inflate<ItemChat2Binding>(
                inflater,
                R.layout.item_chat_2, parent, false
            )
        return if (viewType == R.layout.item_chat_1) {
            ViewHolderChat1(binding1)
        } else {
            ViewHolderChat2(binding2)
        }
    }

    class ViewHolderChat1(val binding: ItemChat1Binding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolderChat2(val binding: ItemChat2Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Chat) {
        if (getItemViewType(position) == R.layout.item_chat_1) {
            val holders = holder as ViewHolderChat1
            holders.binding.apply {
                chat = model
                schedule = items
                itemChat1IvImage.setOnClickListener {
                    ImageActivity.startActivity(it.context,"${items.id_dokter}_${items.id_pasien}/${model.file}")
                }
            }
        } else {
            val holders = holder as ViewHolderChat2
            holders.binding.apply {
                chat = model
                schedule = items
                executePendingBindings()
                itemChat2IvImage.setOnClickListener {
                    ImageActivity.startActivity(it.context,"${items.id_dokter}_${items.id_pasien}/${model.file}")
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messageData = getItem(position)
        return if (!messageData.type.equals(role, ignoreCase = true)) {
            R.layout.item_chat_2
        } else {
            R.layout.item_chat_1
        }
    }

    fun add(list: ScheduleDoctorConsultation?) {
        items = list!!
        notifyDataSetChanged()
    }
}