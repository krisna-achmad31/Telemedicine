package com.telemedicine.indihealth.ui.activity.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseActivity
import com.telemedicine.indihealth.databinding.LayoutImageBinding
import com.telemedicine.indihealth.ui.activity.HomeActivity
import com.telemedicine.indihealth.ui.activity.transition.TransitionViewModel

class ImageActivity : BaseActivity(){

    companion object {
        const val LINK_IMAGE = "LINK_IMAGE"
        fun startActivity(context: Context, linkImage: String?) {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(LINK_IMAGE, linkImage)
            context.startActivity(intent)
        }
    }
    private val binding: LayoutImageBinding by binding(R.layout.layout_image)
    fun Intent(context: TransitionViewModel, java: Class<HomeActivity>): Intent? {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@ImageActivity
            link  = intent.getStringExtra(LINK_IMAGE)
            imageToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }
}