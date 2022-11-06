package com.example.stickerlibrary.contoller

import android.view.View
import com.example.stickerlibrary.custom_components.OnStickerTouchListener

class StickerController : OnStickerTouchListener.OnStickerTouchListener {

    private var stickerTouchListener: OnStickerTouchListener? = null
    private var listener: OnStickerTouchListener.OnStickerTouchListener? = null

    fun getStickerTouchListener(): OnStickerTouchListener? {

        if (stickerTouchListener == null) {
            stickerTouchListener = OnStickerTouchListener(null, this)
        }

        return stickerTouchListener
    }

    fun setListener(listener: OnStickerTouchListener.OnStickerTouchListener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    override fun onStickerDeleteActivated() {
        super.onStickerDeleteActivated()

        listener?.onStickerDeleteActivated()
    }

    override fun onStickerDeleteDeactivated() {
        super.onStickerDeleteDeactivated()

        listener?.onStickerDeleteDeactivated()
    }

    override fun onStickerDeleted(view: View?) {
        super.onStickerDeleted(view)

        listener?.onStickerDeleted(view)
    }

    override fun onStickerDeselected(view: View?) {
        super.onStickerDeselected(view)

        listener?.onStickerDeselected(view)
    }

    override fun onStickerSelected(view: View?) {
        super.onStickerSelected(view)

        listener?.onStickerSelected(view)
    }

    fun getSelectedSticker(): View? {
        return stickerTouchListener?.selectedStickerView
    }

    fun isStickerSelected(): Boolean {
        return stickerTouchListener?.isStickerSelected() ?: false
    }

    fun isStickerPressed(): Boolean {
        return stickerTouchListener?.isStickerPressed() ?: false
    }

}