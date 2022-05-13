package com.telemedicine.indihealth.ui.fragment.consultation.payment.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutConsultationPaymentConfirmationBinding
import com.telemedicine.indihealth.helper.AppSnackbar
import kotlinx.android.synthetic.main.layout_consultation_payment_confirmation.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import kotlinx.android.synthetic.main.layout_consultation_payment_confirmation.*

class ConsultationPaymentConfirmation : BaseFragment() {
    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutConsultationPaymentConfirmationBinding
    private val args: ConsultationPaymentConfirmationArgs by navArgs()
    private val viewModel: ConsultationPaymentConfirmationViewModel by activityViewModels()

    companion object {
        fun newInstance() = ConsultationPaymentConfirmation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutConsultationPaymentConfirmationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.consultationPaymentConfirmationToolbar)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.transactionType == "RECIPE") {
            viewModel.recipe = args.recipe
        } else {
            viewModel.payment = args.payment
        }
        viewModel.listPaymetnMethod = args.paymentId
        navController = Navigation.findNavController(view)
        setObservableValue()
        setOnClickListener()
        viewModel.getProfile()
    }

    private fun setOnClickListener() {
        consultation_payment_detail_btn_send.setOnClickListener {
            if (!(viewModel.provinsi.value.isNullOrBlank() || viewModel.kabupaten.value.isNullOrBlank() ||
                        viewModel.kecamatan.value.isNullOrBlank() || viewModel.kelurahan.value.isNullOrBlank() ||
                        viewModel.kodepos.value.isNullOrBlank() || viewModel.alamat.value.isNullOrBlank())){
                if (args.paymentMethod == "Transfer") {
                    viewModel.setShipmentAddress()
                    if (args.transactionType == "RECIPE") {
                        val bundle = bundleOf(
                            "paymentMethod" to args.paymentMethod,
                            "recipe" to args.recipe,
                            "shipmentAddress" to viewModel.shipmentAddress,
                            "paymentId" to args.paymentId
                        )
                        AppSnackbar.setError("Metode pembayaran ini belum diimplementasikan", requireView())
//                        navController.navigate(
//                            R.id.action_consultationPaymentConfirmationFragment_to_recipeDoctorDetailFragment,
//                            bundle
//                        )
                    } else {
                        val bundle = bundleOf(
                            "paymentMethod" to args.paymentMethod,
                            "payment" to args.payment,
                            "shipmentAddress" to viewModel.shipmentAddress,
                            "paymentId" to args.paymentId
                        )
                        navController
                            .navigate(
                                R.id.action_consultationPaymentConfirmationFragment_to_consultationPaymentDetailFragment,
                                bundle
                            )
                    }
                } else {
                    AppSnackbar.setError("Metode pembayaran ini belum diimplementasikan", requireView())
                }
            } else {
                AppSnackbar.setError("Data tidak lengkap", requireView())
            }
        }
        consultation_payment_confirmation_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            viewModel.countAddress = 0
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner) {
                loadingValidation(it, requireContext())
            }

            provinsiList.observe(viewLifecycleOwner) {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it provinsi = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if (globalItem.id == address_province) {
                        itemSelectedIndex = index
                    }
                }
                mBinding.spinnerProvinsi.adapter = adapter
                mBinding.spinnerProvinsi?.setSelection(itemSelectedIndex)
                mBinding.spinnerProvinsi?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0) {
                                viewModel.apply {
                                    kotaList.value = listOf()
                                    kecamatanList.value = listOf()
                                    kelurahanList.value = listOf()
                                    param.remove("alamat_provinsi")
                                    param.remove("alamat_kota")
                                    param.remove("alamat_kecamatan")
                                    param.remove("alamat_kelurahan")
                                    Timber.d("param = $param")
                                }
                            } else {
                                viewModel.kabupaten.value = ""
                                viewModel.kecamatan.value = ""
                                viewModel.kelurahan.value = ""
                                viewModel.getKota(it[position].id)
                                viewModel.provinsi.postValue((it[position].id))
                                viewModel.afterSpinnerChanged()
                                param["alamat_provinsi"] =
                                    it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                            }
                        }
                    }
            }
            kotaList.observe(viewLifecycleOwner) {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if (globalItem.id == address_city) {
                        itemSelectedIndex = index
                    }
                }
                mBinding.spinnerKabupaten.adapter = adapter
                mBinding.spinnerKabupaten.setSelection(itemSelectedIndex)
                mBinding.spinnerKabupaten?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0) {
                                viewModel.apply {
                                    kecamatanList.value = listOf()
                                    kelurahanList.value = listOf()
                                    param.remove("alamat_kota")
                                    param.remove("alamat_kecamatan")
                                    param.remove("alamat_kelurahan")
                                    Timber.d("param = $param")
                                }
                            } else {
                                viewModel.getKecamatan(it[position].id)
                                viewModel.kabupaten.postValue((it[position].id))
                                viewModel.kecamatan.value = ""
                                viewModel.kelurahan.value = ""
                                viewModel.afterSpinnerChanged()
                                param["alamat_kota"] =
                                    it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                            }
                        }
                    }
            }
            kecamatanList.observe(viewLifecycleOwner) {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if (globalItem.id == address_subdistrict) {
                        itemSelectedIndex = index
                    }
                }
                mBinding.spinnerKecamatan.adapter = adapter
                mBinding.spinnerKecamatan.setSelection(itemSelectedIndex)
                mBinding.spinnerKecamatan?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (!it.isEmpty()) {
                                viewModel.getKelurahan(it[position].id)
                                if (position == 0) {
                                    viewModel.apply {
                                        kelurahanList.value = listOf()
                                        param.remove("alamat_kecamatan")
                                        param.remove("alamat_kelurahan")
                                        Timber.d("param = $param")
                                    }
                                } else {
                                    viewModel.getKelurahan(it[position].id)
                                    viewModel.kecamatan.postValue((it[position].id))
                                    viewModel.kelurahan.value = ""
                                    viewModel.afterSpinnerChanged()
                                    param["alamat_kecamatan"] =
                                        it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                                }
                            }
                        }
                    }
            }
            kelurahanList.observe(viewLifecycleOwner) {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if (globalItem.id == address_subdistrict2) {
                        itemSelectedIndex = index
                    }
                }
                mBinding.spinnerKelurahan.adapter = adapter
                mBinding.spinnerKelurahan.setSelection(itemSelectedIndex)
                mBinding.spinnerKelurahan?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0) {
                                viewModel.apply {
                                    param.remove("alamat_kelurahan")
                                    Timber.d("param = $param")
                                }
                            } else {
                                viewModel.kelurahan.postValue((it[position].id))
                                viewModel.afterSpinnerChanged()
                                param["alamat_kelurahan"] =
                                    it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                            }
                            viewModel.afterSpinnerChanged()
                        }
                    }
            }

        }
    }

}