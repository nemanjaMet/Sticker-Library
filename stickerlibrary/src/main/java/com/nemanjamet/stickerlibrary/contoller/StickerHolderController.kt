package com.nemanjamet.stickerlibrary.contoller

import android.view.View

class StickerHolderController(private var stickerController: StickerController) {

    fun enableStickerMovement() {
        stickerController.getStickerTouchListener()?.enable()
    }

    fun disableStickerMovement() {
        stickerController.getStickerTouchListener()?.disable()
    }

    fun enableStickerScale() {
        stickerController.getStickerTouchListener()?.enableScale()
    }

    fun disableStickerScale() {
        stickerController.getStickerTouchListener()?.disableScale()
    }

    fun enableStickerRotation() {
        stickerController.getStickerTouchListener()?.enableRotation()
    }

    fun disableStickerRotation() {
        stickerController.getStickerTouchListener()?.disableRotation()
    }

    fun enableStickerTranslation() {
        stickerController.getStickerTouchListener()?.enableTranslation()
    }

    fun disableStickerTranslation() {
        stickerController.getStickerTouchListener()?.disableTranslation()
    }

    fun getSelectedSticker(): View? {
        return stickerController.getSelectedSticker()
    }

}