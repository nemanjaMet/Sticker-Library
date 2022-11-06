package com.example.stickerlibrary.models

import android.net.Uri
import java.util.*

data class ImageStickerData (
    val id: UUID = UUID.randomUUID(),
    var uri: Uri? = null,
    var drawableId: Int = 0,
    var transformation: ViewTransformation = ViewTransformation()
)

{
    fun isEqual(id: UUID): Boolean {
        return this.id == id
    }

    fun copy(): ImageStickerData {
        val imageStickerData = ImageStickerData(id)
        imageStickerData.uri = uri
        imageStickerData.drawableId = drawableId
        imageStickerData.transformation = transformation.copy()

        return imageStickerData
    }

}