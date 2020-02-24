package com.hinuri.linememoproject.memo

/**
 * 메모 작성 및 편집 화면
 * */

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hinuri.linememoproject.R
import com.hinuri.linememoproject.addmedia.AddMediaToMemoDialog
import com.hinuri.linememoproject.databinding.FragmentWriteMemoBinding
import kotlinx.android.synthetic.main.layout_memo_toolbar.*
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
            toolbar.viewModel = memoViewModel
        }

        // 메모 내 이미지 리스트 관련 설정
        imageListAdapter = ImageListAdapter(memoViewModel)
        viewDataBinding.rvImage.apply {
            adapter = imageListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        viewDataBinding.btnAddImage.setOnClickListener { showImageTypeDialog() }

        viewDataBinding.toolbar.tvSave.setOnClickListener {
            memoViewModel.saveMemo(viewDataBinding.etTitle.text.toString(), viewDataBinding.etContent.text.toString())
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 메모 내 이미지 추가 및 삭제 옵저빙
        memoViewModel.memoImageList.observe(viewLifecycleOwner, Observer {
            Log.d("LOG>>", "이미지 리스트[${it.size}개] : $it")
            imageListAdapter.submitList(it)
            imageListAdapter.notifyDataSetChanged()
        })
    }

    /**
     * 어떤 방식으로 이미지를 추가할 것인지(카메라, 갤러리, 링크) 선택하는 다이얼로그 보여줌
     * */
    private fun showImageTypeDialog() {
        activity?.let {activity ->
            AddMediaToMemoDialog.newInstance().apply {

                // 다이얼로그로부터 리턴값 받음 (이미지 경로)
                setMediaDialogResult(object : AddMediaToMemoDialog.OnMediaDialogResult{
                    // 링크를 추가한 경우
                    override fun getImageLink(url: String, isValid: Boolean) {
                        if(isValid)
                            //이미지를 정상적으로 받아왔을 때 -> 리스트에 이미지 추가
                            memoViewModel.addImage(url)
                        else
                            Toast.makeText(activity,"링크가 유효하지 않습니다\uD83D\uDE2D",Toast.LENGTH_LONG).show()

                        dismiss()
                    }

                    // 카메라 및 갤러리에서 사진 받아올 경우
                    override fun getImagePath(imagePath: String?) {
                        imagePath?.let {
                            //이미지를 정상적으로 받아왔을 때 -> 리스트에 이미지 추가
                            memoViewModel.addImage(it)
                        }  ?: Toast.makeText(activity,"이미지를 업로드하는데 실패했습니다\uD83D\uDE2D",Toast.LENGTH_LONG).show()

                        dismiss()
                    }
                })

            }.show(activity.supportFragmentManager, "AddMediaToMemoDialog")
        }
    }

}
