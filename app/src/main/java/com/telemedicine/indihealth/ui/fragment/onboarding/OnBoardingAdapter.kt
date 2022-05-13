package com.telemedicine.indihealth.ui.fragment.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.databinding.LayoutSlideBinding
import com.telemedicine.indihealth.model.OnBoardingModel

class OnBoardingAdapter(private val onBoardingItems : List<OnBoardingModel>)
    : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    class ViewHolder(private var binding: LayoutSlideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onBoardingModel: OnBoardingModel, position: Int) {
            binding.imageView.setImageResource(onBoardingModel.icon)
            binding.textView.text = onBoardingModel.title
            binding.textView2.text = onBoardingModel.description
            if(position == 2){
                binding.imageView.scaleType = ImageView.ScaleType.FIT_END
            }else {
                binding.imageView.scaleType = ImageView.ScaleType.CENTER
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSlideBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onBoardingItems[position], position)
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }
}