package com.nemanjamet.stickerlibrary.custom_components

import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.nemanjamet.stickerlibrary.custom_views.ImageStickerView
import com.nemanjamet.stickerlibrary.custom_views.TextStickerView
import kotlin.math.abs

class MoveGestureDetector {

    //private var position = PointF(0f,0f)
    private var startPoint = PointF(0f, 0f)
    private var startTranslate = PointF(0f, 0f)
    private var translation = PointF(0f, 0f)
    private var isTranslationEnabled = true
    private var isStartPointReset = false
    private var isResetStartValues = false

    fun onTouch(view: View?, motionEvent: MotionEvent?, isStickerUpdateEnabled: Boolean = true) {

        if (!isTranslationEnabled)
            return

        when (motionEvent?.actionMasked) {

            MotionEvent.ACTION_DOWN -> {

                Log.d("MoveGestureDetector", "ACTION_DOWN")

                resetStartValues(view, motionEvent)

                //Log.d(TAG, "startPoint: $startPoint, startTranslation: $startTranslate")
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.d("MoveGestureDetector", "ACTION_POINTER_DOWN")
//                    startPoint.set(motionEvent.x, motionEvent.y)
//                    startTranslate.set(view?.translationX ?: 0f, view?.translationY ?: 0f)
//                    translation = startTranslate
                isStartPointReset = true
            }

            MotionEvent.ACTION_POINTER_UP -> {
                Log.d("MoveGestureDetector", "ACTION_POINTER_UP")
                isStartPointReset = true
            }

            MotionEvent.ACTION_UP -> {
                Log.d("MoveGestureDetector", "ACTION_UP")
            }

            MotionEvent.ACTION_MOVE -> {

                //Log.d("MoveGestureDetector", "ACTION_MOVE")
//                    if (motionEvent.pointerCount == 1) {
//                        translation.x = startTranslate.x + (motionEvent.x - startPoint.x)
//                        translation.y = startTranslate.y + (motionEvent.y - startPoint.y)
//                        startPoint.set(motionEvent.x, motionEvent.y)
//
//                        //Log.d(TAG, "move: $translation")
//                    } else {
//                        startPoint.set(motionEvent.x, motionEvent.y)
//                    }

                if  (isResetStartValues) {
                    resetStartValues(view, motionEvent)
                }

                if (isStartPointReset) {
                    isStartPointReset = false
                    startPoint.set(motionEvent.x, motionEvent.y)
                    //startTranslate.set(view?.translationX ?: 0f, view?.translationY ?: 0f)
                    //translation = startTranslate
                } else {
                    translation.x = startTranslate.x + (motionEvent.x - startPoint.x)
                    translation.y = startTranslate.y + (motionEvent.y - startPoint.y)
                    startPoint.set(motionEvent.x, motionEvent.y)
                }

            }

        }

        if (isStickerUpdateEnabled)
            updateTranslation(view, translation.x, translation.y)

    }

    private fun resetStartValues(view: View?, motionEvent: MotionEvent) {
        isResetStartValues = false

        startPoint.set(motionEvent.x, motionEvent.y)
        startTranslate.set(view?.translationX ?: 0f, view?.translationY ?: 0f)
        translation = startTranslate
    }

    private fun updateTranslation(view: View?, translationX: Float, translationY: Float) {

        view?.let { v ->

            when (v) {
                is ImageStickerView -> {
                    v.setStickerTranslation(translationX, translationY)
                }

                is TextStickerView -> {
                    v.setStickerTranslation(translationX, translationY)
                }

                else -> {
                    v.translationX = translation.x
                    v.translationY = translation.y
                }
            }

        }

    }

    fun enable() {
        isTranslationEnabled = true
    }

    fun disable() {
        isTranslationEnabled = false
    }

    fun isTouchJumping(x: Float, y: Float): Boolean {

        return abs(x - y) > 100

    }

    fun resetStartValues() {
        isResetStartValues = true
    }

}