package com.oratakashi.covid19.utils

import android.util.Log
import android.widget.ImageView
import com.oratakashi.covid19.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ImageHelper {
    companion object{
        fun getPicasso(imageView: ImageView, image_url : String){
            Picasso.get().load(image_url)
                .placeholder(R.drawable.img_no_images)
                .error(R.drawable.img_no_images)
                .into(imageView, object : Callback {
                    override fun onSuccess() {}
                    override fun onError(e: Exception) {
                        Log.e("Picasso", e.message!!)
                        Log.e("Picasso_URl", image_url)
                    }
                })
        }
    }
}