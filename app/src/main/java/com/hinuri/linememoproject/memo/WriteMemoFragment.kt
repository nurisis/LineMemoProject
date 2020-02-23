package com.hinuri.linememoproject.memo

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gun0912.tedpermission.TedPermission
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.addmedia.SelectImageTypeDialog
import com.hinuri.linememoproject.databinding.FragmentWriteMemoBinding
import com.hinuri.linememoproject.entity.MemoState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WriteMemoFragment : Fragment() {

    private val memoViewModel by sharedViewModel<MemoViewModel>()
    private lateinit var viewDataBinding : FragmentWriteMemoBinding
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentWriteMemoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@WriteMemoFragment
            viewModel = memoViewModel
        }

        imageListAdapter = ImageListAdapter(memoViewModel)
        viewDataBinding.rvImage.apply {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        viewDataBinding.btnAddImage.setOnClickListener { showImageTypeDialog() }

        viewDataBinding.btnSave.setOnClickListener {
            memoViewModel.saveMemo(viewDataBinding.etTitle.text.toString(), viewDataBinding.etContent.text.toString())
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        memoViewModel.memoImageList.observe(viewLifecycleOwner, Observer {
            Log.d("LOG>>", "이미지 리스트 : $it")
            imageListAdapter.submitList(it)
            imageListAdapter.notifyDataSetChanged()
        })
    }

    /**
     * 어떤 방식으로 이미지를 추가할 것인지 선택하는 다이얼로그 보여줌
     * */
    private fun showImageTypeDialog() {
        activity?.let {activity ->
            SelectImageTypeDialog.newInstance().apply {

                // 이미지 경로인 결과값 받음
                setImageTypeDialogResult(object : SelectImageTypeDialog.OnImageTypeDialogResult{
                    override fun addImageLink(url: String, isValid: Boolean) {
                        Log.d("LOG>>", "받아온링크 : $url, isValid: $isValid")

                        if(isValid)
                            memoViewModel.addImage(url)
                        else
                            Toast.makeText(activity,"링크가 유효하지 않습니다\uD83D\uDE2D",Toast.LENGTH_LONG).show()

                        dismiss()
                    }

                    override fun finish(imagePath: String?) {
                        Log.d("LOG>>", "받아온 이미지 :$imagePath")

                        imagePath?.let {
                            //이미지를 정상적으로 받아왔을 때 -> 리스트에 이미지 추가
                            memoViewModel.addImage(it)
                        }  ?: Toast.makeText(activity,"이미지를 업로드하는데 실패했습니다\uD83D\uDE2D",Toast.LENGTH_LONG).show()

                        dismiss()
                    }


                })

            }.show(activity.supportFragmentManager, "SelectImageTypeDialog")
        }
    }

    private val glideListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Toast.makeText(activity,"유효하지 않은 링크!",Toast.LENGTH_LONG).show()
            Log.e("LOG>>", "유효하지 않은 링크!")
            return true
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            Toast.makeText(activity,"유효한 링크!",Toast.LENGTH_LONG).show()
            Log.d("LOG>>","링크 성공! dataSource : $dataSource")
            return true
        }
    }

}
