package com.telemedicine.indihealth.ui.fragment.consultation.payment.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView.VISIBLE
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationPaymentDetailBinding
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.dialog.DialogTocOwlexa
import id.co.pradiptapaa.icare.binding.setImageUrlWithBaseUrlHistoryConsul
import kotlinx.android.synthetic.main.layout_consultation_payment_detail.*
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ConsultationPaymentDetailFragment : BaseFragment(), SupportedDatePickerDialog.OnDateSetListener {

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_consultationPaymentFragment_to_consultationPaymentDetailFragment)
        }

        /*fun startFragmentDoctor(view: View, payment: Payment) {
            val action =
                ConsultationPaymentFragmentDirections
                    .actionConsultationPaymentFragmentToConsultationPaymentDetailFragment(
                        payment
                    )
            view.findNavController().navigate(action)
        }*/
    }

    private val args: ConsultationPaymentDetailFragmentArgs? by navArgs()
    private val viewModel: ConsultationPaymentDetailViewModel by activityViewModels()
    private lateinit var mBinding: LayoutConsultationPaymentDetailBinding
    private lateinit var dialog: DialogTocOwlexa
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =LayoutConsultationPaymentDetailBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                payment = args?.payment
                vm = viewModel
                viewModel.payment = args?.payment
                paymentId = args?.paymentId
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        setDate(view)
        //kalau sudah lunas, button di bawah di hilangkan saja
        viewModel.payment?.let {
            if(it.id_status_pembayaran.equals("0")){
                mBinding.tvPaymentMethod.text = args?.paymentMethod
                if(args?.paymentMethod=="Transfer"){
                    setViewPaymentTransfer()
                }else{
                    mBinding.dotted.visibility = GONE
//                    setViewPaymentOwlexa()
                }
            }else{
                //lunas
                mBinding.consultationPaymentDetailLlPickPhoto.visibility = GONE
                mBinding.consultationPaymentDetailBtnConfirm. visibility = GONE
                mBinding.consultationPaymentDetailOwlexa.visibility = GONE
                mBinding.consultationPaymentDetailBtnSend.visibility = GONE
                mBinding.tvPaymentMethod.visibility = GONE
                mBinding.tvPaymentMethodLabel.visibility = GONE
                mBinding.dotted.visibility = GONE
//                textView36.visibility = GONE
//                spinner_payment.visibility = GONE
            }
        }
        //kalau
        viewModel.payment?.let {
            //mBinding.spinnerPayment.isEnabled = it.id_status_pembayaran.equals("0")
        }

    }

    private fun setOnClickListener() {
        mBinding.consultationPaymentDetailLlPickPhoto.setOnClickListener {
            ImagePicker.with(this)
//                .compress(3000) // 3mb
//                .maxResultSize(620, 620)
                .start()
        }
        mBinding.consultationPaymentDetailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        /*spinner_payment.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if(position == 0){
                    setViewPaymentTransfer()
                }else{
                    viewModel.getTocOwlexa()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }*/
    }

    private fun setViewPaymentTransfer() {
        mBinding.consultationPaymentDetailLlPickPhoto.visibility = VISIBLE
        mBinding.consultationPaymentDetailBtnConfirm. visibility = VISIBLE
        mBinding.consultationPaymentDetailOwlexa.visibility = GONE
        mBinding.consultationPaymentDetailBtnSend.visibility = GONE
    }

    private fun setViewPaymentOwlexa() {
        mBinding.consultationPaymentDetailLlPickPhoto.visibility = GONE
        mBinding.consultationPaymentDetailBtnConfirm. visibility = GONE
        mBinding.consultationPaymentDetailOwlexa.visibility = VISIBLE
        mBinding.consultationPaymentDetailBtnSend.visibility = VISIBLE
    }

    private fun setObservableValue() {
        args?.payment?.bukti_pembayaran?.let {
            //setImageUrl(it)
        }
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "SUCCESSTOC" -> {
                            val dialogToc = DialogTocOwlexa.newInstance(
                                "Syarat & Ketentuan", this.getValue(
                                    "msg"
                                ).toString()
                            )
                            dialogToc.show(childFragmentManager, "")
                            dialogToc.onConfirmClicked = {
                                setViewPaymentOwlexa()
                            }
                            dialogToc.onCancelClicked = {
                                //spinner_payment.setSelection(0)
                            }
                        }
                        "success" -> {
                            var title = "Pembayaran Konsultasi Berhasil"
                            var isNeedBack = true
                            if (this.containsKey("title")) {
                                title = this.getValue("title").toString()
                            }

                            if (this.containsKey("isNeedBack")) {
                                isNeedBack = this.getValue("isNeedBack") as Boolean
                            }
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                title,
                                this.getValue("msg").toString()
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.isIconVisible = true
                            dialog?.onConfirmClicked = {
                                if (isNeedBack) navController.navigate(R.id.action_consultationPaymentDetailFragment_to_consultationPaymentFragment)
                                else dialog?.dismiss()
                            }
                        }
                        else -> {
                            var title = "Pembayaran Konsultasi Gagal"
                            if (this.containsKey("title")) {
                                title = this.getValue("title").toString()
                            }
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                title,
                                this.getValue("msg").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })
            isFieldsFilledOtp.observe(viewLifecycleOwner, {
                if (it) {
                    tvOtp.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                } else {
                    tvOtp.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_400))
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                //mBinding.spinnerPayment.isEnabled = false

                //You can get File object from intent
                val file: File = ImagePicker.getFile(data)!!
                val fileSize = file.sizeInKb
                Timber.d("Size ini tuh %s", fileSize)
                if(fileSize > 3072){
                    Toast.makeText(requireContext(), "Maksimal file untuk diupload 3MB", Toast.LENGTH_SHORT).show()
                }else {
                    setImage(fileUri)
                    viewModel.setPhoto(file)
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
                setImageEmpty()
            }
            else -> {
                Toast.makeText(requireContext(), "Cari foto dibatalkan", Toast.LENGTH_SHORT).show()
                setImageEmpty()
            }
        }
    }

    private fun setImage(uri: Uri?) {
        consultation_payment_detail_iv_photo.setImageURI(uri)
        consultation_payment_detail_iv_photo.visibility = VISIBLE
        consultation_payment_detail_btn_confirm.isEnabled = true
        mBinding.consultationPaymentDetailLlPickPhoto.visibility = GONE
        mBinding.consultationPaymentDetailBtnConfirm.setBackgroundColor(resources.getColor(R.color.colorPrimary))

    }

    private fun setImageUrl(uri: String?) {
        setImageUrlWithBaseUrlHistoryConsul(consultation_payment_detail_iv_photo, uri)
        consultation_payment_detail_iv_photo.visibility = View.VISIBLE
        consultation_payment_detail_ll_pick_photo.visibility = GONE
        consultation_payment_detail_btn_confirm.visibility = GONE
        mBinding.consultationPaymentDetailLlPickPhoto.isClickable = false
    }

    private fun setImageEmpty() {
        consultation_payment_detail_iv_photo.visibility = GONE
        consultation_payment_detail_ll_pick_photo.visibility = View.VISIBLE
        consultation_payment_detail_btn_confirm.isEnabled = false
        mBinding.consultationPaymentDetailBtnConfirm.setBackgroundColor(resources.getColor(R.color.grey_400))
        //mBinding.spinnerPayment.isEnabled = true
    }

    private fun setDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        et_birthdate.setOnClickListener {
            val dpd = SupportedDatePickerDialog(
                view.context,
                R.style.DatePickerDialogTheme,
                this,
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        et_birthdate.setText(sdf.format(calendar.time))
    }

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
}