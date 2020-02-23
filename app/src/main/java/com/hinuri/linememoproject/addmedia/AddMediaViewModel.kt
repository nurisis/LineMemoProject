package com.hinuri.linememoproject.addmedia

import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gun0912.tedpermission.PermissionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class AddMediaViewModel :ViewModel() {

    private val _isPermissionAllowed = MutableLiveData<Boolean>()
    val isPermissionAllowed : LiveData<Boolean> = _isPermissionAllowed

    private val _isImageLinkAdded = MutableLiveData<Boolean>()
    val isImageLinkAdded : LiveData<Boolean> = _isImageLinkAdded

    /**
     * 카메라 권한 리스너
     * */
    val permissionListener = object : PermissionListener {
        // 권한 허용 시
        override fun onPermissionGranted() {
            _isPermissionAllowed.value = true
//            takePicture()
        }
        // 권한 거부..
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            _isPermissionAllowed.value = false
        }
    }

    fun addImageLink(linkUrl:String) : Boolean {
        return isImageLinkValid(linkUrl).also {
            _isImageLinkAdded.value = it
        }
    }

    /**
     * 사용자가 입력한 이미지 링크의 유효 여부 체크
     */
    private fun isImageLinkValid(url:String) : Boolean{
        if(!url.endsWith(".jpeg", true) && !url.endsWith(".jpg", true) &&
            !url.endsWith(".png", true) && !url.endsWith(".gif", true) && !url.endsWith(".bmp", true))
            return false

        var result = URLUtil.isValidUrl(url)

//        runBlocking {
//            viewModelScope.launch(Dispatchers.IO) {
//                URLUtil.isValidUrl(url)
//
//                URL(url).openConnection().run {
//                    val responseCode = (this as HttpURLConnection).responseCode
//                    if(responseCode == 200) result = true
//                }
//            }
//        }

        return result
    }
}