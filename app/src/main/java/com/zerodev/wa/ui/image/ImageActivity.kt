package com.zerodev.wa.ui.image

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.*
import com.oratakashi.viewbinding.core.tools.*
import com.zero.zerobase.presentation.viewbinding.BaseActivity
import com.zerodev.wa.R
import com.zerodev.wa.databinding.ActivityImageBinding
import com.zerodev.wa.utils.Secured
import com.zerodev.wa.utils.checkPermissionStorage
import com.zerodev.wa.utils.loadImage
import com.zerodev.wa.utils.requestPermission
import pl.aprilapps.easyphotopicker.*
import java.io.File
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class ImageActivity : BaseActivity<ActivityImageBinding>() {

    private lateinit var lastImage: File
    private lateinit var newImage: File
    private lateinit var easyImage: EasyImage
    private var mInterstitialAd: InterstitialAd? = null

    override fun initAction() {
        super.initAction()
        initADS()

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(false)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("WA Bypass Compress")
            .allowMultiple(false)
            .build()

        binding.imgNew.onClick {
            easyImage.openChooser(this)
        }

        binding.btnReplace.onClick {
            if (this@ImageActivity::newImage.isInitialized && this@ImageActivity::lastImage.isInitialized) {
                newImage.copyTo(lastImage, true)
                toast("Success bypass compress")
                loadImageOld()
                resetState()
            }
        }

        binding.btnBack.onClick { finish() }
    }

    private fun initADS() {
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        InterstitialAd.load(
            this,
            "ca-app-pub-2187811703921023/9740774942",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("TAG", adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e("TAG", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    private fun showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.e("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    override fun initObserver() {
        super.initObserver()
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                if (this@ImageActivity::newImage.isInitialized && this@ImageActivity::lastImage.isInitialized) {
                    newImage.copyTo(lastImage, true)
                    toast("Success bypass compress")
                    loadImageOld()
                    resetState()
                }
                // Called when ad is dismissed.
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }
        }

    }

    private fun loadOldImage() {
        if (!baseContext.checkPermissionStorage()) {
            this.requestPermission()
        } else {
            loadImageOld()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadImageOld() {
        val folderImage =
            File(Environment.getExternalStorageDirectory().path + Secured.getDirImages())
        if (folderImage.isDirectory) {
            val listFile = folderImage.listFiles()
            val lastImageOld = listFile?.sortedBy { it.name }?.last()
            lastImageOld?.let {
                loadImage(binding.imgOld, it)
                lastImage = it
            }
        } else {
            toast("Image terbaru tidak ditemukan, silahkan ikuti tutorial yang ada")
        }
    }

    private fun resetState() {
        binding.imgNew.loadImage(
            this,
            resources.getDrawable(R.drawable.list_placeholder, null)
        )
        binding.tvPilihGambar.visible()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            easyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                this,
                object : DefaultCallback() {
                    override fun onMediaFilesPicked(
                        imageFiles: Array<MediaFile>,
                        source: MediaSource,
                    ) {
                        onPhotosReturned(imageFiles)
                    }

                    override fun onImagePickerError(
                        error: Throwable,
                        source: MediaSource,
                    ) {
                        //Some error handling
                        error.printStackTrace()
                    }

                    override fun onCanceled(source: MediaSource) {
                        //Not necessary to remove any files manually anymore
                    }
                })
        }
    }

    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        newImage = returnedPhotos[0].file
        loadImage(binding.imgNew, newImage)
        binding.tvPilihGambar.gone()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2296 -> if (grantResults.isNotEmpty()) {
                val READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadOldImage()
    }
}