package com.telemedicine.indihealth.ui.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragment
import com.telemedicine.indihealth.databinding.LayoutOnboardingBinding
import com.telemedicine.indihealth.model.OnBoardingModel

class OnBoardingFragment : BaseFragment() {

    private lateinit var navController: NavController
    private lateinit var mBinding: LayoutOnboardingBinding
    private val adapter = OnBoardingAdapter(
        listOf(
            OnBoardingModel(
                "Konsultasi Online",
                "Dapat berkonsultasi secara online dengan dokter yang bernaung langsung pada rumah sakit.",
                R.drawable.onboarding1
            ),
            OnBoardingModel(
                "Pengantaran Obat",
                "Resep obat dapat dipesan dan diantarkan ke rumah pasien.",
                R.drawable.onboarding2
            ),
            OnBoardingModel(
                "Catatan Medis",
                "Menyimpan catatan konsultasi dan resep obat secara aman.",
                R.drawable.onboarding3
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = LayoutOnboardingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        //set the adapter to the viewpager2
        mBinding.viewPager.adapter = adapter
//sets the viewpager2 to the indicator
        mBinding.indicator.setViewPager(mBinding.viewPager)

        mBinding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

/*
*check if its the last page, change text on the button
*from next to finish and set the click listener to
*to navigate to welcome screen else set the text to next
* and click listener to move to next page
*/
                    if (position == adapter.itemCount - 1) {
//this animation is added to the finish button
                        val animation = AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.on_boarding_anim
                        )
                        mBinding.buttonNext.animation = animation
                        mBinding.buttonNext.text = getString(R.string.mulai)
                        mBinding.buttonNext.setOnClickListener {
                            navController.navigate(R.id.action_onBoardingFragment_to_getStartedFragment)
                        }
                    } else {
                        mBinding.buttonNext.text = getString(R.string.lanjutkan)
                        mBinding.buttonNext.setOnClickListener {
                            mBinding.viewPager.currentItem.let {
                                mBinding.viewPager.setCurrentItem(it + 1, false)
                            }
                        }
                    }
                }
            })
    }
}