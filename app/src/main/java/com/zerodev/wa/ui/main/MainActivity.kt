package com.zerodev.wa.ui.main

import com.oratakashi.viewbinding.core.tools.*
import com.zero.zerobase.presentation.viewbinding.BaseActivity
import com.zerodev.wa.databinding.ActivityMainBinding
import com.zerodev.wa.ui.image.ImageActivity
import com.zerodev.wa.ui.video.VideoActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initUI() {
        super.initUI()
        binding.btnImage.onClick {
            startActivity(ImageActivity::class.java)
        }

        binding.btnVideo.onClick {
            toast("Coming Soon")
        }
    }

}

