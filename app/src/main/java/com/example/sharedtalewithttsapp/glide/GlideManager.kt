package com.example.sharedtalewithttsapp.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sharedtalewithttsapp.R

class GlideManager {
    companion object {
        val instance = GlideManager()
    }

    fun serverImageRequest(activity: Context, url : String, width : Int, height: Int, imageView: ImageView){
        Glide.with(activity)
            .load(url)
            .override(width, height)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }
}