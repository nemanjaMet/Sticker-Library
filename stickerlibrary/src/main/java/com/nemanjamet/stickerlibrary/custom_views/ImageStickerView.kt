package com.nemanjamet.stickerlibrary.custom_views

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.nemanjamet.stickerlibrary.R
import com.nemanjamet.stickerlibrary.helpers.StickerHelperManager
import com.nemanjamet.stickerlibrary.models.ImageStickerData

class ImageStickerView constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var holderSize = Size(0,0)
    private var data = ImageStickerData()

    companion object {
        private const val IMAGE_VIEW = 0
    }

    init {
        createImageSticker()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        data.transformation.width = w
        data.transformation.height = h
    }

    private fun createImageSticker() {
        this.layoutParams = HelperImageSticker.getDefaultLayoutParams()
        this.background = ContextCompat.getDrawable(context, R.drawable.sticker_border)

        clipChildren = false
        clipToPadding = false

        setImageHolder()
    }

    private fun setImageHolder() {

        val imagePadding = (StickerHelperManager.getScreenWidth(context) * 0.01f).toInt()

        val imageView = ImageView(context)
        imageView.layoutParams = HelperImageSticker.getDefaultLayoutParams()
        imageView.setPadding(imagePadding)

        addView(imageView)

    }

    fun setData(data: ImageStickerData) {
        this.data = data
        //data.id = id

        reloadData()
    }

    fun getData(): ImageStickerData {
        return this.data
    }

    fun setHolderSize(size: Size) {
        this.holderSize = size
    }

    fun setStickerSize(size: Size) {
        layoutParams = HelperImageSticker.getDefaultLayoutParams(size.width, size.height)

        //getImageView().setPadding((size.width * 0.01f).toInt())
    }

    private fun reloadData() {
        val imageView = getImageView()

        imageView.setImageDrawable(ContextCompat.getDrawable(context, data.drawableId))

        data.transformation.let { t ->
           translationX = t.translationX
           translationY = t.translationY
           scaleX = t.scaleX
           scaleY = t.scaleY
           rotation = t.rotation

            t.width = width
            t.height = height
        }
    }

    private fun getImageView(): ImageView {
        return getChildAt(IMAGE_VIEW) as ImageView
    }

    fun onStickerSelected() {
        this.background = ContextCompat.getDrawable(context, R.drawable.sticker_border)
        //getDeleteButton().visibility = View.VISIBLE
        bringToFront()
    }

    fun onStickerDeselected() {
        //Log.d("isStickerClicked", "onStickerDeselected ")
        this.background = null
        //getDeleteButton().visibility = View.INVISIBLE
    }

    fun onStickerDelete() {
        (this.parent as? ViewGroup?)?.removeView(this)
    }

    fun setStickerScale(scaleX: Float, scaleY: Float) {
        this.scaleX = scaleX
        this.scaleY = scaleY

       updateDataTransformation()
    }

    fun setStickerTranslation(translationX: Float, translationY: Float) {
        this.translationX = translationX
        this.translationY = translationY

        updateDataTransformation()
    }

    fun setStickerRotation(rotation: Float) {
        this.rotation = rotation

        updateDataTransformation()
    }

    private fun updateDataTransformation() {

        // Translation
        data.transformation.translationX = translationX
        data.transformation.translationY = translationY

        // Scale
        data.transformation.scaleX = scaleX
        data.transformation.scaleY = scaleY

        // Rotation
        data.transformation.rotation = rotation

        // Width & Height
        data.transformation.width = width
        data.transformation.height = height
    }


    private object HelperImageSticker {
        fun getDefaultLayoutParams(width: Int = LayoutParams.WRAP_CONTENT, height: Int = LayoutParams.WRAP_CONTENT): ViewGroup.LayoutParams {
            val layoutParams = LayoutParams(width, height)
            layoutParams.leftToLeft = LayoutParams.PARENT_ID
            layoutParams.rightToRight = LayoutParams.PARENT_ID
            layoutParams.topToTop = LayoutParams.PARENT_ID
            layoutParams.bottomToBottom = LayoutParams.PARENT_ID

            return layoutParams
        }
    }

}