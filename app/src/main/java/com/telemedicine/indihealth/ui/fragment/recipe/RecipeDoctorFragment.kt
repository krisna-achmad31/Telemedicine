package com.telemedicine.indihealth.ui.fragment.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutRecipeDoctorBinding
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.main.MainFragmentDirections
import com.telemedicine.indihealth.ui.fragment.notification.NotificationFragmentDirections
import kotlinx.android.synthetic.main.layout_recipe_doctor.*
import timber.log.Timber

class RecipeDoctorFragment : BaseFragment() {

    private val viewModel: RecipeDoctorViewModel by activityViewModels()
    private lateinit var navController: NavController

    private val mAdapter: RecipeDoctorAdapter by lazy {
        RecipeDoctorAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutRecipeDoctorBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                adapter = mAdapter
            }
            .root
    }

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_mainFragment_to_recipeDoctorFragment)
        }

        fun startFragmentDirection(fragment: Fragment) {
            val action =
                MainFragmentDirections
                    .actionMainFragmentToRecipeDoctorFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }

        fun startFragmentDirection2(fragment: Fragment) {
            val action =
                NotificationFragmentDirections
                    .actionNotificationFragmentToRecipeDoctorFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController =  Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
    }

    override fun onResume() {
        super.onResume()
        initDoctorList()
    }

    private fun setOnClickListener() {
        consultation_registration_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext(), consultation_registration_rv_consultation_list)
            })
            profileDoctorList.observe(viewLifecycleOwner, {
                viewModel.setProfileDoctorListExist(it.isEmpty())
            })

            polyList.observe(viewLifecycleOwner, {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it poli = $it")
                adapter.clear()
                adapter.add("Semua")
                it.forEachIndexed { index, item ->
                    adapter.add(item)
                }
                spinner_poli.adapter = adapter
                spinner_poli?.setSelection(viewModel.positionPoly)
                spinner_poli?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (parent!!.selectedItem.toString() == "Semua") {
                            if(viewModel.poli != ""){
                                viewModel.poli = ""
                                viewModel.positionPoly = position
                                viewModel.getConsultationDoctor()
                            }
                        } else if(parent.selectedItem.toString() != viewModel.poli){
                            viewModel.poli = parent.selectedItem.toString()
                            viewModel.positionPoly = position
                            viewModel.getConsultationDoctor()
                        }
                    }
                }
            })
            statusPaymentList.observe(viewLifecycleOwner, {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it status = $it")
                adapter.clear()
                adapter.add("Semua")
                it.forEachIndexed { index, item ->
                    if (item == "0") {
                        adapter.add("Sedang Diproses")
                    } else if (item == "2") {
                        adapter.add("Ditolak")
                    } else if (item == "3") {
                        adapter.add("Belum Dibayar")
                    } else if (item == "1"){
                        adapter.add("Lunas")
                    }else if (item == "4"){
                        adapter.add("Dibatalkan")
                    }
                }
                spinner_status_payment.adapter = adapter
                spinner_status_payment?.setSelection(viewModel.positionStatusPayment)
                spinner_status_payment?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (parent!!.selectedItem.toString() != viewModel.choice) {
                                when {
                                    parent.selectedItem.toString() == "Sedang Diproses" -> {
                                        viewModel.statusPayment = "0"
                                    }
                                    parent.selectedItem.toString() == "Ditolak" -> {
                                        viewModel.statusPayment = "2"
                                    }
                                    parent.selectedItem.toString() == "Belum Dibayar" -> {
                                        viewModel.statusPayment = "3"
                                    }
                                    parent.selectedItem.toString() == "Dibatalkan" -> {
                                        viewModel.statusPayment = "4"
                                    }
                                    parent.selectedItem.toString() == "Lunas" -> {
                                        viewModel.statusPayment = "1"
                                    }
                                    else -> {
                                        viewModel.statusPayment = ""
                                    }
                                }
                                viewModel.getConsultationDoctor()
                                viewModel.positionStatusPayment = position
                                viewModel.choice = parent.selectedItem.toString()
                            }
                        }
                    }
            })
        }
        mAdapter.apply {
            clickedItem.observe(viewLifecycleOwner, {
                Timber.d("it clickedItem: $it")
                it.getContentIfNotHandled()?.apply {
//                    RecipeDoctorDetailFragment.startFragmentDirection(requireView(), this)
                    if (this.getStatusPembayaran == "3") {
                        val bundle = bundleOf(
                            "transactionType" to "RECIPE",
                            "recipe" to this
                        )
                        navController.navigate(R.id.action_recipeDoctorFragment_to_paymentConfirmationFragment,bundle)
                    }else{
                        val bundle = bundleOf(
                            "recipe" to this
                        )
                        navController.navigate(R.id.action_recipeDoctorFragment_to_recipeDoctorDetailFragment,bundle)
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

    private fun setCancelDialog(it: Recipe) {
        val title: String?
        val description: String?
        when (it.getStatusPembayaran) {
            "3" -> {
                title = "Hapus Pembayaran"
                description = "Apakah Anda yakin untuk menghapus pembayaran ini?"
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
                viewModel.parameters["id_jadwal_konsultasi"] = it.id_jadwal_konsultasi
                when (it.getStatusPembayaran) {
                    "3" -> {
                        Timber.d("idStatusPembayaran is 0")
                        viewModel.cancelRecipe()
                    }
                    else -> {
                        Timber.d("idStatusPembayaran is else")
                        viewModel.deletePayment()
                    }
                }
            }
            onCancelClicked = {
                dismiss()
            }
        }
    }

    private fun initDoctorList() {
        viewModel.initConsultationDoctor()
    }


}