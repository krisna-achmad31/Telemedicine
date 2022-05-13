package com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telemedicine.indihealth.databinding.ItemAdditionAssesmentBinding

/**
 * Created by Surya Mahadi on 11/27/2021
 */
class AdditionImageAdapter(val context: Context) : ListAdapter<Uri, AdditionImageAdapter.ViewHolder>(
    COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdditionAssesmentBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    inner class ViewHolder(val binding: ItemAdditionAssesmentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Uri) {
            Glide.with(context)
                .load(image)
                .into(binding.imageView)
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return false
            }

        }
    }


}