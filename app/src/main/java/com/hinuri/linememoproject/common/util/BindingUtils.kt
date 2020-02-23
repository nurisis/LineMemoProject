package com.hinuri.linememoproject.common.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

@BindingAdapter("imagePath")
fun loadImage(imageView: ImageView, @Nullable path:String?) {
    path?.let {
        var myOptions = RequestOptions().centerCrop()

        Glide.with(imageView.context)
            .load(it)
            .apply(myOptions)
            .into(imageView)
    }
}

@BindingConversion
fun convertBooleanToVisibility(visible:Boolean) :Int {
    return if(visible) View.VISIBLE else View.GONE
}
