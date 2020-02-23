package com.hinuri.linememoproject.addmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hinuri.linememoproject.databinding.FragmentAddImageLinkBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddImageLinkFragment : Fragment() {

    private val mediaViewModel by sharedViewModel<AddMediaViewModel>()
    private lateinit var viewDataBinding : FragmentAddImageLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentAddImageLinkBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@AddImageLinkFragment
            viewModel = mediaViewModel
        }

        viewDataBinding.tvAddLink.setOnClickListener {
            if(!mediaViewModel.addImageLink(viewDataBinding.etLink.text.toString()))
                Toast.makeText(context, "유효하지 않은 링크입니다!", Toast.LENGTH_LONG).show()
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mediaViewModel.isImageLinkAdded.observe(viewLifecycleOwner, Observer {
            if(it) {
                activity?.onBackPressed()
            }
        })
    }

}
