package com.telemedicine.indihealth.ui.bottomsheet.prescription

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.databinding.LayoutPrescriptionBinding
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.activity.prescription.MedicineActivity
import com.telemedicine.indihealth.ui.bottomsheet.chat.ConsultationChatAdapter
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import kotlinx.android.synthetic.main.layout_prescription.*
import timber.log.Timber


class ConsultationPrescriptionBottomSheet(
    private val scheduleDoctorSchedule: ScheduleDoctorConsultation?
) : BottomSheetDialogFragment() {

    private val viewModel: ConsultationPrescriptionViewModel by activityViewModels()
    private lateinit var adapter: ConsultationChatAdapter

    var onNavigationClicked: (() -> Unit)? = null

    private val mAdapter: ConsultationPrescriptionAdapter by lazy {
        ConsultationPrescriptionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).setSupportActionBar(prescription_toolbar)
        return LayoutPrescriptionBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFirebaseDatabase()
        setObservableValue()
        setOnClickListener()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clear()
    }


    private fun setObservableValue() {
        viewModel.schedule = scheduleDoctorSchedule
        viewModel.apply {
            prescription.observe(viewLifecycleOwner, { event ->
                event?.getContentIfNotHandled().apply {
                    this?.let {
                        mAdapter.add(it)
                    }
                }
            })
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Tambah Resep Berhasil",
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
                                "Tambah Resep Gagal",
                                this.getValue("msg").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })
        }

    }

    private fun setOnClickListener() {
        prescription_tiet_medicine.setOnClickListener {
            val intent = Intent(activity, MedicineActivity::class.java)
            startActivityForResult(intent, MedicineActivity.REQ_OBAT)
        }
        prescription_button_add.setOnClickListener {
            if (viewModel.medicine == null) {
                "Obat tidak boleh kosong".toast()
            } else if (viewModel.total.value == "") {
                "Jumlah Obat tidak boleh kosong".toast()
            } else if (viewModel.rule.value == "") {
                "Aturan pakai tidak boleh kosong".toast()
            } else {
                viewModel.addMedicineToPrescription()
                prescription_tiet_medicine.setText("")
            }
        }
        prescription_toolbar.setNavigationOnClickListener {
            onNavigationClicked?.invoke()
        }
        /*prescription_btn_send.setOnClickListener {
            val data = mAdapter.items
            if (data.size > 0) {
                viewModel.sendPrescription(data)
            } else {
                "Tidak ada resep obat".toast()
            }
        }*/
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            setBottomSheetBehavior(getBottomSheet(dialog))
        }
        return dialog
    }

    private fun getBottomSheet(dialog: Dialog): View {
        val bottomSheet: FrameLayout =
            dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        return bottomSheet
    }

    private fun setBottomSheetBehavior(view: View) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Timber.d("requestCode = $requestCode")
                if (requestCode == MedicineActivity.REQ_OBAT) {
                    val obatItem = data?.getParcelableExtra<Medicine>("obat")
                    Timber.d("obatItem = $obatItem")
                    prescription_tiet_medicine.setText(obatItem?.name)
                    prescription_til_total.hint = "Jumlah (${obatItem?.unit})"
                    viewModel.medicine = obatItem
                }
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.send_recipe_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sendRecipeMenu -> {
                val data = mAdapter.items
                if (data.size > 0) {
                    viewModel.sendPrescription(data)
                } else {
                    "Tidak ada resep obat".toast()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}