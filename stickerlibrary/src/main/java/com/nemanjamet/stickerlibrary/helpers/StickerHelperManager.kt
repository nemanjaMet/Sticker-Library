package com.nemanjamet.stickerlibrary.helpers

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.nemanjamet.stickerlibrary.custom_views.ImageStickerView
import com.nemanjamet.stickerlibrary.custom_views.TextStickerView
import com.nemanjamet.stickerlibrary.models.ImageStickerData
import com.nemanjamet.stickerlibrary.models.TextStickerData

object StickerHelperManager {

    const val DELETE_STICKER_ICON_SIZE = 0.15f

    fun getScreenWidth(context: Context): Int {

        val dm = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30 >=
            windowManager.currentWindowMetrics.bounds.width()
        } else { // OLD API
            windowManager.defaultDisplay.getMetrics(dm)
            dm.widthPixels
        }

    }

    fun getScreenHeight(context: Context): Int {

        val dm = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30 >=
            windowManager.currentWindowMetrics.bounds.height()
        } else { // OLD API
            windowManager.defaultDisplay.getMetrics(dm)
            dm.heightPixels
        }

    }

    fun getDeleteStickerIconSize(context: Context) : Int {
        val screenWidth = getScreenWidth(context)
        return (screenWidth * 0.1f).toInt()
    }

    fun getStickerImage(context: Context, stickerHolder: ConstraintLayout, bitmapSize: Size): Bitmap {

        val stickerData = mutableListOf<Any>()


        for (i in 0 until stickerHolder.childCount) {

            when (val v = stickerHolder.getChildAt(i)) {
                is ImageStickerView -> {
                    stickerData.add(v.getData())
                }

                is TextStickerView -> {
                    stickerData.add(v.getData())
                }

                else -> {

                }
            }

        }

        val widthPercentDiff = bitmapSize.width / stickerHolder.width.toFloat()
        val heightPercentDiff = bitmapSize.height / stickerHolder.height.toFloat()

        Log.d("decodeSampledBitmap", " ${bitmapSize.width} / ${stickerHolder.width} = $widthPercentDiff")
        Log.d("decodeSampledBitmap", " ${bitmapSize.height} / ${stickerHolder.height} = $heightPercentDiff")
        //Log.d("decodeSampledBitmap", "widthPercentDiff: $widthPercentDiff, heightPercentDiff: $heightPercentDiff")

        val bitmap = Bitmap.createBitmap(bitmapSize.width, bitmapSize.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        stickerData.forEach { sticker ->

            when (sticker) {

                is ImageStickerData -> {
                    drawImageSticker(context, sticker, canvas, PointF(widthPercentDiff, heightPercentDiff))
                }

                is TextStickerData -> {
                    drawTextSticker(context, sticker, canvas,  PointF(widthPercentDiff, heightPercentDiff))
                }

            }

        }

        return bitmap
    }

//    private fun drawImageSticker(context: Context, stickerData: ImageStickerData, canvas: Canvas, sizeDiff: PointF) {
//
//        val stickerTransform = stickerData.transformation.copy()
//        stickerTransform.setTransformationSize(sizeDiff.x, sizeDiff.y)
//
//        val imagePadding = (getScreenWidth(context) * 0.01f).toInt()
//        val padding = 1f - (imagePadding.toFloat() / stickerTransform.width) * 2f
//        val stickerWidth = stickerTransform.width //* (1f - padding * 2f)).toInt() //- imagePadding
//        val stickerHeight = stickerTransform.height //* (1f - padding * 2f)).toInt() //- imagePadding
//        val bitmap = BitmapManager.getScaledBitmap(BitmapManager.decodeSampledBitmapFromResource(context.resources, stickerData.drawableId, stickerWidth, stickerHeight), stickerWidth, stickerHeight)
//
//        //val bitmap =  ContextCompat.getDrawable(context, stickerData.drawableId)?.toBitmap()!! //BitmapFactory.decodeResource(context.resources, stickerData.drawableId)
//
//        Log.d("drawImageSticker", "translation: ${PointF(stickerTransform.translationX, stickerTransform.translationY)}")
//        Log.d("drawImageSticker", "rotation: ${stickerTransform.rotation}")
//        Log.d("drawImageSticker", "padding: ${padding}")
//
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        canvas.save()
//
//        //canvas.translate(stickerTransform.translationX + (canvas.width / 2f -( stickerTransform.width / 2f * padding)),  stickerTransform.translationY + (canvas.height / 2f - stickerTransform.height / 2f * padding))
//        canvas.translate(stickerTransform.translationX + (canvas.width / 2f -( stickerTransform.width * stickerTransform.scaleX / 2f * padding)),  stickerTransform.translationY + (canvas.height / 2f - stickerTransform.height  * stickerTransform.scaleY / 2f * padding))
//        //* (1f - padding * 2f)
//        //val scaleXTest = (stickerTransform.width / 2f * padding  * stickerTransform.scaleX / 2f)
//        //val scaleYTest = (stickerTransform.height / 2f  * padding * stickerTransform.scaleY /2f)
//        canvas.scale(stickerTransform.scaleX * padding, stickerTransform.scaleY * padding)
//        //canvas.rotate(stickerTransform.rotation, 0.5f, 0.5f)
//
//        val matrix = Matrix()
//        matrix.postRotate(stickerTransform.rotation, stickerTransform.width / 2f, stickerTransform.height / 2f)
//        //matrix.postRotate(stickerTransform.rotation)
//
//        canvas.drawBitmap(bitmap, matrix, paint)
//
//        canvas.restore()
//
//    }

    private fun drawImageSticker(context: Context, stickerData: ImageStickerData, canvas: Canvas, sizeDiff: PointF) {

        val stickerTransform = stickerData.transformation.copy()
        stickerTransform.setTransformationSize(sizeDiff.x, sizeDiff.y)

        val imagePadding = (getScreenWidth(context) * 0.01f).toInt()
        val padding = 1f - (imagePadding.toFloat() / stickerTransform.width) * 2f
        val stickerWidth = (stickerTransform.width * stickerTransform.scaleX * padding).toInt()
        val stickerHeight = (stickerTransform.height * stickerTransform.scaleY * padding).toInt()
        val bitmap = BitmapManager.getScaledBitmap(BitmapManager.decodeSampledBitmapFromResource(context.resources, stickerData.drawableId, stickerWidth, stickerHeight), stickerWidth, stickerHeight)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.save()

        canvas.translate(stickerTransform.translationX + (canvas.width / 2f -( stickerTransform.width * stickerTransform.scaleX / 2f * padding)),  stickerTransform.translationY + (canvas.height / 2f - stickerTransform.height  * stickerTransform.scaleY / 2f * padding))
        //canvas.scale(stickerTransform.scaleX * padding, stickerTransform.scaleY * padding)

        val matrix = Matrix()
        matrix.postRotate(stickerTransform.rotation, stickerWidth / 2f, stickerHeight / 2f)

        canvas.drawBitmap(bitmap, matrix, paint)

        canvas.restore()

    }

        private fun drawTextSticker(context: Context, stickerData: TextStickerData, canvas: Canvas, sizeDiff: PointF) {

            val textPadding = (getScreenWidth(context) * 0.01f).toInt()
            val scaledPadding = textPadding * stickerData.transformation.scaleX

            val textPaint = TextPaint()
            textPaint.isAntiAlias = true
            textPaint.textSize = stickerData.textSize
            textPaint.color = stickerData.color
            textPaint.typeface = stickerData.typeface

            val stickerTransform = stickerData.transformation
            val text = stickerData.text
            val width = stickerData.transformation.width
            val height = stickerData.transformation.height

            val textRect = Rect()
            textPaint.getTextBounds(text, 0, text.length, textRect)

            val staticLayout =
                StaticLayout.Builder.obtain(text, 0, text.length, textPaint, width)
                    .setAlignment(Layout.Alignment.ALIGN_CENTER)
                    .setLineSpacing(0f, 1f)
                    .setIncludePad(false).build()

            canvas.save()

            canvas.translate(stickerTransform.translationX + (canvas.width / 2f -((width / 2f ) * stickerTransform.scaleX)),  stickerTransform.translationY + (canvas.height / 2f - (height / 2f) * stickerTransform.scaleY) + scaledPadding)
            canvas.scale(stickerData.transformation.scaleX, stickerData.transformation.scaleY)
            canvas.rotate(stickerData.transformation.rotation, staticLayout.width / 2f, staticLayout.height / 2f)

            staticLayout.draw(canvas)

            canvas.restore()
        }


}