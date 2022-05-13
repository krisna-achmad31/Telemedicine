package com.telemedicine.indihealth.ui.fragment.consultation.registration

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.model.ConsultationDoctor
import id.co.pradiptapaa.icare.binding.setImageBaseUrl
import kotlinx.android.synthetic.main.dialog_consultation_registration_confirmation.*

class DialogRegistrationConfirmation(
    private val doctor: ConsultationDoctor,
    private val viewModel: ConsultationRegistrationViewModel
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
        return inflater.inflate(R.layout.dialog_consultation_registration_confirmation, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogView()
        setDialogListener()
    }

    private fun setDialogListener() {
        dialog_consultation_registration_confirmation_btn_cancel.setOnClickListener {
            dismiss()
        }
        dialog_consultation_registration_confirmation_btn_confirm.setOnClickListener {
            viewModel.postRegistrationDoctor(doctor)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDialogView() {
        setImageBaseUrl(dialog_consultation_registration_confirmation_iv_photo, doctor.foto)
        dialog_consultation_registration_confirmation_tv_name.text = doctor.getName
        dialog_consultation_registration_confirmation_tv_poly.text = doctor.poli
        dialog_consultation_registration_confirmation_tv_time.text =
            "${doctor.hari} | ${doctor.waktu}"
        dialog_consultation_registration_confirmation_tv_price.text = doctor.getPrice
    }

    companion object {
        fun newInstance(
            doctor: ConsultationDoctor,
            viewModel: ConsultationRegistrationViewModel
        ): DialogRegistrationConfirmation? {
            return DialogRegistrationConfirmation(doctor, viewModel)
        }
    }
}