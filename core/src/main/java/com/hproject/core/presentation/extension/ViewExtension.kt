package com.hproject.core.presentation.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible

fun View.expand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    isVisible = true
    layoutParams.height = 0

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            when (interpolatedTime.toInt() == 1) {
                true -> layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                false -> layoutParams.height = (targetHeight * interpolatedTime).toInt()
            }
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = ((targetHeight / resources.displayMetrics.density) * 0.5).toLong()

    startAnimation(animation)
}

fun View.collapse() {
    val initialHeight = measuredHeight
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            when (interpolatedTime.toInt() == 1) {
                true -> {
                    isGone = true
                }
                false -> {
                    layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    requestLayout()
                }
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = ((initialHeight / resources.displayMetrics.density) * 0.5).toLong()
    startAnimation(animation)
}

fun View.expandCollapseToggle() {
    when {
        isVisible -> collapse()
        isGone -> expand()
    }
}