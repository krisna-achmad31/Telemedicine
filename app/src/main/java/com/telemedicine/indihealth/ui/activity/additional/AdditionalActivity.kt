package com.telemedicine.indihealth.ui.activity.additional

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseActivity
import com.telemedicine.indihealth.databinding.LayoutAdditionalBinding
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Surya Mahadi on 11/13/2021
 */
@AndroidEntryPoint
class AdditionalActivity : BaseActivity() {
    private val binding: LayoutAdditionalBinding by binding(R.layout.layout_additional)
    private val viewModel: AdditionalVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.schedule = intent.extras?.getParcelable("schedule")
        viewModel.getAdditionalInfo()
        binding.apply {
            lifecycleOwner = this@AdditionalActivity
            vm = viewModel
            additionalButtonSave.setOnClickListener {
                viewModel.postAdditionalInfo()
            }
            additionalToolbar.setNavigationOnClickListener {
                finish()
            }
            cbLaboratory.setOnCheckedChangeListener { _, b ->
                viewModel.isLaboratory = b
            }
            cbRadiology.setOnCheckedChangeListener { _, b ->
                viewModel.isRadiology = b
            }
        }

        viewModel.apply {
            responseStatus.observe(this@AdditionalActivity) {
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Informasi tambahan Berhasil",
                                ""
                            )
                            dialog?.show(supportFragmentManager, "")
                            dialog?.onConfirmClicked = {
                                val intent = Intent()
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pengisian Informasi tambahan Gagal",
                                this.getValue("msg").toString()
                            )?.show(supportFragmentManager, "")
                        }
                    }
                }
            }
            additionalInfo.observe(this@AdditionalActivity) {
                binding.cbLaboratory.isChecked = it.laboratorium == "1"
                binding.cbRadiology.isChecked = it.radiologi == "1"
            }
        }
    }
}