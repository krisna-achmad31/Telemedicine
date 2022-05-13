package com.telemedicine.indihealth.ui.fragment.doctor.profile.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutDoctorProfileEditBinding
import com.telemedicine.indihealth.helper.AppSnackbar
import com.telemedicine.indihealth.helper.GlideApp
import com.telemedicine.indihealth.ui.fragment.doctor.profile.show.DoctorProfileShowFragmentDirections
import kotlinx.android.synthetic.main.layout_doctor_profile_edit.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DoctorProfileEditFragment : BaseFragment(), SupportedDatePickerDialog.OnDateSetListener {

    private lateinit var mBinding: LayoutDoctorProfileEditBinding
    private lateinit var navController: NavController

    companion object {
        fun startFragment(view: View) {
            view.findNavController()
                .navigate(R.id.action_doctorProfileShowFragment_to_doctorProfileEditFragment)
        }
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                DoctorProfileShowFragmentDirections
                    .actionDoctorProfileShowFragmentToDoctorProfileEditFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    private val viewModel: DoctorProfileEditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = LayoutDoctorProfileEditBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(mBinding.profileEditToolbar)
        return mBinding
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClickListener()
        setObservableValue()
        setDate(view)
        viewModel.getProfile()
    }

    private fun setOnClickListener() {
        mBinding.profileEditToolbar.setNavigationOnClickListener {
            viewModel.count = 0
            viewModel.countAddress = 0
            requireActivity().onBackPressed()
        }
        mBinding.cvImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
//                .compress(3000)
                .maxResultSize(620, 620)
                .start()
        }
        mBinding.buttonUpdateUsername.setOnClickListener{
            navController.navigate(R.id.action_doctorProfileEditFragment_to_updateUsernameFragment)
        }

        mBinding.buttonUpdatePassword.setOnClickListener {
            navController.navigate(R.id.action_doctorProfileEditFragment_to_updatePasswordFragment)
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })

            isEditIn.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    when (this) {
                        "success" -> {
                            requireActivity().onBackPressed()
                        }
                        else -> {
                            AppSnackbar.setError(this, requireView())
                        }
                    }
                }
            })

            genderList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it gender = $it")
                adapter.clear()
                it.forEachIndexed { index, item ->
                    adapter.add(item)
                }
                dd_gender.adapter = adapter
                dd_gender?.setSelection(viewModel.statGender)
                dd_gender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        viewModel.gender.postValue(parent!!.selectedItem.toString())
                    }
                }
            })

            provinsiList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it provinsi = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if(globalItem.id == address_province.value){
                        itemSelectedIndex = index
                    }
                }
                profile_patient_edit_spinner_provinsi.adapter = adapter
                profile_patient_edit_spinner_provinsi?.setSelection(itemSelectedIndex)
                profile_patient_edit_spinner_provinsi?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(position == 0){
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
                            viewModel.getKota(it[position].id)
                            param["alamat_provinsi"] = it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                        }
                    }
                }
            })
            kotaList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if(globalItem.id == address_city.value){
                        itemSelectedIndex = index
                    }
                }
                profile_patient_edit_spinner_kota.adapter = adapter
                profile_patient_edit_spinner_kota.setSelection(itemSelectedIndex)
                profile_patient_edit_spinner_kota?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(position == 0){
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
                            param["alamat_kota"] = it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                        }
                    }
                }
            })
            kecamatanList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if(globalItem.id == address_subdistrict.value){
                        itemSelectedIndex = index
                    }
                }
                profile_patient_edit_spinner_kecamatan.adapter = adapter
                profile_patient_edit_spinner_kecamatan.setSelection(itemSelectedIndex)
                profile_patient_edit_spinner_kecamatan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
                                param["alamat_kecamatan"] =
                                    it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                            }
                        }
                    }
                }
            })
            kelurahanList.observe(viewLifecycleOwner,{
                val adapter = ArrayAdapter<String>(requireActivity() , android.R.layout.simple_spinner_item , android.R.id.text1)
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                Timber.d("it = $it")
                adapter.clear()
                var itemSelectedIndex = 0
                it.forEachIndexed { index, globalItem ->
                    adapter.add(globalItem.name)
                    if(globalItem.id == address_subdistrict2.value){
                        itemSelectedIndex = index
                    }
                }
                profile_patient_edit_spinner_kelurahan.adapter = adapter
                profile_patient_edit_spinner_kelurahan.setSelection(itemSelectedIndex)
                profile_patient_edit_spinner_kelurahan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(position == 0){
                            viewModel.apply {
                                param.remove("alamat_kelurahan")
                                Timber.d("param = $param")
                            }
                        } else {
                            param["alamat_kelurahan"] = it[position].id?.toRequestBody("text/plain".toMediaTypeOrNull())
                        }
                    }
                }
            })

        }
    }

    private fun setDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mBinding.tvBirth.setOnClickListener {
            val dpd = SupportedDatePickerDialog(view.context, R.style.DatePickerDialogTheme,this, year, month, day)
            dpd.show()
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
        GlideApp
            .with(consultation_payment_detail_iv_photo)
            .load(uri)
            .placeholder(R.drawable.placeholder_rectangle)
            .into(mBinding.noImage)
        //consultation_payment_detail_iv_photo.visibility = View.VISIBLE
        //consultation_payment_detail_ll_pick_photo.visibility = View.GONE
//        consultation_payment_detail_btn_confirm.isEnabled = true
    }

    private fun setImageEmpty() {
        consultation_payment_detail_iv_photo.visibility = View.GONE
        //consultation_payment_detail_ll_pick_photo.visibility = View.VISIBLE
//        consultation_payment_detail_btn_confirm.isEnabled = false
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        tv_birth.setText(sdf.format(calendar.time))


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.saveMenu->{
                viewModel.attemptEditProfile()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu,menu)
    }

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
}