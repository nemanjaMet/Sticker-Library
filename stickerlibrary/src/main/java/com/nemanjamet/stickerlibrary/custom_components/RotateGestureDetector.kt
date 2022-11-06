package com.nemanjamet.stickerlibrary.custom_components

import android.view.MotionEvent
import android.view.View
import com.nemanjamet.stickerlibrary.custom_views.ImageStickerView
import com.nemanjamet.stickerlibrary.custom_views.TextStickerView
import kotlin.math.atan2

class RotateGestureDetector {

    private var rotation = 0f
    private var oldRotation = 0f
    private var isRotationEnabled = true
    private var isResetStartValues = false

    fun onTouch(view: View?, motionEvent: MotionEvent?, isStickerUpdateEnabled: Boolean = true) {

        if (!isRotationEnabled)
            return

        when (motionEvent?.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                isResetStartValues = false
                rotation = view?.rotation ?: 0f
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldRotation = rotation(motionEvent)
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
                    val newRotation = rotation(motionEvent)
                    rotation += newRotation - oldRotation
                    oldRotation = newRotation
                }
            }

        }

        if (isStickerUpdateEnabled)
            updateRotation(view, rotation)

//            Log.d(TAG, "rotation: $rotation")
    }

    private fun updateRotation(view: View?, rotation: Float) {

        view?.let { v ->

            when (v) {
                is ImageStickerView -> {
                    v.setStickerRotation(rotation)
                }

                is TextStickerView -> {
                    v.setStickerRotation(rotation)
                }

                else -> {
                    v.rotation = rotation
                }
            }

        }

    }

    private fun resetStartValues(view: View?, motionEvent: MotionEvent) {
        isResetStartValues = false
        rotation = view?.rotation ?: 0f
        //oldRotation = rotation(motionEvent)
    }

    private fun rotation(event: MotionEvent) : Float {
        val deltaX = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(deltaY, deltaX)

        return Math.toDegrees(radians).toFloat()
    }

    fun enable() {
        isRotationEnabled = true
    }

    fun disable() {
        isRotationEnabled = false
    }

    fun resetStartValues() {
        isResetStartValues = true
    }

}