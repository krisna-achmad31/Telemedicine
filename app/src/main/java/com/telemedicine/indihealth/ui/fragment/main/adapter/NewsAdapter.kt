package com.telemedicine.indihealth.ui.fragment.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemNewsBinding
import com.telemedicine.indihealth.model.News
import timber.log.Timber


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val items: MutableList<News> by lazy {
        mutableListOf()
    }

    init {
        Timber.d("NewsAdapter is called")
    }

    class ViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemNewsBinding>(
                inflater,
                R.layout.item_news, parent, false
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
            news = item
            executePendingBindings()
            root.setOnClickListener {
                when (item.id) {
//                    "doctor" -> DoctorLoginFragment.startFragment(it)
//                    "patient" -> PatientLoginFragment.startFragment(it)
                }
            }
        }
    }

    fun addList(list: List<News>) {
        Timber.d("NewsAdapter AddList = $list")
        items.addAll(list)
        notifyDataSetChanged()
    }
}
