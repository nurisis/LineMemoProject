package com.hinuri.linememoproject.addmedia

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
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.hinuri.linememoproject.BuildConfig
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.common.Constant
import com.hinuri.linememoproject.databinding.DialogSelectImageTypeBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SelectImageTypeDialog : DialogFragment() {

    private lateinit var viewDataBinding:DialogSelectImageTypeBinding
    private val mediaViewModel by sharedViewModel<AddMediaViewModel>()

    var onImageTypeDialogResult: OnImageTypeDialogResult? = null

    private var imageType : String? = null
    private var cameraImagePath : String? = null
    private var imageLink : String? = null

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
            imageType = "camera"
            checkCameraPermission()
        }
        viewDataBinding.tvGallery.setOnClickListener {
            imageType = "gallery"
            checkCameraPermission()
        }

        viewDataBinding.tvAddLink.setOnClickListener {
            imageLink = viewDataBinding.etLink.text.toString()
            Glide.with(this)
                .load(imageLink)
                .listener(mediaViewModel.glideListener)
                .into(viewDataBinding.ivLink)
//                .submit()
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mediaViewModel.isPermissionAllowed.observe(viewLifecycleOwner, Observer {
            if(it) {
                when(imageType) {
                    "camera" -> callCameraApp()
                    "gallery" -> callGalleryApp()
                }
            }
        })

        mediaViewModel.isImageLinkValid.observe(viewLifecycleOwner, Observer {
            Log.d("LOG>>", "isImageLinkValid : $it, imageLink : $imageLink")

            imageLink?.run {
                onImageTypeDialogResult?.addImageLink(this, it)
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
                        "camera" -> setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        "gallery" -> setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                .check()
        }
    }

    private fun callCameraApp() {
        activity?.let {activity ->

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
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
                        Toast.makeText(activity, "카메라 앱을 실행하는데 에러가 생겼어요!", Toast.LENGTH_LONG).show()
                        Log.e("LOG>>","이미지 파일 생성 에러 :$ex")
                    }

                }
            }

        }
    }

    /**
     * 촬영한 사진을 담기 위한 파일을 만듬
     * 경로 : 앱의 개별 저장소/Pictures/APP_NAME
     */
    @Throws(IOException::class)
    private fun createImageFile() : Uri? {
        //This is the directory in which the file will be created. DCIM > APP_NAME
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            )
            , activity?.getString(R.string.app_name) ?: "Memo"
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

    private fun callGalleryApp() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
            },
            GALLERY_PHOTO_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            TAKE_PHOTO_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    onImageTypeDialogResult?.finish(imagePath = cameraImagePath)
                    notifyGallery(File(cameraImagePath))
                }
            }
            GALLERY_PHOTO_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    data?.let {
                        Log.i("LOG>>", "uri : ${it.data}")
                        context?.run {
                            onImageTypeDialogResult?.finish(imagePath = Constant.getAbsolutePath(this, it.data))
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
    fun setImageTypeDialogResult(onImageTypeDialogResult: OnImageTypeDialogResult) {
        this.onImageTypeDialogResult = onImageTypeDialogResult
    }

    interface OnImageTypeDialogResult {
        fun finish(imagePath:String?)
        fun addImageLink(url:String, isValid:Boolean)
    }

    companion object {
        fun newInstance() = SelectImageTypeDialog()
    }
}