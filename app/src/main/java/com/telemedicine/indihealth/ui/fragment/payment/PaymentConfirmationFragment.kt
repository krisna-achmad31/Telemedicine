package com.telemedicine.indihealth.ui.fragment.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutPaymentConfirmationBinding
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.helper.GlideApp
import com.telemedicine.indihealth.model.ListPaymetnMethod
import com.telemedicine.indihealth.model.Payment
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.ui.dialog.DialogTocOwlexa
import kotlinx.android.synthetic.main.item_payment_method.*
import kotlinx.android.synthetic.main.layout_payment_confirmation.*
import timber.log.Timber

class PaymentConfirmationFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    companion object {
        fun newInstance() = PaymentConfirmationFragment()
    }

    private val viewModel: PaymentConfirmationViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutPaymentConfirmationBinding
    private val args: PaymentConfirmationFragmentArgs by navArgs()
    private lateinit var payment: Payment
    private lateinit var recipe: Recipe
    private var paymentMethod: String = ""
    private val TAG = "com.telemedicine.indihealth.ui.fragment.payment.PaymentConfirmationFragment"
    private var data = ArrayList<String>()
    private var mAdapter : PaymentConfirmationAdapter? = null
    private var listPaymetnMethod : ListPaymetnMethod? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAdapter = PaymentConfirmationAdapter()
        mBinding = LayoutPaymentConfirmationBinding.inflate(inflater, container, false)
        return mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            adapter = mAdapter
        }.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("$TAG: ${args.transactionType}")
        if (args.transactionType == "RECIPE") {
            recipe = args.recipe!!
            Timber.d("$TAG: ${args.recipe}")
            setViewForRecipe()
        } else {
            payment = args.payment!!
            Timber.d("$TAG: ${args.payment}")
            setViewForConsultation()
        }
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservable()
        //default payment Method
        //mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnTransfer.id)
        mBinding.button.isEnabled = false

            data.clear()
            data.add("Pilih Metode Pembayaran")
            data.add("Bank Transfer")
//            data.add("Owlexa")
//            data.add("Virtual Account")
//            data.add("Dompet Digital")

        mBinding.spinnerPaymentMethod.adapter = ArrayAdapter<String>(requireContext(), R.layout.item_payment_method, R.id.tv_item_payment,data)
        mBinding.spinnerPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(view != null) {
                    var selectedItem: String = data[position]
                    if (!selectedItem.equals("Pilih Metode Pembayaran")) {
                        if (selectedItem.equals("Bank Transfer")) {
                            mBinding.containerSelectedMethod.visibility = GONE
//                            tv_note_payment_transfer.visibility = VISIBLE
                            paymentMethod = "Transfer"
                            mBinding.button.isEnabled = false
                            viewModel.getVaList()
                            mBinding.containerSelectedMethod.visibility = VISIBLE
                        } else if (selectedItem.equals("Owlexa")) {
                            mBinding.containerSelectedMethod.visibility = GONE
                            tv_note_payment_transfer.visibility = GONE
                            paymentMethod = "Owlexa"
                            if (args.transactionType != "RECIPE") {
                                viewModel.getTocOwlexa()
                            }
                            mBinding.button.isEnabled = true
                        } else {
                            tv_note_payment_transfer.visibility = GONE
                            if (selectedItem.equals("Virtual Account")){
                                viewModel.getVaList()
                                mBinding.containerSelectedMethod.visibility = VISIBLE
                            }
                        }
                    } else {
                        mBinding.containerSelectedMethod.visibility = GONE
                        mBinding.button.isEnabled = false
                        tv_note_payment_transfer.visibility = GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setObservable() {
        mAdapter?.apply {
            clickedItem.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled().apply{
                    mBinding.button.isEnabled = true
                    listPaymetnMethod = this
                }
            })
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
                                "Syarat & Ketentuan",
                                this.getValue("msg").toString()
                            )
                            dialogToc.show(childFragmentManager, "")
                            dialogToc.onConfirmClicked = {
                                paymentMethod = "Owlexa"
                                viewModel.owlexaApproved = true
                            }
                            dialogToc.onCancelClicked = {
                                mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnTransfer.id)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListener() {
        mBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        mBinding.containerRdBtnTransfer.setOnClickListener {
            mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnTransfer.id)
        }
        mBinding.containerRdBtnOwlexa.setOnClickListener {
            mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnOwlexa.id)
        }
        mBinding.rdBtnTransfer.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnTransfer.id)
            }
        }
        mBinding.rdBtnOwlexa.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mBinding.rdGroupPaymentMethod.check(mBinding.rdBtnOwlexa.id)
            }
        }
        mBinding.rdGroupPaymentMethod.setOnCheckedChangeListener(this)
        mBinding.button.setOnClickListener {
            if (args.transactionType=="RECIPE"){
                val bundle = bundleOf("paymentMethod" to paymentMethod, "recipe" to recipe)
                navController.navigate(R.id.action_paymentConfirmationFragment_to_recipeDoctorDetailFragment,bundle)
            }else{
                val bundle = bundleOf(
                    "paymentMethod" to paymentMethod,
                    "payment" to payment,
                    "transactionType" to args.transactionType,
                    "paymentId" to listPaymetnMethod,
                )
                navController.navigate(R.id.action_paymentConfirmationFragment_to_consultationPaymentConfirmationFragment, bundle)
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            mBinding.rdBtnTransfer.id -> {
//                tv_note_payment_transfer.visibility = VISIBLE
//                paymentMethod = "Transfer"
            }
            mBinding.rdBtnOwlexa.id -> {
//                tv_note_payment_transfer.visibility = GONE
//                paymentMethod = "Owlexa"
//                if (args.transactionType!="RECIPE") {
//                    viewModel.getTocOwlexa()
//                }
            }
        }
    }

    private fun setDoctorImage(url: String?) {
        if (!url.isNullOrBlank()) {
            Timber.d("the url is = ${AppVar.BASE_FILE_URL + url}")
            GlideApp
                .with(mBinding.ivDoctorPhoto)
                .load(AppVar.BASE_FILE_URL + url)
                .override(80, 80)
                .placeholder(R.drawable.placeholder_square)
                .into(mBinding.ivDoctorPhoto)
        } else {
            GlideApp
                .with(mBinding.ivDoctorPhoto)
                .load(R.drawable.placeholder_square)
                .into(mBinding.ivDoctorPhoto)
        }
    }

    private fun setViewForConsultation() {
        setDoctorImage(payment.foto)
        mBinding.tvDoctorName.text = payment.nama_dokter
        mBinding.tvDoctorPoly.text = payment.poli
        mBinding.tvDoctorStr.text = payment.getDoctorSTR
        mBinding.tvPrice1.text = payment.getBiayaKonsultasi
        mBinding.tvPrice2.text = payment.getBiayaAdministrasi
        mBinding.tvTotalPrice.text = payment.getTotalPembayaran
    }

    @SuppressLint("SetTextI18n")
    private fun setViewForRecipe() {
        setDoctorImage(recipe.foto_dokter)
        mBinding.tvDoctorName.text = recipe.nama_dokter
        mBinding.tvDoctorPoly.text = recipe.poli
        mBinding.tvDoctorStr.text = recipe.getDoctorSTR
        mBinding.tvPrice1Label.text = "Harga Obat"
        mBinding.tvPrice1.text = recipe.getHargaObat
        mBinding.tvPrice2Label.text = "Biaya Pengiriman"
        mBinding.tvPrice2.text = recipe.getBiayaPengiriman
        mBinding.tvTotalPrice.text = recipe.getTotalPrice
    }

}