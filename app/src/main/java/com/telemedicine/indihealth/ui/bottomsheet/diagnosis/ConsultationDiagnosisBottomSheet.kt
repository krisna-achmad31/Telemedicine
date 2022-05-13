package com.telemedicine.indihealth.ui.bottomsheet.diagnosis

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.telemedicine.indihealth.databinding.LayoutDiagnosisOldBinding
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import kotlinx.android.synthetic.main.layout_diagnosis_old.*
import timber.log.Timber


class ConsultationDiagnosisBottomSheet(
    private val scheduleDoctorSchedule: ScheduleDoctorConsultation?
) : BottomSheetDialogFragment() {

    private val viewModel: ConsultationDiagnosisViewModel by activityViewModels()
    var onNavigationClicked: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return LayoutDiagnosisOldBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            setBottomSheetBehavior(getBottomSheet(dialog))
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.schedule = scheduleDoctorSchedule
    }

    private fun setOnClickListener(){
        diagnosis_btn_confirm.setOnClickListener {
            if(viewModel.diagnosis.value != null){
                viewModel.postDiagnosis()
            } else {
                "Tidak ada diagnosis".toast()
            }
        }
        diagnosis_toolbar.setNavigationOnClickListener {
            onNavigationClicked?.invoke()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Diagnosis Berhasil",
                                this.getValue("msg").toString()
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.onConfirmClicked = {
                                dismiss()
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Diagnosis Gagal",
                                this.getValue("msg").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })
        }

    }

    private fun getBottomSheet(dialog: Dialog): View{
        val bottomSheet: FrameLayout =
            dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        return bottomSheet
    }

    private fun setBottomSheetBehavior(view: View){
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Timber.d("newState = $newState peekheight = ${behavior.peekHeight}")
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

   private fun String.toast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }
}