package com.telemedicine.indihealth.ui.activity.diagnosis

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseActivity
import com.telemedicine.indihealth.databinding.LayoutDiagnosisBinding
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_diagnosis.*
import timber.log.Timber

@AndroidEntryPoint
class DiagnosisActivity : BaseActivity() {
    private val binding: LayoutDiagnosisBinding by binding(R.layout.layout_diagnosis)
    private val viewModel: DiagnosisViewModel by viewModels()

    private val mAdapter: DiagnosisAdapter by lazy {
        DiagnosisAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@DiagnosisActivity
            adapter = mAdapter
            vm = viewModel
        }

        viewModel.schedule = intent.extras?.getParcelable("schedule")
        viewModel.diagnosis = intent.extras?.getString("diagnosis")

        setView()
        setObservableValue()
        setOnClickListener()
    }

    private fun setView() {
        if(viewModel.diagnosis.isNullOrEmpty()){
            hideSelectedDiagnosis()
        }else
        {
            showSelectedDiagnosis()
        }
    }

    private fun hideSelectedDiagnosis() {
        diagnosis_sv.visibility = VISIBLE
        diagnosis_rv.visibility = VISIBLE

        label_selected_diagnosis.visibility = GONE
        tv_selected_diagnosis.visibility = GONE
        btn_change_diagnosis.visibility = GONE
    }

    private fun showSelectedDiagnosis() {
        diagnosis_sv.visibility = GONE
        diagnosis_rv.visibility = GONE

        label_selected_diagnosis.visibility = VISIBLE
        tv_selected_diagnosis.visibility = VISIBLE
        btn_change_diagnosis.visibility = VISIBLE
    }

    private fun setOnClickListener(){
        diagnosis_toolbar.setOnClickListener {
            finish()
        }

        diagnosis_sv.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                viewModel.getDiagnosisList(query)
                return false
            }
        })

        diagnosis_sv.setIconifiedByDefault(false)
        diagnosis_sv.requestFocus()
        btn_change_diagnosis.setOnClickListener { hideSelectedDiagnosis() }
    }

    private fun setObservableValue(){
        mAdapter.apply {
            clickedItem.observe(this@DiagnosisActivity,{
                it?.getContentIfNotHandled().apply {
                    val dialog = DialogNotification.newInstance(
                        "confirm",
                        "Konfirmasi",
                        "Apakah anda akan memilih diagnosa ini?"
                    )
                    dialog?.confirmButtonText = "Ya"
                    dialog?.cancelButtonText = "Batal"
                    dialog?.isCancelButtonVisible = true
                    dialog?.isIconVisible = false
                    dialog?.show(supportFragmentManager, "")
                    dialog?.onConfirmClicked = {
                        this?.id?.let { it1 -> viewModel.postDiagnosis(it1, this.nama) }
                        dialog?.dismiss()
                    }
                    dialog?.onCancelClicked = {
                        dialog?.dismiss()
                    }
//                    val intent = Intent()
//                    intent.putExtra("obat", this)
//                    setResult(Activity.RESULT_OK, intent)
//                    finish()
                }
            })
        }

        viewModel.apply {
            responseStatus.observe(this@DiagnosisActivity, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Diagnosis Berhasil",
                                ""
                            )
                            dialog?.show(supportFragmentManager, "")
                            dialog?.onConfirmClicked = {
                                val intent = Intent()
                                intent.putExtra("diagnosis", viewModel.diagnosis)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Diagnosis Gagal",
                                this.getValue("msg").toString()
                            )?.show(supportFragmentManager, "")
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDiagnosisList()
    }
}