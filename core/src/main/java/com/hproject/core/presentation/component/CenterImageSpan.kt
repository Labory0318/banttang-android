package com.hproject.core.presentation.component

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

class CenterImageSpan(drawable: Drawable) : ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM) {
    private var mDrawableRef: WeakReference<Drawable?>? = null
    override fun getSize(
        paint: Paint, text: CharSequence,
        start: Int, end: Int,
        fm: FontMetricsInt?
    ): Int {
        val drawable = cachedDrawable
        val rect: Rect? = drawable?.bounds
        if (fm != null) {
            val pfm = paint.fontMetricsInt
            // keep it the same as paint's fm
            fm.ascent = pfm.ascent
            fm.descent = pfm.descent
            fm.top = pfm.top
            fm.bottom = pfm.bottom
            fm.leading = pfm.leading
        }
        return rect?.right ?: 0
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val drawable = cachedDrawable
        canvas.save()
        if (drawable != null) {
            val drawableHeight = drawable.intrinsicHeight
            val transY = y.toFloat() - (drawableHeight * 0.75f)

            canvas.translate(x, transY)
            drawable.draw(canvas)
            canvas.restore()
        }
    }

    private val cachedDrawable: Drawable?
        get() {
            val wr = mDrawableRef
            var d: Drawable? = null
            if (wr != null) d = wr.get()
            if (d == null) {
                d = drawable
                mDrawableRef = WeakReference(d)
            }
            return d
        }
}