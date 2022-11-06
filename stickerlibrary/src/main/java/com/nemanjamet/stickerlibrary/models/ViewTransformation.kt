package com.nemanjamet.stickerlibrary.models

class ViewTransformation (
    var translationX: Float = 0f,
    var translationY: Float = 0f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var rotation: Float = 0f,
    var wPercent: Float = 0f,
    var hPercent: Float = 0f,
    var width: Int = 0,
    var height: Int = 0
)

{
    fun copy(): ViewTransformation {
        val transformation = ViewTransformation()

        transformation.translationX = translationX
        transformation.translationY = translationY
        transformation.scaleX = scaleX
        transformation.scaleY = scaleY
        transformation.rotation = rotation

        transformation.wPercent = wPercent
        transformation.hPercent = hPercent
        transformation.width = width
        transformation.height = height

        return transformation
    }

    fun setTransformationSize(wMultiplier: Float, hMultiplier: Float) {
        translationX *= wMultiplier
        translationY *= hMultiplier
        width = (width * wMultiplier).toInt()
        height = (height * hMultiplier).toInt()
    }
}