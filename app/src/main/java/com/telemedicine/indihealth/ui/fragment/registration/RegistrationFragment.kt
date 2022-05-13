package com.telemedicine.indihealth.ui.fragment.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutRegistrationBinding
import com.telemedicine.indihealth.helper.AppSnackbar
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import com.telemedicine.indihealth.ui.fragment.login.LoginFragment
import com.telemedicine.indihealth.ui.fragment.login.LoginFragmentDirections
import kotlinx.android.synthetic.main.layout_registration.*
import kotlinx.android.synthetic.main.layout_schedule_doctor.*
import java.text.SimpleDateFormat
import java.util.*

class RegistrationFragment : BaseFragment(), SupportedDatePickerDialog.OnDateSetListener {

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return LayoutRegistrationBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableValue()
        setOnClickListener()
        setDate(view)
        setDropdown()
    }

    companion object {
        fun startFragmentDirection(fragment: Fragment) {
            val action =
                LoginFragmentDirections
                    .actionLoginFragmentToRegistrationFragment()
            NavHostFragment.findNavController(fragment).navigate(action)
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, {
                loadingValidation(it, requireContext())
            })
            toastMessage.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    AppSnackbar.setError(this, requireView())
                }
            })
            responseStatus.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("status").toString()) {
                        "success" -> {
                            val dialog = DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Berhasil",
                                this.getValue("message").toString()
                            )
                            dialog?.show(childFragmentManager, "")
                            dialog?.apply {
                                onConfirmClicked = {
                                    LoginFragment.startFragmentDirection2(this@RegistrationFragment)
                                }
                            }
                        }
                        else -> {
                            DialogNotification.newInstance(
                                this.getValue("status").toString(),
                                "Pendaftaran Gagal",
                                this.getValue("message").toString()
                            )?.show(childFragmentManager, "")
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListener() {
        textView3.setOnClickListener {
            LoginFragment.startFragmentDirection2(this@RegistrationFragment)
        }
    }

    private fun setDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        tv_birth.setOnClickListener {
            val dpd = SupportedDatePickerDialog(
                view.context,
                R.style.DatePickerDialogTheme,
                this,
                c
            )
            dpd.show()
        }
    }


    lateinit var genderAdapter: ArrayAdapter<String>

    private fun setDropdown() {
        genderAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            android.R.id.text1
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        genderAdapter.add("Laki-laki")
        genderAdapter.add("Perempuan")

        dd_gender.adapter = genderAdapter
        dd_gender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                pl = parent!!.selectedItem.toString()
                viewModel.gender.postValue(parent!!.selectedItem.toString())
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                pl = parent!!.selectedItem.toString()
                viewModel.gender.postValue(parent!!.selectedItem.toString())

            }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        tv_birth.setText(sdf.format(calendar.time))

    }
}