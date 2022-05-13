package com.telemedicine.indihealth.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.telemedicine.indihealth.R
import kotlinx.android.synthetic.main.dialog_app.*

class DialogNotification(
    private val status: String,
    private val title: String,
    private val description: String
) : DialogFragment() {

    var onConfirmClicked: (() -> Unit)? = null
    var onCancelClicked: (() -> Unit)? = null

    var isCancelButtonVisible: Boolean? = false
    var isConfirmButtonVisible: Boolean? = true
    var isIconVisible: Boolean? = false
    var confirmButtonText: String? = null
    var cancelButtonText: String? = null

    init {
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_app, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogView()
        setDialogListener()
    }


    private fun setDialogListener() {
        dialog_btn_confirm.setOnClickListener {
            dismiss()
            onConfirmClicked?.invoke()
        }
        dialog_btn_cancel.setOnClickListener {
            onCancelClicked?.invoke()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDialogView() {
        setIcon(status)
        dialog_tv_title.text = title
        dialog_tv_body.text = description
        confirmButtonText?.let {
            dialog_btn_confirm.text = it
        }

        cancelButtonText?.let {
            dialog_btn_cancel.text = it
        }

        when (isIconVisible) {
            true -> {
                dialog_iv_icon.visibility = View.VISIBLE
                //image_background.visibility = GONE
            }
            false -> {
                dialog_iv_icon.visibility = View.GONE
                //image_background.visibility = VISIBLE
            }
        }
        when (isCancelButtonVisible) {
            true -> dialog_btn_cancel.visibility = View.VISIBLE
            false -> dialog_btn_cancel.visibility = View.GONE
        }
        when (isConfirmButtonVisible) {
            true -> dialog_btn_confirm.visibility = View.VISIBLE
            false -> dialog_btn_confirm.visibility = View.GONE
        }
    }

    private fun setIcon(string: String) {
        dialog_iv_icon_calling.visibility = View.GONE
        when (string) {
            "success" -> {
                //dialog_iv_icon.setImageResource(R.drawable.ic_success)
            }
            "failed" -> {
                //dialog_iv_icon.setImageResource(R.drawable.ic_failed)
            }
            "call" -> {
                dialog_iv_icon_calling.visibility = View.VISIBLE
//                dialog_iv_icon.setImageResource(R.drawable.new_calling_icon)
                /*dialog_iv_icon_calling.visibility = View.VISIBLE
                dialog_iv_icon.visibility = View.GONE*/
            }
        }
    }


    companion object {
        fun newInstance(status: String, title: String, description: String): DialogNotification? {
            return DialogNotification(status, title, description)
        }
    }
}