package com.zerodev.wa.ui.main

import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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

        initADS()
    }

    private fun initADS() {
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

    }

}

