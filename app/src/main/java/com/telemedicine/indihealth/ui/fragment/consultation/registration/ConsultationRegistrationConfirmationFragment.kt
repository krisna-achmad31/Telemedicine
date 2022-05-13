package com.telemedicine.indihealth.ui.fragment.consultation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationRegistrationConfirmationBinding
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.consultation.payment.ConsultationPaymentFragment
import timber.log.Timber

class ConsultationRegistrationConfirmationFragment : BaseFragment() {

    private lateinit var mBinding: LayoutConsultationRegistrationConfirmationBinding
    private val viewModel: ConsultationRegistrationConfirmationViewModel by activityViewModels()
    private val args: ConsultationRegistrationConfirmationFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.idJadwal = args.idJadwal
        viewModel.idDokter = args.idDokter
        Timber.d("ConsultationRegistrationConfirmationFragment ${args.idJadwal} ${args.idDokter}")
        Timber.d("isAccepted: ${args.isAccepted}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LayoutConsultationRegistrationConfirmationBinding.inflate(
            inflater,
            container,
            false
        )
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservable()
        viewModel.getDoctorProfile()
        if (args.isAccepted) {
            viewModel.postRegistrationDoctor()
        }
    }

    private fun setObservable() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner,{
                it.getContentIfNotHandled().apply {
                    loadingValidation(it, requireContext())
                }
            })

            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Konsultasi Berhasil",
                                this.getValue("msg").toString()
                            )
                            dialog?.isIconVisible = true
                            dialog?.show(childFragmentManager, "")
                            dialog?.apply {
                                onConfirmClicked = {
                                    ConsultationPaymentFragment.startFragmentDirection2(this@ConsultationRegistrationConfirmationFragment)
                                }
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Konsultasi Gagal",
                                this.getValue("msg").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })

            showTOC.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.apply {
                    if (it.peekContent()) {
                        findNavController().navigate(R.id.action_consultationRegistrationConfirmationFragment_to_tocFragment, Bundle().apply {
                            putInt("idDokter", viewModel.idDokter)
                            putInt("idJadwal", viewModel.idJadwal)
                        })
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {
        mBinding.profileShowToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}