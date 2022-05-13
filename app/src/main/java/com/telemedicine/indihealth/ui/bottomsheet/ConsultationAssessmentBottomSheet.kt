package com.telemedicine.indihealth.ui.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.binding.checkIfRadioButtonIsEmpty
import com.telemedicine.indihealth.binding.checkIfTIETIsEmpty
import com.telemedicine.indihealth.databinding.LayoutConsultationAssessmentBinding
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail.AdditionImageAdapter
import com.telemedicine.indihealth.ui.fragment.consultation.assessment.detail.ConsultationAssessmentViewModel
import kotlinx.android.synthetic.main.layout_consultation_assessment.*
import pl.aprilapps.easyphotopicker.*
import timber.log.Timber


class ConsultationAssessmentBottomSheet(
    private val scheduleDoctorSchedule: ScheduleDoctorConsultation?
) : BottomSheetDialogFragment() {

    private val viewModel: ConsultationAssessmentViewModel by activityViewModels()
    var onNavigationClicked: (() -> Unit)? = null

    private lateinit var binding: LayoutConsultationAssessmentBinding
    private lateinit var imageAdapter: AdditionImageAdapter
    private lateinit var easyImage: EasyImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LayoutConsultationAssessmentBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
        return binding.root
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
        consultation_assessment_toolbar.setBackgroundColor(
            ResourcesCompat.getColor(view.context.resources,
                R.color.colorPrimary,null))

        setObservableValue()
        setOnClickListener()

        imageAdapter = AdditionImageAdapter(requireContext())
        binding.rvImages.adapter = imageAdapter
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initScheduleDoctor(scheduleDoctorSchedule?.id_pasien)
        Timber.d("ID pasien = ${scheduleDoctorSchedule?.id_pasien}")
    }

    private fun setOnClickListener(){
        consultation_assessment_toolbar.setNavigationOnClickListener {
            onNavigationClicked?.invoke()
        }
        consultation_assessment_btn_send.setOnClickListener {
            if(validate()) {
                viewModel.onAssessmentClicked2(scheduleDoctorSchedule?.id_pasien, scheduleDoctorSchedule?.id)
                viewModel.onOpenImage = false
            }
        }


        binding.btnSelectImage.setOnClickListener {
            easyImage = EasyImage.Builder(requireContext())
                .allowMultiple(true)
                .setChooserTitle("Pilih Gambar")
                .build()
            viewModel.onOpenImage = true
            easyImage.openChooser(this)
        }
    }


    private fun setObservableValue() {
        viewModel.apply {
            assessment.observe(viewLifecycleOwner, {
                Timber.d("smoke = ${it!!.berat_badan}")
                imageAdapter.submitList(viewModel.images)
            })
            scheduleList.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    viewModel.initAssessment(scheduleDoctorSchedule?.id_pasien, scheduleDoctorSchedule?.id)
                }
            })
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Assessment Berhasil",
                                ""
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.apply {
                                onConfirmClicked = {
                                    onNavigationClicked?.invoke()
                                }
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Assessment Gagal",
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

    private fun validate(): Boolean {
        if (checkIfTIETIsEmpty(
                consultation_assessment_til_keluhan,
                viewModel.assessment.value?.keluhan?.isEmpty()!!
            )
        ) {
            Timber.d("Assessment validate is false= ${viewModel.assessment.value}")
            return false
        } else {
            Timber.d("Assessment validate is true= ${viewModel.assessment.value}")
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                viewModel.images.clear()
                imageFiles.forEachIndexed { index, mediaFile ->
                    if (mediaFile.file.length() > 3 * 1024 * 1024) {
                        Toast.makeText(requireContext(), "Ukuran file ke-${index+1} diatas 3MB", Toast.LENGTH_SHORT).show()
                        return@forEachIndexed
                    }
                    if (index < 3) {
                        Timber.d("File Size: ${mediaFile.file.length() / 1024}KB -> ${mediaFile.file.name}")
                        viewModel.addImage(mediaFile.file.toUri())
                    }
                }
                imageAdapter.submitList(viewModel.images)
                if (imageFiles.size > 3) {
                    Toast.makeText(requireContext(), "Maksimal hanya 3 gambar", Toast.LENGTH_SHORT).show()
                }
                imageAdapter.notifyDataSetChanged()
            }

        })

    }
}