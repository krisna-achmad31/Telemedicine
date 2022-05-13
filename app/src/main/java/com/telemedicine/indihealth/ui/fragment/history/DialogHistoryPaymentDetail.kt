package com.telemedicine.indihealth.ui.fragment.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.model.ProfileDoctor
import id.co.pradiptapaa.icare.binding.setImageBaseUrl
import kotlinx.android.synthetic.main.dialog_history_payment_detail.*

class DialogHistoryPaymentDetail(
    private val queue: ProfileDoctor,
    private val viewModel: HistoryPaymentViewModel
) : DialogFragment() {

    init {
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_history_payment_detail, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogView()
        setDialogListener()
    }

    private fun setDialogListener() {
        dialog_queue_detail_btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDialogView() {
        setImageBaseUrl(dialog_history_payment_iv_photo, queue.foto)
        dialog_queue_detail_tv_name.text = queue.getName
        dialog_queue_detail_tv_poly.text = queue.getName
        dialog_queue_detail_tv_date.text = queue.created_at
        dialog_queue_detail_tv_time.text = "${queue.username} | ${queue.str}"
        dialog_queue_detail_tv_queue.text = queue.getName
        dialog_queue_detail_tv_price.text = queue.getName
        dialog_queue_detail_tv_created_at.text = queue.getName
    }

    companion object {
        fun newInstance(
            queue: ProfileDoctor,
            viewModel: HistoryPaymentViewModel
        ): DialogHistoryPaymentDetail? {
            return DialogHistoryPaymentDetail(queue, viewModel)
        }
    }
}