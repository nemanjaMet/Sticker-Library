package com.example.stickerlibrary.custom_views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Size
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.stickerlibrary.R
import com.example.stickerlibrary.helpers.StickerHelperManager
import com.example.stickerlibrary.models.TextStickerData

class TextStickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var holderSize = Size(0,0)

    companion object {
        private const val TEXT_VIEW = 0
        private const val DELETE_BUTTON = 1
    }

    init {
        createTextSticker()
    }

    private var data = TextStickerData()

    fun setData(data: TextStickerData) {
        this.data = data
        //data.id = id

        reloadData()
    }

    fun getData(): TextStickerData {
        return this.data
    }

    fun setHolderSize(size: Size) {
        this.holderSize = size
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        data.transformation.width = w
        data.transformation.height = h
    }

    fun setTextSize(size: Float) {
        val textView = getTextView()
        //Log.v("setTextSize", "1) TextSize: ${textView.textSize}")
        val newTextSize = textView.textSize * size
        //textView.textSize = newTextSize
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize)
        //Log.v("setTextSize", "2) TextSize: ${textView.textSize}, newTextSize: $newTextSize, sizeMultiply: $size")
        data.textSize = textView.textSize
    }

    private fun reloadData() {
        val textView = getTextView()

        data.apply {
            textView.typeface = typeface
            textView.setTextColor(color)
            textView.text = text
        }

        data.transformation.let { t ->
            translationX = t.translationX
            translationY = t.translationY
            scaleX = t.scaleX
            scaleY = t.scaleY
            rotation = t.rotation
        }
    }

    private fun createTextSticker() {
        this.layoutParams = HelperTextSticker.getDefaultLayoutParams()
        this.background = ContextCompat.getDrawable(context, R.drawable.sticker_border)

        clipChildren = false
        clipToPadding = false

        setTextHolder()
        //setDeleteButton()
    }

    private fun setTextHolder() {
        val textPadding = (StickerHelperManager.getScreenWidth(context) * 0.01f).toInt()

        val textView = TextView(context)
        textView.layoutParams = HelperTextSticker.getDefaultLayoutParams()
        textView.setPadding(textPadding)
        textView.includeFontPadding = false

        textView.gravity = Gravity.CENTER

        addView(textView)
    }

    private fun setDeleteButton() {

        val ivDelete = ImageView(context)
        val buttonSize = (StickerHelperManager.getScreenWidth(context) * 0.07f).toInt()
        ivDelete.setColorFilter(Color.RED)
        ivDelete.layoutParams = LayoutParams(buttonSize, buttonSize)
        ivDelete.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_stickerlibrary))


        val translation =  - buttonSize / 2f
        ivDelete.translationX = translation
        ivDelete.translationY = translation

        //ivDelete.layout(translation.toInt(), translation.toInt(), buttonSize, buttonSize)

//        ivDelete.setOnClickListener {
//            onStickerDelete()
//        }

        addView(ivDelete)

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

    private fun getTextView(): TextView {
        return getChildAt(TEXT_VIEW) as TextView
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

//        data.transformation.width = width
//        data.transformation.height = height

        data.textSize = getData().textSize

    }

//    private fun getDeleteButton(): ImageView {
//        return getChildAt(DELETE_BUTTON) as ImageView
//    }

    private object HelperTextSticker {

        fun getDefaultLayoutParams(): ViewGroup.LayoutParams {
            val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams.leftToLeft = LayoutParams.PARENT_ID
            layoutParams.rightToRight = LayoutParams.PARENT_ID
            layoutParams.topToTop = LayoutParams.PARENT_ID
            layoutParams.bottomToBottom = LayoutParams.PARENT_ID

            return layoutParams
        }

    }

}