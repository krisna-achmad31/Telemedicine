package com.telemedicine.indihealth.ui.fragment.consultation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationPaymentBinding
import com.telemedicine.indihealth.model.ListPaymetnMethod
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationConfirmationFragmentDirections
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationFragment
import com.telemedicine.indihealth.ui.fragment.consultation.registration.ConsultationRegistrationFragmentDirections
import kotlinx.android.synthetic.main.layout_consultation_payment.*
import timber.log.Timber

class ConsultationPaymentFragment : BaseFragment() {

    private val viewModel: ConsultationPaymentViewModel by activityViewModels()
    private var mAdapter: ConsultationPaymentAdapter? = null
    private lateinit var mBinding: LayoutConsultationPaymentBinding
    private lateinit var navController: NavController

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_consultationPaymentFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                ConsultationRegistrationFragmentDirections
                    .actionConsultationRegistrationFragmentToConsultationPaymentFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }

        fun startFragmentDirection2(fragment: Fragment) {
            val action =
                ConsultationRegistrationConfirmationFragmentDirections
                    .actionConsultationRegistrationConfirmationFragmentToConsultationPaymentFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAdapter = ConsultationPaymentAdapter()
        mBinding = LayoutConsultationPaymentBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setObservableValue()
        setOnClickListener()
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                Timber.d("isLoading = ${it.peekContent()}")
                loadingValidation(it, requireContext())
            })
            consultationPaymentList.observe(viewLifecycleOwner, {
                this.setConsultationPaymentListExist(it.isEmpty())
            })
        }
        mAdapter?.apply {
            clickedItem.observe(viewLifecycleOwner, {
                Timber.d("it clickedItem: $it")
                it.getContentIfNotHandled()?.apply {
//                    ConsultationPaymentDetailFragment.startFragmentDoctor(requireView(), this)
                    if (this.id_status_pembayaran == "0") {
                        val bundle = bundleOf(
                            "transactionType" to "CONSULTATION",
                            "payment" to this
                        )
                        navController.navigate(
                            R.id.action_consultationPaymentFragment_to_paymentConfirmationFragment,
                            bundle
                        )
                    }else{
                        val bundle = bundleOf(
                            "payment" to this,
                            "paymentId" to ListPaymetnMethod()
                        )
                        navController.navigate(R.id.action_consultationPaymentFragment_to_consultationPaymentDetailFragment,bundle)
                    }
                }
            })
            clickedCanceledItem.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    setCancelDialog(this)
                }
            })
        }
    }

    private fun setCancelDialog(it: Payment) {
        val title: String?
        val description: String?
        when (it.id_status_pembayaran) {
            "0" -> {
                title = "Hapus Pendaftaran"
                description = "Apakah Anda yakin untuk menghapus pendaftaran ini?"
            }
            else -> {
                title = "Batalkan Pembayaran"
                description = "Apakah Anda yakin untuk membatalkan pembayaran ini?"
            }
        }
        val dialog = DialogNotification.newInstance("failed", title, description)
        dialog?.confirmButtonText = "Ya"
        dialog?.cancelButtonText = "Tidak"
        dialog?.isCancelButtonVisible = true
        dialog?.show(childFragmentManager, "")
        dialog?.apply {
            onConfirmClicked = {
                viewModel.parameters["id_registrasi"] = it.id
                when (it.id_status_pembayaran) {
                    "0" -> {
                        Timber.d("idStatusPembayaran is 0")
                        viewModel.deletePayment()
                    }
                    else -> {
                        Timber.d("idStatusPembayaran is else")
                        viewModel.updateCanceledOnProcessPayment()
                    }
                }
            }
            onCancelClicked = {
                dismiss()
            }
        }
    }

    private fun setOnClickListener() {
        consultation_payment_toolbar_payment.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        button.setOnClickListener {
            ConsultationRegistrationFragment.startFragmentDirection(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initConsultationPaymentList()
    }

    override fun onPause() {
        super.onPause()
        mAdapter = null
    }
}