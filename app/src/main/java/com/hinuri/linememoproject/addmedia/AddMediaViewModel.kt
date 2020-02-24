package com.hinuri.linememoproject.addmedia

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gun0912.tedpermission.PermissionListener

class AddMediaViewModel :ViewModel() {

    private val _isPermissionAllowed = MutableLiveData<Boolean>()
    val isPermissionAllowed : LiveData<Boolean> = _isPermissionAllowed

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