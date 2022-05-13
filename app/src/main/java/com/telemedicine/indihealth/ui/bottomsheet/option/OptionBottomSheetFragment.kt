package com.telemedicine.indihealth.ui.bottomsheet.option

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.telemedicine.indihealth.R
import kotlinx.android.synthetic.main.layout_option_bottom_sheet.*

class OptionBottomSheetFragment(
    private val options: List<String?>,
    private val selected: Int,
    private val title: String,
    private val canUnSelect: Boolean = true) : BottomSheetDialogFragment() {

    var onClicked: ((Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.layout_option_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter()
        tv_title.text = title
    }

    private inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_option_bottom_sheet,
            parent,
            false
        )
    ) {

        val text: TextView = itemView.findViewById(R.id.textView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    private inner class ItemAdapter :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = options[position]
            holder.checkBox.isChecked = (position == selected)

            holder.itemView.setOnClickListener {
                if(position == selected){
                    if(canUnSelect){
                        onClicked?.invoke(-1)
                    }else{
                        onClicked?.invoke(position)
                    }
                }else {
                    onClicked?.invoke(position)
                }
                dismiss()
            }
        }

        override fun getItemCount() = options.size
    }

    companion object {
        fun newInstance(options: List<String?>, selected: Int, title: String): OptionBottomSheetFragment {
            return OptionBottomSheetFragment(options, selected, title)
        }
        fun newInstance(options: List<String?>, selected: Int, title: String, canUnSelect: Boolean): OptionBottomSheetFragment {
            return OptionBottomSheetFragment(options, selected, title, canUnSelect)
        }
    }
}