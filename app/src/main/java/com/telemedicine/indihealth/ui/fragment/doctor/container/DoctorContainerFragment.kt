package com.telemedicine.indihealth.ui.fragment.doctor.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.telemedicine.indihealth.databinding.LayoutDoctorContainerBinding
import com.telemedicine.indihealth.ui.fragment.main.adapter.MenuLainnyaAdapter

class DoctorContainerFragment : Fragment() {
    private val viewModel: DoctorContainerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutDoctorContainerBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                adapterMenuLainnya = MenuLainnyaAdapter().apply {
                    clickedItem.observe(viewLifecycleOwner, {
                        it.getContentIfNotHandled().apply {
                            when (this?.id) {
                                "logout" -> {
//                                    LoginFragment.startFragmentDirection(this@MainFragment)
//                                    viewModel.clearSharedPreference()
                                }
                            }
                        }
                    })
                }
                vm = viewModel
            }
            .root
    }

}