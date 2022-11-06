package com.nemanjamet.stickerlibrary.custom_components

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.nemanjamet.stickerlibrary.custom_views.ImageStickerView
import com.nemanjamet.stickerlibrary.custom_views.TextStickerView
import com.nemanjamet.stickerlibrary.helpers.StickerHelperManager
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class OnStickerTouchListener(var selectedStickerView: View?, var listener: OnStickerTouchListener? = null): View.OnTouchListener {

    companion object {
        private const val TAG = "OnStickerTouchListener"

        private const val LONG_PRESS_SELECT_STICKER = 100L //250L
    }

    interface OnStickerTouchListener {
        fun onTransformationUpdate(view: View?) {}
        fun onTransformationRealTimeUpdate(view: View?) {}
        fun onStickerSelected(view: View?) {}
        fun onStickerDeselected(view: View?) {}
        fun onStickerDeleted(view: View?) {}
        fun onStickerDeleteActivated() {}
        fun onStickerDeleteDeactivated() {}
    }

    private var moveGestureDetector = MoveGestureDetector()
    private var scaleGestureDetector = ScaleGestureDetector()
    private var rotateGestureDetector = RotateGestureDetector()
    private var isStickerPressed = false
    private var isStickerSelected = false
    private var isTouchEnabled = true
    private var startedTouchTime = 0L
    private var isCheckingClick = false
    private var isDeselectStickerEnabled = true
    //private var isStickerSelected = false
    private var deleteRectArea = RectF()
    private var parentSize = Size(0,0)
    //private var listener: OnStickerTransformationListener? = null
    private val handler = Handler(Looper.getMainLooper())
    private var clickedStickerView: View? = null
    private val longPressed = Runnable {

        if (!isStickerPressed) {
            isCheckingClick = false
            return@Runnable
        }


        deselectSticker()

        selectedStickerView = clickedStickerView
        //clickedStickerView = null
        selectedStickerView?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

        selectSticker()

        moveGestureDetector.resetStartValues()
        scaleGestureDetector.resetStartValues()
        rotateGestureDetector.resetStartValues()

        isCheckingClick = false
    }

    fun updateView(view: View?, isAutoSelectStickerEnabled: Boolean = false) {
        Log.d("TestPress", "updateView -> isStickerPressed: $isStickerPressed")
        if (!isTouchEnabled())
            return

        if (isAutoSelectStickerEnabled) {
            deselectSticker()
            selectedStickerView = view
            selectSticker()
        } else {
            //deselectSticker()

            clickedStickerView = view

            // selectSticker()
        }

    }

    private fun deselectView() {

    }

    private fun isClick(): Boolean {
        val touchDuration = System.currentTimeMillis() - startedTouchTime

        return touchDuration < 100
    }

    private fun isStickerLongPress(): Boolean {
        val touchDuration = System.currentTimeMillis() - startedTouchTime

        return touchDuration > LONG_PRESS_SELECT_STICKER
    }

    private fun removeSticker() {

    }

    fun isStickerSelected(): Boolean {
        if (selectedStickerView == null)
            isStickerSelected = false

        return isStickerSelected
    }

    private fun deselectSticker() {

        if (!isDeselectStickerEnabled)
            return

        when (selectedStickerView) {
            is TextStickerView -> {
                val textStickerView = (selectedStickerView as TextStickerView)
                textStickerView.onStickerDeselected()
                selectedStickerView = null
                listener?.onStickerDeselected(textStickerView)
            }

            is ImageStickerView -> {
                val imageStickerView = (selectedStickerView as ImageStickerView)
                imageStickerView.onStickerDeselected()
                selectedStickerView = null
                listener?.onStickerDeselected(imageStickerView)
            }
        }

        isStickerSelected = false

    }

    private fun selectSticker() {

        when (selectedStickerView) {
            is TextStickerView -> {
                val textStickerView = (selectedStickerView as TextStickerView)
                textStickerView.onStickerSelected()
                listener?.onStickerSelected(textStickerView)
                isStickerSelected = true
            }

            is ImageStickerView -> {
                val imageStickerView = (selectedStickerView as ImageStickerView)
                imageStickerView.onStickerSelected()
                listener?.onStickerSelected(imageStickerView)
                isStickerSelected = true
            }
        }


    }

    fun deleteSelectedSticker() {

        (selectedStickerView as? ImageStickerView?)?.apply {
            onStickerDelete()
            listener?.onStickerDeleted(this)
        }

        (selectedStickerView as? TextStickerView?)?.apply {
            onStickerDelete()
            listener?.onStickerDeleted(this)
        }

        deselectSticker()

    }

    fun isStickerPressed(): Boolean {
        return isStickerPressed
    }

    override fun onTouch(parentHolder: View?, motionEvent: MotionEvent?): Boolean {

        if (!isTouchEnabled())
            return false

        if (parentSize.width == 0 && (parentHolder?.width ?: 0) != 0) {
            parentSize = Size(parentHolder?.width ?: 0, parentHolder?.height ?: 0)
            //val deleteAreaSize = (parentSize.width * 0.1f) / 2f
            val deleteAreaSize = (parentSize.width * StickerHelperManager.DELETE_STICKER_ICON_SIZE) / 2f
            val centerPointDeleteArea = PointF(parentSize.width * 0.5f, parentSize.height * 0.85f)
            deleteRectArea = RectF(centerPointDeleteArea.x - deleteAreaSize, centerPointDeleteArea.y - deleteAreaSize, centerPointDeleteArea.x + deleteAreaSize,centerPointDeleteArea.y + deleteAreaSize )
        }

        when (motionEvent?.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                isStickerPressed = true
                Log.d(TAG, "PointerCount: ${motionEvent.pointerCount}")
                //Log.d(TAG, "ACTION_DOWN PointerCount: ${motionEvent.pointerCount}")

                startedTouchTime = System.currentTimeMillis()

                val isStickersEqual = clickedStickerView == null || clickedStickerView == selectedStickerView //clickedStickerView != null && clickedStickerView == selectedStickerView
                Log.d("StickerCheck", "ACTION_DOWN -> isStickersEqual = ${clickedStickerView != null}, ${clickedStickerView == selectedStickerView}")

                if (!isStickersEqual) { //!isStickerSelected()
                    isCheckingClick = true

                    handler.postDelayed(longPressed, LONG_PRESS_SELECT_STICKER)
                    //longPressed.run()
                }

               // handler.postDelayed(longPressed, LONG_PRESS_SELECT_STICKER)

               // longPressed.run()

                //Log.d("TestPress", "ACTION_DOWN -> isStickerPressed: $isStickerPressed")

            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                //Log.d(TAG, "ACTION_POINTER_DOWN PointerCount: ${motionEvent.pointerCount}")

            }

            MotionEvent.ACTION_POINTER_UP -> {
                //Log.d(TAG, "ACTION_POINTER_UP PointerCount: ${motionEvent.pointerCount}")

                Log.d("TestPress", "ACTION_POINTER_UP -> isStickerPressed: $isStickerPressed")
                if (motionEvent.pointerCount == 1) {
                    isStickerPressed = false
                    isCheckingClick = false
                    Log.d("TestPress", "ACTION_POINTER_UP -> isStickerPressed: $isStickerPressed")
                }

            }

            MotionEvent.ACTION_UP -> {
                //Log.d(TAG, "ACTION_UP PointerCount: ${motionEvent.pointerCount}")
                    isStickerPressed = false

                // TODO DELETE STICKER
             /*   if (isDelete(PointF(motionEvent.x, motionEvent.y)) && isStickerClicked(PointF(motionEvent.x, motionEvent.y))) {
                    Log.d("isDeleteClicked", "delete sticker clicked")
                    val textStickerView = (stickerView as? TextStickerView?)
                    textStickerView?.onStickerDelete()
                    listener?.onStickerDeleted(textStickerView)
                } else {
                    //Log.d("isDeleteClicked", "delete sticker not clicked")
                    //deselectSticker()
                }*/

                if (isClick()) {
                    //Log.d("isStickerClicked", "click")
//                    if (isDeleteClicked(PointF(motionEvent.x, motionEvent.y))) { // isDeleteClicked(PointF(motionEvent.x, motionEvent.y))
//                        Log.d("isDeleteClicked", "delete sticker clicked")
//                        (stickerView as? TextStickerView?)?.onStickerDelete()
//                    } else {
//                        //Log.d("isDeleteClicked", "delete sticker not clicked")
//                        //deselectSticker()
//                    }

                    deselectSticker()
                    if (isStickerClicked(PointF(motionEvent.x, motionEvent.y))) {
                        Log.d("isStickerClicked", "sticker clicked")

                    } else {
                        Log.d("isStickerClicked", "sticker not clicked")
                        //deselectSticker()
                    }



                } else {
                    Log.d("isStickerClicked", "not click")
                }

                if (!isStickerSelected()) {
                    handler.removeCallbacks(longPressed)
                    clickedStickerView = null
                }

                isCheckingClick = false

            }

            MotionEvent.ACTION_CANCEL -> {
//                if (!isStickerSelected()) {
//                    handler.removeCallbacks(longPressed)
//                    clickedStickerView = null
//                }
            }

            MotionEvent.ACTION_MOVE -> {
//                if (!isTouchEnabled && isClick()) {
//                    isTouchEnabled = true
//                }

//                val isDelete = isDelete(PointF(motionEvent.x, motionEvent.y))
//                val isStickerClicked = isStickerClicked(PointF(motionEvent.x, motionEvent.y))
//                isStickerClickedTest(PointF(motionEvent.x, motionEvent.y))
//                Log.d("isDeleteClicked", "MOVE -> isDelete: $isDelete, isSticker2Clicked: $isStickerClicked")
//                if (isDelete(PointF(motionEvent.x, motionEvent.y)) && isStickerClicked(PointF(motionEvent.x, motionEvent.y))) {
//                    Log.d("isDeleteClicked", "DELETE AREA")
//                    stickerView?.alpha = 0.3f
//                    listener?.onStickerDeleteActivated()
//                } else {
//                    stickerView?.alpha = 1.0f
//                    listener?.onStickerDeleteDeactivated()
//                }
            }

        }

        if (!isCheckingClick) {
            if (isStickerPressed && isStickerSelected()) {
                moveGestureDetector.onTouch(selectedStickerView, motionEvent)
                scaleGestureDetector.onTouch(selectedStickerView, motionEvent)
                rotateGestureDetector.onTouch(selectedStickerView, motionEvent)
            } else if (isStickerPressed && !isStickerSelected()) {
                moveGestureDetector.resetStartValues()
                scaleGestureDetector.resetStartValues()
                rotateGestureDetector.resetStartValues()
            }
        }


        // update transformation
        if (motionEvent?.actionMasked == MotionEvent.ACTION_UP ) //&& motionEvent.pointerCount == 0
            listener?.onTransformationUpdate(selectedStickerView)
        else if (motionEvent?.actionMasked == MotionEvent.ACTION_MOVE ) {
            listener?.onTransformationRealTimeUpdate(selectedStickerView)

            // TODO: DELETE ACTIVATED
           /* if (isDelete(PointF(motionEvent.x, motionEvent.y)) && isStickerClicked(PointF(motionEvent.x, motionEvent.y))) { // isDelete(PointF(motionEvent.x, motionEvent.y)) && isStickerClicked(PointF(motionEvent.x, motionEvent.y))
                Log.d("isDeleteClicked", "DELETE AREA")
                stickerView?.alpha = 0.3f
                listener?.onStickerDeleteActivated()
            } else {
                stickerView?.alpha = 1.0f
                listener?.onStickerDeleteDeactivated()
            }*/
        }

        //view?.performClick()

        return true
    }

    private fun isDelete(pointClick: PointF) : Boolean {

        Log.d("isDeleteArea", "point: $pointClick")
        Log.d("isDeleteArea", "deleteRectArea: $deleteRectArea")
        return deleteRectArea.contains(pointClick.x, pointClick.y)

    }

    private fun isStickerClickedTest(pointClick: PointF) {
        selectedStickerView?.let { s ->

            val rotation = s.rotation % 360

            //val centerPoint = PointF(s.x, s.y)
            RectF(0f,0f,0f,0f)
            val startX = s.x + (s.width - s.width * s.scaleX) / 2 //- (s.width * s.scaleX) / 2f
            val startY = s.y + (s.height - s.height * s.scaleY) / 2  //- (s.height * s.scaleY) / 2f
            val stickerRect = RectF(startX, startY, startX + s.width * s.scaleX, startY + s.height * s.scaleY)
//            val rotatedRect = RectF()
//            val matrix = Matrix()
//            matrix.setRotate(rotation, pointClick.x, pointClick.y)
//            matrix.mapRect(rotatedRect)
//
//            val rotatedPoint = PointF(rotatedRect.left, rotatedRect.top)
            val rotPointOld = getRotatedPointByMatrix(pointClick, - rotation, s.width / 2f, s.height / 2f)
            val rotPoint = pointClick.rotate(rotation)
            val p1 = PointF(stickerRect.left, stickerRect.top).rotate(rotation)
//            val p2 = PointF(stickerRect.right, stickerRect.top).rotate(s.rotation)
//            val p3 = PointF(stickerRect.left, stickerRect.bottom).rotate(s.rotation)
            val p4 = PointF(stickerRect.right, stickerRect.bottom).rotate(rotation)
            val newRect = RectF(p1.x, p1.y, p4.x, p4.y)

            val isClicked = stickerRect.contains(rotPoint.x, rotPoint.y)
            //val isClicked = stickerRect.containsPoint(rotPoint)


            Log.d("isStickerClickedTest", "---- isClicked: $isClicked, rotation: $rotation, rotPointOld: $rotPointOld ----")
            Log.d("isStickerClickedTest", "pointClick: $pointClick , rotatedPointClick: $rotPoint")
            Log.d("isStickerClickedTest", "stickerRect: $stickerRect, stickerRotatedRect: $newRect")

        }
    }

    private fun isStickerClicked(pointClick: PointF): Boolean {

        selectedStickerView?.let { s ->

            val sWidth = s.width
            val sHeight = s.height

            val scaledWidth = sWidth * s.scaleX
            val scaledHeight = sHeight * s.scaleY

            val scaleDiffW = sWidth - scaledWidth
            val scaleDiffH = sHeight - scaledHeight

            val pointRect = PointF(s.x, s.y)
            val pointToRot = PointF(pointClick.x - pointRect.x - scaleDiffW / 2f, pointClick.y - pointRect.y - scaleDiffH / 2f)

//            val paddingW = s.scaleX * paddingForClick
//            val paddingY = s.scaleY * paddingForClick


            val rotPoint = getRotatedPointByMatrix(pointToRot, - s.rotation, scaledWidth / 2f, scaledHeight / 2f)
            val startRect = RectF(0f,0f, scaledWidth, scaledHeight)

            //Log.d("isStickerClicked", "\nstartRect: $startRect \nrotPoint: $rotPoint")

            return startRect.contains(rotPoint.x, rotPoint.y)
        } ?: kotlin.run {
            return false
        }

    }

    private fun isDeleteActivated() : Boolean {
        selectedStickerView?.let { s ->

            val sWidth = s.width
            val sHeight = s.height

            val scaledWidth = sWidth * s.scaleX
            val scaledHeight = sHeight * s.scaleY

            val scaleDiffW = sWidth - scaledWidth
            val scaleDiffH = sHeight - scaledHeight

            //val pointRect = PointF(s.x, s.y)
            //val pointToRot = PointF(pointClick.x - pointRect.x - scaleDiffW / 2f, pointClick.y - pointRect.y - scaleDiffH / 2f)

//            val paddingW = s.scaleX * paddingForClick
//            val paddingY = s.scaleY * paddingForClick

            //val startRect = RectF(0f,0f, scaledWidth, scaledHeight)
            val startX = s.x + scaleDiffW / 2f
            val startY = s.y + scaleDiffH / 2f
            val startRect = RectF(startX, startY , startX + scaledWidth, startY + scaledHeight)

            val deleteRect = RectF(deleteRectArea)
            //deleteRect.set(deleteRectArea)
//            deleteRect.right += scaleDiffW / 2f
//            deleteRect.bottom += scaleDiffH / 2f
//            deleteRect.left += scaleDiffW / 2f
//            deleteRect.top += scaleDiffH / 2f

            Log.d("isDeleteActivated", "-----ROT: ${s.rotation}-----")
            Log.d("isDeleteActivated", "startRect: $startRect")
            val matrix = Matrix()
            matrix.setRotate(- s.rotation, scaledWidth / 2f, scaledHeight / 2f)
            matrix.mapRect(startRect)

            //val deleteAreaSize = (parentSize.width * 0.1f) / 2f
            //val deleteAreaSize = (parentSize.width * StickerHelperManager.DELETE_STICKER_ICON_SIZE) // 2f
            val matrix2 = Matrix()
            matrix2.setRotate(- s.rotation,  scaledWidth / 2f, scaledWidth / 2f)
            matrix2.mapRect(deleteRect)


            Log.d("isDeleteActivated", "startRectRotated: $startRect")
            Log.d("isDeleteActivated", "deleteRectArea: $deleteRectArea")
            Log.d("isDeleteActivated", "deleteRectRotated: $deleteRect")

            //val rotPoint = getRotatedPointByMatrix(pointToRot, - s.rotation, scaledWidth / 2f, scaledHeight / 2f)


            //Log.d("isStickerClicked", "\nstartRect: $startRect \nrotPoint: $rotPoint")


            return startRect.intersect(deleteRect)
        } ?: kotlin.run {
            return false
        }
    }

    private fun isDeleteClicked(pointClick: PointF): Boolean {
        selectedStickerView?.let { s ->

            Log.d("isDeleteClickedFunc", "pointClick: $pointClick")

            val deleteBtnSize = StickerHelperManager.getScreenWidth(s.context) * 0.07f * s.scaleX

            val sWidth = s.width
            val sHeight = s.height

            val scaleDiffW = sWidth - sWidth * s.scaleX
            val scaleDiffH = sHeight - sHeight * s.scaleY

            val pointRect = PointF(s.x, s.y)
            val pointToRot = PointF(pointClick.x - pointRect.x - scaleDiffW / 2f, pointClick.y - pointRect.y - scaleDiffH / 2f)

//            val paddingW = s.scaleX * paddingForClick
//            val paddingY = s.scaleY * paddingForClick

            val rotPoint = getRotatedPointByMatrix(pointToRot, - s.rotation, sWidth / 2f, sHeight / 2f)
            //val startRect = RectF(0f,0f, sWidth.toFloat(), sHeight.toFloat())
            val startRect = RectF(- deleteBtnSize / 2f,- deleteBtnSize / 2f,  deleteBtnSize / 2f, deleteBtnSize / 2f)

            //Log.d("isDeleteClicked", "startRect: $startRect \nrotPoint: $rotPoint,  \npointClick: $pointClick")
            Log.d("isDeleteClickedFunc", "Start rect: $startRect")
            Log.d("isDeleteClickedFunc", "rotPoint: ${PointF(rotPoint.x, rotPoint.y)}, angle: $")
            Log.d("isDeleteClickedFunc", "isStickerDeleted: ${startRect.contains(rotPoint.x, rotPoint.y)}")

            return startRect.contains(rotPoint.x, rotPoint.y)
        } ?: kotlin.run {
            return false
        }
    }

    private fun PointF.rotate(degree: Float) : PointF {

        Log.d("rotatePoint", "beforeRotation: angle: $degree, x: $x, y: $y")

        val radian = degree * (PI / 180)

        val rotX = x * cos(radian) + y * sin(radian)
        val rotY = - x * sin(radian) + y * cos(radian)

        Log.d("rotatePoint", "afterRotation: angle: $degree, x: $rotX, y: $rotY")

        return PointF(rotX.toFloat(), rotY.toFloat())
    }

    private fun RectF.containsPoint(point: PointF) : Boolean {
        val x = point.x
        val y = point.y

        return ((x >= left) && (x <= right)) && ((y >= bottom) && (y <= top))
    }

    private fun getRotatedPointByMatrix(point: PointF, angle: Float, pivotX: Float = 0f, pivotY: Float = 0f) : PointF {
        val floatArray = FloatArray(2)
        floatArray[0] = point.x
        floatArray[1] = point.y

        val matrix = Matrix()
        matrix.postRotate(angle, pivotX, pivotY)
        matrix.mapPoints(floatArray)

        point.x = floatArray[0]
        point.y = floatArray[1]

        return point
    }

    private class ButtonClickDetector {

        fun onTouch(view: View?, motionEvent: MotionEvent?) {

        }

    }

    fun enable() {
        isTouchEnabled = true
    }

    fun disable() {
        isTouchEnabled = false
        deselectSticker()
    }

    fun isTouchEnabled(): Boolean {
        return isTouchEnabled
    }

    fun enableTranslation() {
        moveGestureDetector.enable()
    }

    fun disableTranslation() {
        moveGestureDetector.disable()
    }

    fun enableScale() {
        scaleGestureDetector.enable()
    }

    fun disableScale() {
        scaleGestureDetector.disable()
    }

    fun enableRotation() {
        rotateGestureDetector.enable()
    }

    fun disableRotation() {
        rotateGestureDetector.disable()
    }

    fun setMinMaxScale(minScale: Float, maxScale: Float) {
        scaleGestureDetector.setMinMaxScale(minScale, maxScale)
    }

    fun setIsStickerSelected(isSelected: Boolean) {
        this.isStickerSelected = isSelected
    }

    fun setIsDeselectStickerEnabled(isEnabled: Boolean) {
        this.isDeselectStickerEnabled = isEnabled
    }

}