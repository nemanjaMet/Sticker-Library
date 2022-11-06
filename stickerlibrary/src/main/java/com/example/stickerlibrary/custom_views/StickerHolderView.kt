package com.example.stickerlibrary.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.stickerlibrary.R
import com.example.stickerlibrary.constants.Constants
import com.example.stickerlibrary.contoller.StickerController
import com.example.stickerlibrary.contoller.StickerHolderController
import com.example.stickerlibrary.custom_components.OnStickerTouchListener
import com.example.stickerlibrary.helpers.StickerHelperManager
import com.example.stickerlibrary.models.ImageStickerData
import com.example.stickerlibrary.models.TextStickerData


class StickerHolderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val stickerController = StickerController()
    val controller = StickerHolderController(stickerController)

    private var lastTimeStickerClicked = 0L

    companion object {
        private const val DELETE_STICKER = 0
        private const val EDIT_STICKER = 1
        private const val STICKER_HOLDER = 2
    }

    init {
        createStickerHolder()
        initStickerController()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //setButtonIconPosition()


        getDeleteStickerIcon()?.setButtonPosition()
        getEditStickerIcon()?.setButtonPosition()

    }

    private fun initStickerController()  {
        addStickerTouchListener(null)
        addStickerTouchListener(stickerController.getStickerTouchListener())
    }


//    private fun setButtonIconPosition(sizeWPercent: Float = StickerHelperManager.DELETE_STICKER_ICON_SIZE, xPercent: Float = 0.5f, yPercent: Float = 0.85f) {
//        if (this.childCount > 0) {
//            val iconSize = (width * sizeWPercent).toInt() //StickerHelperManager.getDeleteStickerIconSize(context)
//            val stickerIcon = getDeleteStickerIcon()
//            stickerIcon.translationX = (width * xPercent - iconSize / 2)
//            stickerIcon.translationY = (height * yPercent - iconSize / 2)
//
//            stickerIcon.layoutParams = LayoutParams(iconSize, iconSize)
//        }
//    }

    private fun ImageView.setButtonPosition(sizeWPercent: Float = StickerHelperManager.DELETE_STICKER_ICON_SIZE, xPercent: Float = 0.5f, yPercent: Float = 0.85f) {

            val iconSize = (width * sizeWPercent).toInt() //StickerHelperManager.getDeleteStickerIconSize(context)
            this.translationX = (width * xPercent - iconSize / 2f)
            this.translationY = (height * yPercent - iconSize / 2f)

            this.layoutParams = LayoutParams(iconSize, iconSize)

    }

    private fun createStickerHolder() {

        //val deleteIconSize = StickerHelperManager.getDeleteStickerIconSize(context)

        val deleteStickerIcon = ImageView(context)
        //deleteStickerIcon.layoutParams = LayoutParams(deleteIconSize, deleteIconSize)
        deleteStickerIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_stickerlibrary))
        //setButtonIconPosition()
        deleteStickerIcon.setButtonPosition()
        deleteStickerIcon.setColorFilter(Color.RED)
        //deleteStickerIcon.visibility = View.INVISIBLE
        deleteStickerIcon.alpha = 0.4f

        val editStickerIcon = ImageView(context)
        editStickerIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit_stickerlibrary))
        //setButtonIconPosition()
        editStickerIcon.setButtonPosition()
        editStickerIcon.setColorFilter(Color.RED)
        //editStickerIcon.visibility = View.INVISIBLE
        editStickerIcon.alpha = 0.4f
        editStickerIcon.visibility = View.GONE


        val stickerHolder = ConstraintLayout(context)
        stickerHolder.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        addView(deleteStickerIcon)
        addView(editStickerIcon)
        addView(stickerHolder)

    }

    fun onDeletePressed() {
        getDeleteStickerIcon()?.alpha = 1.0f
    }

    fun onDeleteReleased() {
        getDeleteStickerIcon()?.alpha = 0.4f
    }

    private fun getDeleteStickerIcon(): ImageView? {
        if (childCount < DELETE_STICKER)
            return getChildAt(DELETE_STICKER) as ImageView
        return null
    }

    private fun getStickerHolder(): ConstraintLayout {
        return getChildAt(STICKER_HOLDER) as ConstraintLayout
    }

    private fun getEditStickerIcon(): ImageView? {
        if (childCount < EDIT_STICKER)
            return getChildAt(EDIT_STICKER) as ImageView
        return null
    }

