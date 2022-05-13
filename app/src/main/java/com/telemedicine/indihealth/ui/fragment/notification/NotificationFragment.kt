package com.telemedicine.indihealth.ui.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutNotificationBinding
import com.telemedicine.indihealth.model.Notification
import com.telemedicine.indihealth.ui.fragment.consultation.schedule.ConsultationScheduleFragment
import com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule.ConsultationScheduleDoctorFragment
import com.telemedicine.indihealth.ui.fragment.doctor.main.MainDoctorFragmentDirections
import com.telemedicine.indihealth.ui.fragment.history.HistoryPaymentFragment
import com.telemedicine.indihealth.ui.fragment.main.MainFragmentDirections
import com.telemedicine.indihealth.ui.fragment.recipe.RecipeDoctorFragment
import kotlinx.android.synthetic.main.layout_notification.*
import timber.log.Timber

class NotificationFragment : BaseFragment() {

    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutNotificationBinding

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_consultationAssessmentFragment)
        }

        fun startFragmentDirection(fragment: Fragment, list: List<Notification>?) {
            if (list != null) {
                val action =
                    MainFragmentDirections
                        .actionMainFragmentToNotificationFragment(list.toTypedArray())
                NavHostFragment.findNavController(fragment).navigate(action)
            } else {
                val action =
                    MainFragmentDirections
                        .actionMainFragmentToNotificationFragment(null)
                NavHostFragment.findNavController(fragment).navigate(action)
            }
//            list?.let {

//            }
        }

        fun startFragmentDirection2(fragment: Fragment, list: List<Notification>?) {
            list?.let {
                val action =
                    MainDoctorFragmentDirections
                        .actionMainDoctorFragmentToNotificationFragment(list.toTypedArray())
                NavHostFragment.findNavController(fragment).navigate(action)
            }
        }
    }

    //private val args: NotificationFragmentArgs? by navArgs()
    private val viewModel: NotificationViewModel by activityViewModels()
    private lateinit var mAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAdapter = NotificationAdapter()
        mBinding = LayoutNotificationBinding.inflate(inflater, container, false)
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
        setObservable()

        if (viewModel.user?.role!! == "dokter") {
            mBinding.notificationToolbar.navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.back_icon, null)
            mBinding.notificationToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        mAdapter.clickedItem.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled().apply {
                this?.let { notif ->
                    val splited = notif.direct_link.split("/")
                    if (viewModel.user?.role.equals("pasien", true)) {
                        when {
                            splited.last().contains("ResepDokter", true) -> {
                                //ke resep dokter
                                RecipeDoctorFragment.startFragmentDirection2(this@NotificationFragment)
                            }
                            splited.last().contains("Assesment", ignoreCase = true) -> {
                                val splitId = notif.direct_link.split("=")
                                val id = splitId.last()
                                val bundle = bundleOf("idJadwalKonsultasi" to id)

                                //ke assessment
                                navController.navigate(
                                    R.id.action_notificationFragment_to_consultationAssessmentFragment,
                                    bundle
                                )
                            }
                            splited.last().equals("history_obat", true) -> {
                                //to history obat
                                HistoryPaymentFragment.startFragmentDirection(this@NotificationFragment)
                            }
                            splited.last().equals("jadwal", true) -> {
                                //to jadwal konsultasi
                                ConsultationScheduleFragment.startFragmentDirection2(this@NotificationFragment)
                            }
                            else -> {
                                Timber.d("Notif Clicked")
                            }
                        }
                    } else if (viewModel.user?.role.equals("dokter", true)) {
                        when (splited.lastOrNull()) {
                            "Teleconsultasi" -> ConsultationScheduleDoctorFragment.startFragmentNotification(
                                this@NotificationFragment
                            )
                            "teleconsultasi" -> ConsultationScheduleDoctorFragment.startFragmentNotification(
                                this@NotificationFragment
                            )
                            else -> {
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setObservable() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.postNotificationReadAll()
        //viewModel.notificationList.value = args?.notification?.asList()
        viewModel.fetchNotificationList()
        Timber.d("notification list = ${viewModel.notificationList.value}")
    }

}