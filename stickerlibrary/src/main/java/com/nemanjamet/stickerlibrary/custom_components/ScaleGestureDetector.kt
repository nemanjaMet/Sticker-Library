package com.nemanjamet.stickerlibrary.custom_components

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import com.nemanjamet.stickerlibrary.custom_views.ImageStickerView
import com.nemanjamet.stickerlibrary.custom_views.TextStickerView
import kotlin.math.sqrt

class ScaleGestureDetector {

//        companion object {
//            private const val minScale = 0.3f
//            private const val maxScale = 3f
//        }

    //private var startScale = PointF(1f,1f)
    private var scale = PointF(1f,1f)
    private var oldDist = 0f
    private var startScale = PointF(1f, 1f)
    private var isScaleEnabled = true
    private var minScale = 0.3f
    private var maxScale = 3f
    private var isResetStartValues = false

    fun onTouch(view: View?, motionEvent: MotionEvent?, isStickerUpdateEnabled: Boolean = true) {

        if (!isScaleEnabled)
            return

        when (motionEvent?.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                isResetStartValues = false
                scale.set(view?.scaleX ?: 1f, view?.scaleY ?: 1f)
                //Log.d(TAG, "ActionDown Scale: $scale")
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                startScale.set(view?.scaleX ?: 1f, view?.scaleY ?: 1f)
                oldDist = spacing(motionEvent)
                //Log.d(TAG, "ACTION_POINTER_DOWN oldDost: $oldDist")
            }

            MotionEvent.ACTION_POINTER_UP -> {

            }

            MotionEvent.ACTION_UP -> {

            }

            MotionEvent.ACTION_MOVE -> {

                if (isResetStartValues) {
                    resetStartValues(view, motionEvent)
                }

                if (motionEvent.pointerCount == 2) {
                    val newDist = spacing(motionEvent)

                    if (newDist > 10f) {
                        val newScale = (newDist / oldDist) * startScale.x

                        scale.set(newScale, newScale)
                        checkMinMaxScale()

                        //Log.d(TAG, "ACTION_MOVE Scale: $scale, newDist: $newDist")
                    }
                }
            }

        }

        if (isStickerUpdateEnabled)
            updateScale(view, scale.x, scale.y)
    }

    private fun updateScale(view: View?, scaleX: Float, scaleY: Float) {

        view?.let { v ->

            when (v) {
                is ImageStickerView -> {
                    v.setStickerScale(scaleX, scaleY)
                }

                is TextStickerView -> {
                    v.setStickerScale(scaleX, scaleY)
                }

                else -> {
                    v.scaleX = scaleX
                    v.scaleY = scaleY
                }
            }

        }

    }

    private fun spacing(event: MotionEvent) : Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)

        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    // provera da li je scale view-a u granicama
    private fun checkMinMaxScale() {
        val scaleX = scale.x

        if (scaleX < minScale)
            scale.set(minScale, minScale)
        else if (scaleX > maxScale)
            scale.set(maxScale, maxScale)
    }

    fun enable() {
        isScaleEnabled = true
    }

    fun disable() {
        isScaleEnabled = false
    }

    fun setMinMaxScale(minScale: Float, maxScale: Float) {

        if (minScale >= 0f)
            this.minScale = minScale

        if (maxScale > 0f)
            this.maxScale = maxScale
    }

    private fun resetStartValues(view: View?, motionEvent: MotionEvent) {
        isResetStartValues = false
        scale.set(view?.scaleX ?: 1f, view?.scaleY ?: 1f)

        startScale.set(view?.scaleX ?: 1f, view?.scaleY ?: 1f)
        //oldDist = spacing(motionEvent)
    }

    fun resetStartValues() {
        isResetStartValues = true
    }

}