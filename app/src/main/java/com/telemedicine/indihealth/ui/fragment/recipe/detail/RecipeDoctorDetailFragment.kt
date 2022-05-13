package com.telemedicine.indihealth.ui.fragment.recipe.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutRecipeDoctorDetailBinding
import com.telemedicine.indihealth.model.Recipe
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.recipe.RecipeDoctorFragmentDirections
import id.co.pradiptapaa.icare.binding.setImageUrlWithBaseUrlHistoryDrug
import kotlinx.android.synthetic.main.layout_recipe_doctor_detail.*
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecipeDoctorDetailFragment : BaseFragment(),SupportedDatePickerDialog.OnDateSetListener  {

    private lateinit var mBinding: LayoutRecipeDoctorDetailBinding
    private lateinit var navController: NavController

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_recipeDoctorFragment_to_recipeDoctorDetailFragment)
        }
        fun startFragmentDirection(view: View, recipe: Recipe) {
            val action =
                RecipeDoctorFragmentDirections
                    .actionRecipeDoctorFragmentToRecipeDoctorDetailFragment(recipe)
            view.findNavController().navigate(action)
        }
    }

    private val viewModel: RecipeDoctorDetailViewModel by activityViewModels()
    private val args: RecipeDoctorDetailFragmentArgs? by navArgs()
    private val mAdapter: RecipeDoctorDetailAdapter by lazy {
        RecipeDoctorDetailAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutRecipeDoctorDetailBinding.inflate(inflater, container, false)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                recipe = args?.recipe
                vm = viewModel
                adapter = mAdapter
                viewModel.recipe = args?.recipe
                viewModel.getRecipe()
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        setDate(view)
        if (args?.paymentMethod!=null){
            mBinding.tvPaymentMethodLabel.visibility = VISIBLE
            mBinding.tvPaymentMethod.visibility = VISIBLE
            mBinding.tvPaymentMethod.text = args?.paymentMethod
            if (args?.paymentMethod=="Transfer"){
                setViewPaymentTransfer()
            }else{
                setViewPaymentOwlexa()
            }
        }else {
            if(args?.recipe?.bukti_pembayaran != null && args?.recipe?.status_pembayaran_obat != null){
                if(args?.recipe?.status_pembayaran_obat != "1") setViewPaymentTransfer()
            }
        }
    }

    private fun setOnClickListener() {
        mBinding.profileDoctorDetailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        mBinding.recipePaymentDetailMcvPhoto.setOnClickListener {
            ImagePicker.with(this)
//                .compress(3000) // 3mb
//                .maxResultSize(620, 620)
                .start()
        }


//        recipe_spinner_payment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parentView: AdapterView<*>?,
//                selectedItemView: View,
//                position: Int,
//                id: Long
//            ) {
//                if (position == 0) {
//                    setViewPaymentTransfer()
//                } else {
//                    setViewPaymentOwlexa()
//                }
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // your code here
//            }
//        }
    }


    private fun setViewPaymentOwlexa() {
        mBinding.recipePaymentDetailMcvPhoto.visibility = GONE
        mBinding.recipePaymentDetailBtnConfirm.visibility = GONE
        mBinding.recipePaymentDetailOwlexa.visibility = AdapterView.VISIBLE
        mBinding.recipePaymentDetailBtnSend.visibility = AdapterView.VISIBLE


    }

    private fun setViewPaymentTransfer() {
        mBinding.recipePaymentDetailMcvPhoto.visibility = AdapterView.VISIBLE
        mBinding.recipePaymentDetailBtnConfirm.visibility = AdapterView.VISIBLE
        mBinding.recipePaymentDetailOwlexa.visibility = GONE
        mBinding.recipePaymentDetailBtnSend.visibility = GONE
    }

    private fun setObservableValue() {
        //kalau sudah lunas, button di bawah di hilangkan saja
        viewModel.recipe?.let {
            if(!it.status_pembayaran_obat.equals("3")){ //harus dipastikan status yang sudah bener itu berapa
                //lunas
                mBinding.recipePaymentDetailMcvPhoto.visibility = GONE
                mBinding.recipePaymentDetailBtnConfirm.visibility = GONE
                mBinding.recipePaymentDetailOwlexa.visibility = GONE
                mBinding.recipePaymentDetailBtnSend.visibility = GONE
//                textView_pay.visibility = View.GONE
//                recipe_spinner_payment.visibility = View.GONE
            }
        }
        if (args?.recipe?.getStatusPembayaran == "0") {
            setImageUrl(args?.recipe?.bukti_pembayaran)
        }
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            responseStatus.observe(viewLifecycleOwner, {
                Timber.d("responseStatus: ${it.peekContent()}")
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            var title = "Pembayaran Resep Berhasil"
                            var isNeedBack = true
                            if(this.containsKey("title")){
                                title = this.getValue("title").toString()
                            }

                            if(this.containsKey("isNeedBack")){
                                isNeedBack = this.getValue("isNeedBack") as Boolean
                            }
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                title,
                                this.getValue("msg").toString()
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.onConfirmClicked = {
                                if(isNeedBack) navController.navigate(R.id.action_recipeDoctorDetailFragment_to_recipeDoctorFragment)
                                else dialog?.dismiss()
                            }
                        }
                        else -> {
                            var title = "Pembayaran Resep Gagal"
                            if(this.containsKey("title")){
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data

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
        recipe_payment_detail_iv_photo.setImageURI(uri)
        recipe_payment_detail_iv_photo.visibility = VISIBLE
        recipe_payment_detail_ll_pick_photo.visibility = GONE
        recipe_payment_detail_btn_confirm.isEnabled = true

    }

    private fun setImageUrl(uri: String?) {
        setImageUrlWithBaseUrlHistoryDrug(recipe_payment_detail_iv_photo,uri)
        recipe_payment_detail_iv_photo.visibility = VISIBLE
        recipe_payment_detail_ll_pick_photo.visibility = GONE
        recipe_payment_detail_btn_confirm.visibility = GONE
        recipe_payment_detail_mcv_photo.isClickable = false
    }

    private fun setImageEmpty() {
        recipe_payment_detail_iv_photo.visibility = GONE
        recipe_payment_detail_ll_pick_photo.visibility = VISIBLE
        recipe_payment_detail_btn_confirm.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRecipe()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    private fun setDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        et_birthdate.setOnClickListener {
            val dpd = SupportedDatePickerDialog(view.context, R.style.DatePickerDialogTheme,this, year, month, day)
            dpd.show()
        }
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