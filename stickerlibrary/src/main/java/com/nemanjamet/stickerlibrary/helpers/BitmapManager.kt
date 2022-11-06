package com.nemanjamet.stickerlibrary.helpers

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import kotlin.math.min

object BitmapManager {

    // scaling bitmap
    fun getScaledBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {

        val scaledBitmap: Bitmap = Bitmap.createBitmap(width, height, bitmap.config)
            .copy(Bitmap.Config.ARGB_8888, true)

        val canvas2 = Canvas(scaledBitmap)
        val cameraScaleFactor = min(
            height.toFloat() / bitmap.height.toFloat(),
            height.toFloat() / bitmap.width.toFloat()
        )

        canvas2.translate(
            width.toFloat() / 2 - bitmap.width.toFloat() / 2,
            height / 2f - bitmap.height / 2f
        )
        canvas2.scale(
            cameraScaleFactor,
            cameraScaleFactor,
            bitmap.width.toFloat() / 2,
            bitmap.height / 2f
        )
        //canvas2.drawBitmap(bitmap, 0f, 0f, null)
        canvas2.drawBitmap(bitmap, 0f, 0f, Paint(Paint.ANTI_ALIAS_FLAG))

        return scaledBitmap
    }

    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        Log.d("decodeSampledBitmap", "resId: $resId, w: $reqWidth, h: $reqHeight")
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}