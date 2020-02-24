package com.hinuri.linememoproject.addmedia

/**
 * 메모 작성 시 이미지 추가 버튼을 클릭하면 나오는 다이얼로그.
 * 이미지를 추가할 수 있는 여러가지 옵션(카메라, 갤러리, 링크 추가 등)을 리스트로 주고, 각 옵션에 맞게 이미지를 얻어옴.
 * 얻어온 이미지 경로는 리턴해줌.
 * */

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.TedPermission
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.hinuri.linememoproject.BuildConfig
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.common.Constant
import com.hinuri.linememoproject.common.Constant.EXTRA_MEMO_IMAGE_FROM_CAMERA
import com.hinuri.linememoproject.common.Constant.EXTRA_MEMO_IMAGE_FROM_GALLERY
import com.hinuri.linememoproject.databinding.DialogSelectImageTypeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddMediaToMemoDialog : DialogFragment() {

    private lateinit var viewDataBinding:DialogSelectImageTypeBinding
    private val mediaViewModel by sharedViewModel<AddMediaViewModel>()

    private var onMediaDialogResult: OnMediaDialogResult? = null

    private var imageType : String? = null // 이미지를 어떻게 추가할 것인지 옵션. 카메라 또는 갤러리
    private var cameraImagePath : String? = null // 카메라로 촬영 후 담는 이미지 절대경로
    private var imageLink : String? = null // 링크 추가 시 링크 url

    private val TAKE_PHOTO_CODE = 111
    private val GALLERY_PHOTO_CODE = 222

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DialogSelectImageTypeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = activity
        }

        // 카메라 실행
        viewDataBinding.tvCamera.setOnClickListener {
            imageType = EXTRA_MEMO_IMAGE_FROM_CAMERA
            checkCameraPermission()
        }
        viewDataBinding.tvGallery.setOnClickListener {
            imageType = EXTRA_MEMO_IMAGE_FROM_GALLERY
            checkCameraPermission()
        }

        viewDataBinding.tvAddLink.setOnClickListener {
            imageLink = viewDataBinding.etLink.text.toString()

            // Glide의 requestListener를 통해 이미지 링크 url 유효 여부 체크
            Glide.with(this)
                .load(imageLink)
                .listener(mediaViewModel.glideListener)
                .into(viewDataBinding.ivLink)
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 권한 허용 옵저빙
        mediaViewModel.isPermissionAllowed.observe(viewLifecycleOwner, Observer {
            if(it) {
                when(imageType) {
                    EXTRA_MEMO_IMAGE_FROM_CAMERA -> callCameraApp()
                    EXTRA_MEMO_IMAGE_FROM_GALLERY -> callGalleryApp()
                }
            }
        })

        // 이미지 링크 유효 여부 옵저빙
        mediaViewModel.isImageLinkValid.observe(viewLifecycleOwner, Observer {
            imageLink?.run {
                onMediaDialogResult?.getImageLink(this, it)
                imageLink = null
            }
        })
    }

    /**
     * 카메라 촬영 및 저장 권한 요청
     * */
    private fun checkCameraPermission() {
        activity?.let {
            TedPermission.with(it)
                .setPermissionListener(mediaViewModel.permissionListener)
                .setDeniedMessage("거절하시면 이미지를 올리실 수 없어요 \uD83D\uDE2D\uD83D\uDE2D")
                .apply {
                    when(imageType){
                        EXTRA_MEMO_IMAGE_FROM_CAMERA -> setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        EXTRA_MEMO_IMAGE_FROM_GALLERY -> setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                .check()
        }
    }

    // 카메라 앱 호출
    private fun callCameraApp() {
        activity?.let {activity ->

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity.packageManager)?.also {
                    try {
                        // 1. 촬영할 사진을 담을 파일 생성
                        // 2. 위에서 생성한 이미지 파일이 존재할 경우에만 카메라앱 실행
                        createImageFile()?.also {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                            startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE)
                        }
                    } catch (ex: IOException) {
                        // 이미지 파일을 생성하는데 에러 남.
                        Toast.makeText(activity, "카메라 앱을 실행하는데 에러가 발생했습니다.", Toast.LENGTH_LONG).show()
                    }

                }
            }

        }
    }

    // 갤러리 앱 호출
    private fun callGalleryApp() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
            },
            GALLERY_PHOTO_CODE
        )
    }

    /**
     * 촬영한 사진을 담기 위한 파일을 만듬
     * 경로 : /DCIM/APP_NAME
     */
    @Throws(IOException::class)
    private fun createImageFile() : Uri? {
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            )
            , activity?.getString(R.string.app_name) ?: "LineMemo"
        )

        // 해당 폴더가 없으면 생성
        if(!storageDir.exists())
            storageDir.mkdir()

        val image = File.createTempFile(
            SimpleDateFormat("YYYY_MM_dd_HH:mm:ss").format(Date()), /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        cameraImagePath = image.absolutePath

        // 위에서 생성한 파일의 uri를 리턴
        return context?.let {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    it
                    , BuildConfig.APPLICATION_ID + ".provider"
                    , image)
            }
            else
                Uri.fromFile(image)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            // 카메라 촬영 후
            TAKE_PHOTO_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    onMediaDialogResult?.getImagePath(imagePath = cameraImagePath)
                    notifyGallery(File(cameraImagePath))
                }
            }
            // 갤러리에서 이미지 선택 후
            GALLERY_PHOTO_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    data?.let {
                        context?.run {
                            onMediaDialogResult?.getImagePath(imagePath = Constant.getAbsolutePath(this, it.data))
                        }
                    }
                }
            }
        }
    }

    /**
     * 앨범에 새로운 사진이 추가되었다고 알림.
     * */
    private fun notifyGallery(imageFile:File?) {
        imageFile?.let {
            activity?.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(it)
                )
            )
        }
    }

    /**
     * Dialog에서 이미지 관련 처리 후 결과 값 리턴 (다이얼로그를 호출한 곳으로)
     * */
    fun setMediaDialogResult(onMediaDialogResult: OnMediaDialogResult) {
        this.onMediaDialogResult = onMediaDialogResult
    }

    interface OnMediaDialogResult {
        fun getImagePath(imagePath:String?)
        fun getImageLink(url:String, isValid:Boolean)
    }

    companion object {
        fun newInstance() = AddMediaToMemoDialog()
    }
}