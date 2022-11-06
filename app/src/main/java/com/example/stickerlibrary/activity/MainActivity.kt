package com.example.stickerlibrary.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import com.example.stickerlibrary.R
import com.example.stickerlibrary.custom_components.OnStickerTouchListener
import com.example.stickerlibrary.custom_views.StickerHolderView
import com.example.stickerlibrary.models.ImageStickerData
import com.example.stickerlibrary.models.TextStickerData

class MainActivity : AppCompatActivity(), OnStickerTouchListener.OnStickerTouchListener {

    private var onStickerTouchListener: OnStickerTouchListener? = null
    private lateinit var stickerHolderView: StickerHolderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initStickerHolder()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        findViewById<TextView>(R.id.btn_add_image_sticker).setOnClickListener {
            addImageSticker()
        }

        findViewById<TextView>(R.id.btn_add_text_sticker).setOnClickListener {
            addTextSticker()
        }

        findViewById<TextView>(R.id.btn_delete_selected).setOnClickListener {
            deleteSelectedSticker()
            //showSavedImage()
        }

    }

    private fun initStickerHolder() {
        stickerHolderView = findViewById(R.id.sticker_holder_view)
        //binding?.clStickerHolder?.setOnTouchListener(null)
//        stickerHolderView.addStickerTouchListener(null)
//        onStickerTouchListener = OnStickerTouchListener(null, this)
//        //binding?.clStickerHolder?.setOnTouchListener(onStickerTouchListener)
//        stickerHolderView.addStickerTouchListener(onStickerTouchListener)

        stickerHolderView.setDeleteButtonPosition(0.15f, 0.1f, 0.85f)
        stickerHolderView.setEditButtonPosition(0.15f, 0.9f, 0.85f)
        stickerHolderView.setEditStickerIconVisibility(true)

        stickerHolderView.setListener(this)

        //stickerHolderView.controller.disableStickerRotation()
    }

    private fun addImageSticker() {
        val imageStickerData = ImageStickerData()
        imageStickerData.drawableId = R.drawable.ic_launcher_background

        stickerHolderView.addSticker(imageStickerData)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addTextSticker() {
        val textStickerData = TextStickerData()
        textStickerData.color = Color.BLUE
        textStickerData.text = "Example of \nsticker library \nfor text sticker" //"Text Sticker"
        textStickerData.textSize = 2.5f
//        val textStickerView = TextStickerView(this)
//        textStickerView.setData(textStickerData)
//
//        textStickerView.setOnTouchListener { view, motionEvent ->
//
//            when (motionEvent?.actionMasked) {
//
//                MotionEvent.ACTION_DOWN -> {
//                    onStickerTouchListener?.updateView(view)
//                }
//
//            }
//
//            false
//        }

        //binding?.clStickerHolder?.addView(textStickerView)
       stickerHolderView.addSticker(textStickerData)
    }

    private fun deleteSelectedSticker() {
        stickerHolderView.removeSelectedSticker()
        //stickerHolderView.controller.disableStickerMovement()
    }

    private fun showSavedImage() {

        val imageHolder = findViewById<ConstraintLayout>(R.id.cl_saved_image_holder)

        if (imageHolder.visibility == View.VISIBLE) {
            imageHolder.visibility = View.INVISIBLE
        } else {
            imageHolder.visibility = View.VISIBLE

            val imageView = findViewById<ImageView>(R.id.iv_saved_image)

            imageView.doOnPreDraw {
                val w = it.width
                Log.d("ImagePreview", "size: ($w, ${it.height})")
                Log.d("ImagePreview", "sizeCalc: (${(Size(w, getScaledHeight(Size(stickerHolderView.width, stickerHolderView.height), w)))})")
                imageView.setImageBitmap(stickerHolderView.getStickersImage(Size(w, getScaledHeight(Size(stickerHolderView.width, stickerHolderView.height), w))))
                //imageView.setImageBitmap(BitmapManager.decodeSampledBitmapFromResource(resources, R.drawable.ic_launcher_background, 200,200))
            }


        }

    }

    fun getScaledHeight(originalSize: Size, newWidth: Int): Int {
        val newHeight = (originalSize.height / originalSize.width.toFloat()) * newWidth

        return newHeight.toInt()
    }

    override fun onStickerSelected(view: View?) {
        super.onStickerSelected(view)

        Log.d("HomeScreenFragment", "onStickerSelected")
        //binding?.stickerHolderView?.setDeleteStickerIconVisibility(true)
    }

    override fun onStickerDeselected(view: View?) {
        super.onStickerDeselected(view)

        Log.d("HomeScreenFragment", "onStickerDeselected")
        //binding?.stickerHolderView?.setDeleteStickerIconVisibility(false)
    }

    override fun onStickerDeleted(view: View?) {
        super.onStickerDeleted(view)

        //binding?.stickerHolderView?.setDeleteStickerIconVisibility(false)
    }

    override fun onStickerDeleteActivated() {
        super.onStickerDeleteActivated()

        //binding?.stickerHolderView?.onDeletePressed()
    }

    override fun onStickerDeleteDeactivated() {
        super.onStickerDeleteDeactivated()

        //binding?.stickerHolderView?.onDeleteReleased()
    }

}