package com.telemedicine.indihealth.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.telemedicine.indihealth.databinding.DialogTocOwlexaBinding
import kotlinx.android.synthetic.main.dialog_app.*

class DialogTocOwlexa(
    private val title: String,
    private val description: String
) : DialogFragment(){

    var onConfirmClicked: (() -> Unit)? = null
    var onCancelClicked: (() -> Unit)? = null

    var confirmButtonText: String? = null
    var cancelButtonText: String? = null

    private lateinit var mBinding: DialogTocOwlexaBinding

    init {
        isCancelable = false
        setStyle(
            STYLE_NO_FRAME,
            android.R.style.Theme_Light_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBinding = DialogTocOwlexaBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogView()
        setDialogListener()
    }

    private fun setDialogListener() {
        mBinding.dialogBtnConfirm.setOnClickListener {
            dismiss()
            onConfirmClicked?.invoke()
        }
        mBinding.dialogBtnCancel.setOnClickListener {
            dismiss()
            onCancelClicked?.invoke()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDialogView() {
        mBinding.dialogTvTitle.text = title
        mBinding.dialogTvBody.text = description
        confirmButtonText?.let {
            dialog_btn_confirm.text = it
        }

        cancelButtonText?.let {
            dialog_btn_cancel.text = it
        }
    }

    companion object {
        fun newInstance(title: String, description: String): DialogTocOwlexa{
            return DialogTocOwlexa( title, description)
        }
    }
}