//    fun addSticker(view: View) {
//        getStickerHolder().addView(view)
//    }

//    private val _handler = Handler(Looper.getMainLooper())
//    var longPressed = Runnable { Log.i("TestLPress", "LongPress handler") }

    private var numberOfStickers = 0

    private fun isFirstStickerTouch(): Boolean {
        return if (System.currentTimeMillis() - lastTimeStickerClicked > 100) {
            lastTimeStickerClicked = System.currentTimeMillis()
            true
        } else
            false
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addSticker(stickerData: ImageStickerData, stickerSizePercent: Float = Constants.IMAGE_STICKER_SIZE, autoSelectSticker: Boolean = true) {

        Log.d(Constants.LOG.DEFAULT, "add ImageSticker")
        if (updateSticker(stickerData)) {
            Log.d(Constants.LOG.DEFAULT, "ImageSticker updated")
            return
        }

        val imageStickerView = ImageStickerView(context)
        imageStickerView.setData(stickerData)

//        val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
//            override fun onLongPress(e: MotionEvent) {
//                Log.d("TestLPress", "Longpress detected")
//
//                stickerController.getStickerTouchListener()?.updateView(imageStickerView)
//
//                imageStickerView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
//
//            }
//
//            override fun onDown(e: MotionEvent?): Boolean {
//                Log.d("TestLPress", "onDown")
//
//                return !(stickerController.getStickerTouchListener()?.isStickerSelected() ?: false)
//                //return true
//            }
//
//
//
//        })

//        val _handler = Handler(Looper.getMainLooper())
//        val longPressed = Runnable {
//            Log.i("TestLPress", "LongPress handler")
//            stickerController.getStickerTouchListener()?.updateView(imageStickerView)
//            imageStickerView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
//        }

        imageStickerView.setOnTouchListener { view, motionEvent ->

//            if (gestureDetector.onTouchEvent(motionEvent)) {
//                Log.d("TestLPress", "if press")
//                return@setOnTouchListener true
//            }

            when (motionEvent?.actionMasked) {

                MotionEvent.ACTION_DOWN -> {


                    Log.d("TestLPress", "ACTION_DOWN")

                    if (isFirstStickerTouch()) {
                        stickerController.getStickerTouchListener()?.updateView(view)
                    }

                }

            }

            false
        }



        if (autoSelectSticker) {
            stickerController.getStickerTouchListener()?.updateView(imageStickerView, autoSelectSticker)
        } else {
            imageStickerView.onStickerDeselected()
        }

        imageStickerView.setHolderSize(Size(width, height))
        val stickerSize = (width * stickerSizePercent).toInt()
        imageStickerView.setStickerSize(Size(stickerSize, stickerSize))

        numberOfStickers++
        getStickerHolder().addView(imageStickerView)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addSticker(stickerData: TextStickerData, textSize: Float = Constants.TEXT_STICKER_SIZE,  autoSelectSticker: Boolean = true) {

        Log.d(Constants.LOG.DEFAULT, "add TextSticker")
        if (updateSticker(stickerData)) {
            Log.d(Constants.LOG.DEFAULT, "TextSticker updated")
            return
        }

        val textStickerView = TextStickerView(context)
        textStickerView.setData(stickerData)

        textStickerView.setOnTouchListener { view, motionEvent ->

            when (motionEvent?.actionMasked) {

                MotionEvent.ACTION_DOWN -> {

                    if (isFirstStickerTouch()) {
                        stickerController.getStickerTouchListener()?.updateView(view)
                    }

                }

            }


            false
        }


//        textStickerView.setOnLongClickListener { view ->
//
//            stickerController.getStickerTouchListener()?.updateView(view)
//
//            false
//        }

        if (autoSelectSticker) {
            stickerController.getStickerTouchListener()?.updateView(textStickerView, autoSelectSticker)
        } else {
            textStickerView.onStickerDeselected()
        }

        textStickerView.setHolderSize(Size(width, height))
        textStickerView.setTextSize(textSize)

        getStickerHolder().addView(textStickerView)
    }

    fun addAllStickers(stickersData: MutableList<Any>, clearPreviousStickers: Boolean = false) {

        val stickerHolder = getStickerHolder()

        if (clearPreviousStickers)
            stickerHolder.removeAllViews()

        stickersData.forEach { s ->

            when (s) {
                is ImageStickerData -> addSticker(s, Constants.IMAGE_STICKER_SIZE, false)
                is TextStickerData -> addSticker(s, Constants.TEXT_STICKER_SIZE, false)
                else -> Log.e(Constants.LOG.DEFAULT, "Unsupported sticker data $s")
            }

        }
    }

//    fun updateAllStickers(stickersData: MutableList<Any>) {
//
//    }

    fun updateSticker(stickerData: Any): Boolean {

//        when (stickerData) {
//            is ImageStickerData -> addSticker(stickerData)
//            is TextStickerData -> addSticker(stickerData)
//            else -> Log.e(Constants.LOG.DEFAULT, "Unsupported sticker data $stickerData")
//        }

        var isUpdated = false

        val stickerHolder = getStickerHolder()

        for (i in 0 until stickerHolder.childCount) {
            val sticker = stickerHolder.getChildAt(i)

            if (sticker is ImageStickerView && stickerData is ImageStickerData && sticker.getData().isEqual(stickerData.id)) {

                sticker.setData(stickerData)

                isUpdated = true
            } else if (sticker is TextStickerView && stickerData is TextStickerData && sticker.getData().isEqual(stickerData.id)) {

                sticker.setData(stickerData)

                isUpdated = true
            }

        }

        return isUpdated
    }

    fun setListener(listener: OnStickerTouchListener.OnStickerTouchListener) {
        stickerController.setListener(listener)
    }

    fun setDeleteStickerIconVisibility(isVisible: Boolean) {
        if (isVisible) {
            getDeleteStickerIcon()?.visibility = View.VISIBLE

        }
        else {
            getDeleteStickerIcon()?.visibility = View.INVISIBLE
        }
    }

    fun setEditStickerIconVisibility(isVisible: Boolean) {
        if (isVisible) getEditStickerIcon()?.visibility = View.VISIBLE
        else getEditStickerIcon()?.visibility = View.INVISIBLE
    }

    fun setDeleteButtonPosition(sizeWPercent: Float = StickerHelperManager.DELETE_STICKER_ICON_SIZE, xPercent: Float = 0.5f, yPercent: Float = 0.85f) {
        getDeleteStickerIcon()?.setButtonPosition(sizeWPercent, xPercent, yPercent)
    }

    fun setEditButtonPosition(sizeWPercent: Float = StickerHelperManager.DELETE_STICKER_ICON_SIZE, xPercent: Float = 0.5f, yPercent: Float = 0.85f) {
        getEditStickerIcon()?.setButtonPosition(sizeWPercent, xPercent, yPercent)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addStickerTouchListener(touchListener: OnStickerTouchListener?) {
        getStickerHolder().setOnTouchListener(touchListener)
    }

    fun getDefaultLayoutParams(): ViewGroup.LayoutParams {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.leftToLeft = LayoutParams.PARENT_ID
        layoutParams.rightToRight = LayoutParams.PARENT_ID
        layoutParams.topToTop = LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID

        return layoutParams
    }

//    fun getSelectedSticker(): View? {
//        return stickerController.getSelectedSticker()
//    }

    fun removeSelectedSticker() {
        controller.getSelectedSticker()?.let {
            //getStickerHolder().removeView(it)
            stickerController.getStickerTouchListener()?.deleteSelectedSticker()
        }

    }

    fun getStickersImage(size: Size): Bitmap {
        return StickerHelperManager.getStickerImage(context, getStickerHolder(), size)
    }

}