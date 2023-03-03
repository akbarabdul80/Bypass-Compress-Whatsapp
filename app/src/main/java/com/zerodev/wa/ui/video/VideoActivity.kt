package com.zerodev.wa.ui.video

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.oratakashi.viewbinding.core.tools.*
import com.zero.zerobase.presentation.viewbinding.BaseActivity
import com.zerodev.wa.R
import com.zerodev.wa.databinding.ActivityVideoBinding
import com.zerodev.wa.utils.*
import java.io.File

class VideoActivity : BaseActivity<ActivityVideoBinding>() {

    companion object {
        const val REQUEST_CODE = 100
    }

    private lateinit var lastVideo: File
    private lateinit var newVideo: File

    override fun initAction() {
        super.initAction()
        binding.videoNew.onClick {
            openGalleryForVideo()
        }

        binding.videoOld.onClick {
            if (this::lastVideo.isInitialized) {
                playVideoInDevicePlayer(lastVideo.path)
            } else {
                toast("Video terbaru tidak ditemukan, silahkan ikuti tutorial yang ada")
            }
        }

        binding.btnReplace.onClick {
            if (this::newVideo.isInitialized && this::lastVideo.isInitialized) {
                newVideo.copyTo(lastVideo, true)
                toast("Success bypass compress")
                loadOldVideo()
                resetState()
            }
        }
    }

    private fun resetState() {
        binding.videoNew.loadImage(
            this,
            resources.getDrawable(R.drawable.list_placeholder, null)
        )
        binding.tvPilihGambar.visible()
    }


    private fun loadOldVideo() {
        if (!baseContext.checkPermissionStorage()) {
            this.requestPermission()
        } else {
            loadVideoOld()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadVideoOld() {
        val folderVideo =
            File(Environment.getExternalStorageDirectory().path + Secured.getDirVideos())
        if (folderVideo.isDirectory) {
            val listFile = folderVideo.listFiles()
            val lastVideoOld = listFile?.sortedBy { it.name }?.last()

            lastVideoOld?.let {
                val bitmap = ThumbnailUtils.createVideoThumbnail(
                    it.path,
                    MediaStore.Video.Thumbnails.MICRO_KIND
                )
                binding.videoOld.setImageBitmap(bitmap)
                lastVideo = it
            }
        } else {
            toast("Video terbaru tidak ditemukan, silahkan ikuti tutorial yang ada")
        }
    }

    private fun openGalleryForVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.data != null) {
                val uriPathHelper = URIPathHelper()
                val videoFullPath = uriPathHelper.getPath(
                    this,
                    data.data!!
                ) // Use this video path according to your logic
                // if you want to play video just after picking it to check is it working
                videoFullPath?.let {
                    val bitmap = ThumbnailUtils.createVideoThumbnail(
                        it,
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                    binding.videoNew.setImageBitmap(bitmap)
                    newVideo = File(it)
                    binding.tvPilihGambar.gone()
                }
//                if (videoFullPath != null) {
//                    playVideoInDevicePlayer(videoFullPath);
//                }
            }
        }
    }

    fun playVideoInDevicePlayer(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadOldVideo()
    }
}