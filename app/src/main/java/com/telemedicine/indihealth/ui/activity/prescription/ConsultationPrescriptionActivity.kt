package com.telemedicine.indihealth.ui.activity.prescription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseActivity
import com.telemedicine.indihealth.databinding.LayoutPrescriptionNewBinding
import com.telemedicine.indihealth.model.Medicine
import com.telemedicine.indihealth.model.Prescription
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_prescription.*
import kotlinx.android.synthetic.main.layout_prescription.prescription_tiet_medicine
import kotlinx.android.synthetic.main.layout_prescription.prescription_til_total
import kotlinx.android.synthetic.main.layout_prescription_new.*
import timber.log.Timber

@AndroidEntryPoint
class ConsultationPrescriptionActivity : BaseActivity() {
    private val binding: LayoutPrescriptionNewBinding by binding(R.layout.layout_prescription_new)
    private val viewModel: ConsultationPrescriptionNewViewModel by viewModels()

    private var scheduleDoctorSchedule: ScheduleDoctorConsultation? = null

    private val TAG="com.telemedicine.indihealth.ui.activity.prescription.ConsultationPrescriptionActivity"

    private val mAdapter: ConsultationPrescriptionAdapter by lazy {
        ConsultationPrescriptionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@ConsultationPrescriptionActivity
            vm = viewModel
            adapter = mAdapter
        }
        setSupportActionBar(binding.prescriptionToolbar)
        scheduleDoctorSchedule = intent.extras?.getParcelable("schedule")
        val temp = intent?.extras?.getParcelableArrayList<Prescription>("prescription")
        temp?.let {
            viewModel.prescriptionList = it
            mAdapter.addList(it)
        }

        setObservableValue()
        setOnClickListener()
    }

    private fun setObservableValue() {
        viewModel.schedule = scheduleDoctorSchedule
        viewModel.apply {
            prescription.observe(this@ConsultationPrescriptionActivity, { event ->
                event?.getContentIfNotHandled().apply {
                    this?.let {
                        mAdapter.add(it)
                    }
                }
            })
            responseStatus.observe(this@ConsultationPrescriptionActivity, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Tambah Resep Berhasil",
                                ""
                            )
                            dialog?.show(supportFragmentManager, "")
                            dialog?.onConfirmClicked = {
                                val intent = Intent()
                                val array = arrayListOf<Prescription>()
                                array.addAll(viewModel.prescriptionList)
                                intent.putParcelableArrayListExtra("prescription", array)
                                intent.putExtra("isPrescriptionSend", "send")
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Tambah Resep Gagal",
                                this.getValue("msg").toString()
                            )?.show(supportFragmentManager, "")
                        }
                    }
                }
            })
        }

    }

    private fun setOnClickListener() {
        prescription_tiet_medicine.setOnClickListener {
            val intent = Intent(this, MedicineActivity::class.java)
            startActivityForResult(intent, MedicineActivity.REQ_OBAT)
        }
        binding.prescriptionButtonAdd.setOnClickListener {
            var status = ""
            Timber.d("$TAG add button clicked")
            when {
                viewModel.medicine == null -> {
                    "Obat tidak boleh kosong".toast()
                    status = "obat kosong"
                }
                viewModel.total.value == "" -> {
                    "Jumlah Obat tidak boleh kosong".toast()
                    status = "Jumlah kosong"
                }
                viewModel.rule.value == "" -> {
                    "Aturan pakai tidak boleh kosong".toast()
                    status = "aturan kosong"
                }
                else -> {
                    status = "sukses"
                    viewModel.addMedicineToPrescription()
                    prescription_tiet_medicine.setText("")
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    var view: View? = currentFocus
                    if (view == null) {
                        view = View(this)
                    }
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    et_rule.clearFocus()
                }
            }
            Timber.d("$TAG $status")
        }
        binding.prescriptionToolbar.setNavigationOnClickListener {
            Timber.d("$TAG back button clicked")
            onBackPressed()
        }
        /* prescription_btn_send.setOnClickListener {
             val data = mAdapter.items
             if (data.size > 0) {
                 viewModel.sendPrescription(data)
             } else {
                 "Tidak ada resep obat".toast()
             }
         }*/
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
        Toast.makeText(this@ConsultationPrescriptionActivity, this, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clear()
    }

    override fun onBackPressed() {
        val intent = Intent()
        val array = arrayListOf<Prescription>()
        array.addAll(mAdapter.items)
        intent.putParcelableArrayListExtra("prescription", array)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.send_recipe_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sendRecipeMenu -> {
                Timber.d("$TAG option menu clicked")
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