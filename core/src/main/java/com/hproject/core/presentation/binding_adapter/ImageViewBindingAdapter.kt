package com.hproject.core.presentation.binding_adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import timber.log.Timber


/**
 * [ImageView] 관련 UI Extension 모음
 *
 * @author hsjun
 * @since 2022/07/29
 */
object ImageViewBindingAdapter {

    /**
     * URL 이미지 호출
     *
     * @param imageUrl      이미지 URL
     * @param placeholder   로딩시 출력될 이미지
     * @author thomas
     * @since 2022/11/24
     **/
    @JvmStatic
    @BindingAdapter(
        value = [
            "imageUrl",
            "placeholder"
        ],
        requireAll = false
    )
    fun ImageView.loadImageByUrl(
        imageUrl: String?,
        placeholder: Drawable? = null,
    ) {
        if (imageUrl.isNullOrBlank()) {
            setImageDrawable(placeholder)
            return
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeholder)
            .fallback(placeholder)
            .dontAnimate()
            .dontTransform()
            .override(SIZE_ORIGINAL)
            .into(this)
    }

    fun ImageView.loadUrlAsync(
        url: String?,
        placeholder: Drawable? = null,
        circleCrop: Boolean = false,
        cornerRadius: Int? = null
    ) {
        Timber.i("[Glide] imageUrl=$url, circleCrop=$circleCrop, cornerRadius=$cornerRadius")

        if (!url.isNullOrEmpty()) {
            var builder = Glide.with(context)
                .load(url)
                .centerCrop()
            when {
                cornerRadius != null && cornerRadius > 0 -> {
                    builder = builder.transform(CenterCrop(), RoundedCorners(cornerRadius))
                }
                circleCrop -> {
                    builder = builder.circleCrop()
                }
            }
            builder.placeholder(placeholder)
                .into(this)
        } else {
            Glide.with(context)
                .load(placeholder)
                .into(this)
        }
    }
}