package com.nemanjamet.stickerlibrary.models

import android.graphics.Color
import android.graphics.Typeface
import java.util.*

data class TextStickerData (
    val id: UUID = UUID.randomUUID(),
    var text: String = "",
    var color: Int = Color.BLACK,
    var typeface: Typeface = Typeface.DEFAULT,
    var textSize: Float = 0f,
    var transformation: ViewTransformation = ViewTransformation()
)

{
    fun isEqual(id: UUID): Boolean {
        return this.id == id
    }

    fun copy(): TextStickerData {
        val textStickerData = TextStickerData(id)
        textStickerData.text = text
        textStickerData.color= color
        textStickerData.typeface = typeface
        textStickerData.transformation = transformation.copy()

        return textStickerData
    }

}