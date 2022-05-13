package com.telemedicine.indihealth.ui.fragment.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.ItemRecipeDoctorBinding
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.model.Recipe
import timber.log.Timber


class RecipeDoctorAdapter :
    RecyclerView.Adapter<RecipeDoctorAdapter.ViewHolder>() {

    private val TAG="com.telemedicine.indihealth.ui.fragment.recipe.RecipeDoctorAdapter"

    private val items: MutableList<Recipe> by lazy {
        mutableListOf()
    }

    val clickedItem: MutableLiveData<Event<Recipe>> by lazy {
        MutableLiveData<Event<Recipe>>()
    }

    val clickedCanceledItem: MutableLiveData<Event<Recipe>> by lazy {
        MutableLiveData<Event<Recipe>>()
    }

    class ViewHolder(val binding: ItemRecipeDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemRecipeDoctorBinding>(
                inflater,
                R.layout.item_recipe_doctor, parent, false
            )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        Timber.d("$TAG: ${items.size}")
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            recipe = item
//            when(item.getStatusPembayaran){
//                "3"-> {
//                    recipePaymentBtnConfirm.apply {
//                        setBackgroundColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.amber_800
//                            )
//                        )
//                        text = "Bayar"
//                        isEnabled = true
//                    }
//                    recipePaymentBtnCancel.visibility = View.VISIBLE
//                }
//                "0"->{
//                    recipePaymentBtnConfirm.apply {
//                        setBackgroundColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.blue_600
//                            )
//                        )
//                        setTextColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.white
//                            )
//                        )
//                        text = "Sedang Diproses"
//                        isEnabled = true
//                    }
//                    recipePaymentBtnCancel.visibility = View.VISIBLE
//                }
//                "1"-> {
//                    recipePaymentBtnConfirm.apply {
//                        setBackgroundColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.green_900
//                            )
//                        )
//                        setTextColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.white
//                            )
//                        )
//                        text = "Lunas"
//                        isEnabled = false
//                    }
//                    recipePaymentBtnCancel.visibility = View.GONE
//                }
//                "2"-> {
//                    recipePaymentBtnConfirm.apply {
//                        setBackgroundColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.red_a700
//                            )
//                        )
//                        setTextColor(
//                            ContextCompat.getColor(
//                                this.context,
//                                R.color.white
//                            )
//                        )
//                        text = "Ditolak"
//                        isEnabled = true
//                    }
//                    recipePaymentBtnCancel.visibility = View.GONE
//                }
//            }
//            when(item.dibatalkan){
//                "1"-> {
//                    recipePaymentBtnCancel.visibility = View.GONE
//                    recipePaymentBtnConfirm.visibility = View.GONE
//                    tvDibatalkan.visibility = View.VISIBLE
//                }
//            }
            if (item.dibatalkan=="0"){
                when (item.getStatusPembayaran) {
                    "1" -> {
                        buttonNegative.visibility = View.GONE
                        buttonPositive.visibility = View.GONE
                        buttonNeutral.visibility = View.VISIBLE
                    }
                    "0"->{
                        buttonNegative.text = "Batalkan Pembayaran"
                        buttonPositive.text = "Sedang Diproses"
                        buttonPositive.isEnabled = true
                        buttonPositive.setTextColor(ResourcesCompat.getColor(holder.itemView.resources,R.color.white,null))
                        buttonNeutral.visibility = View.GONE
                        buttonNegative.visibility = View.VISIBLE
                        buttonPositive.visibility = View.VISIBLE
                    }
                    else -> {
                        buttonPositive.text = "Bayar"
                        buttonNegative.text = "Hapus Resep"
                        buttonPositive.setTextColor(ResourcesCompat.getColor(holder.itemView.resources,R.color.white,null))

                        buttonPositive.isEnabled = true
                        buttonNegative.visibility = View.VISIBLE
                        buttonPositive.visibility = View.VISIBLE
                        buttonNeutral.visibility = View.GONE
                    }
                }
            }else{
                buttonNeutral.visibility = View.GONE
                buttonNegative.visibility = View.GONE
                buttonPositive.text = "Dibatalkan"
                buttonPositive.isEnabled = false
                buttonPositive.visibility = View.VISIBLE
                buttonPositive.setTextColor(ResourcesCompat.getColor(holder.itemView.resources,R.color.textLabel,null))
            }
            buttonNeutral.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
            buttonPositive.setOnClickListener {
                clickedItem.postValue(Event(item))
            }
            buttonNegative.setOnClickListener {
                clickedCanceledItem.postValue(Event(item))
            }
        }
    }

    fun addList(list: List<Recipe>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
