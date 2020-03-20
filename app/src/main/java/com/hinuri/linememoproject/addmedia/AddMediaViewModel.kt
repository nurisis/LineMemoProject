package com.hinuri.linememoproject.addmedia

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gun0912.tedpermission.PermissionListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddMediaViewModel(
    private val myApplication: Application
) :AndroidViewModel(myApplication) {

    private val _isPermissionAllowed = MutableLiveData<Boolean>()
    val isPermissionAllowed : LiveData<Boolean> = _isPermissionAllowed

    // 촬영한 이미지를 담는 이미지파일
    var imageFile: File? = null

    private val _isImageLinkValid = MutableLiveData<Boolean>()
    val isImageLinkValid : LiveData<Boolean> = _isImageLinkValid

    /**
     * 카메라 권한 리스너
     * */
    val permissionListener = object : PermissionListener {
        // 권한 허용 시
        override fun onPermissionGranted() {
            _isPermissionAllowed.value = true
        }
        // 권한 거부..
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            _isPermissionAllowed.value = false
        }
    }

    /**
     * Glide listener
     * 이를 통해 이미지 링크 url의 유효 여부를 체크.
     * */
    val glideListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            _isImageLinkValid.value = false
            return true
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            _isImageLinkValid.value = true
            return true
        }
    }
}