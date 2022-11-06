Sticker Library
=====

Sticker Library is open source library to help you easy and fast to add image and text sticker in your project.

How do I use Sticker Library?
-------------------

First create holder where you want to use stickers:

```xml
<com.example.stickerlibrary.custom_views.StickerHolderView
        android:id="@+id/sticker_holder_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

Example how to add image sticker:

```kotlin
// Create ImageStickerData object and set drawableId of your image for sticker
val imageStickerData = ImageStickerData()
imageStickerData.drawableId = R.drawable.ic_launcher_background

// Add ImageStickerData to StickerHolderView
stickerHolderView.addSticker(imageStickerData)
```

Example how to add text sticker:

```kotlin
// Create TextStickerData object
 val textStickerData = TextStickerData()

 // Set your text for sticker
 textStickerData.text = "Enter your text"

 // Set color of your text
 textStickerData.color = Color.BLUE
 
 // Set typeface for your text sticker
 textStickerData.typeface = Typeface.DEFAULT
 
 // Add TextStickerData to StickerHolderView
 stickerHolderView.addSticker(textStickerData)
```

Example how to remove selected sticker:

```kotlin
// Remove selected sticker
stickerHolderView.removeSelectedSticker()
```

StickerLibrary Listener:

```kotlin
// Set OnStickerTouchListener
stickerHolderView.setListener(this)

// When sticker is selected
override fun onStickerSelected(view: View?) {
    super.onStickerSelected(view)
}

// When sticker is deselected
override fun onStickerDeselected(view: View?) {
    super.onStickerDeselected(view)
}

// When sticker is deleted
override fun onStickerDeleted(view: View?) {
    super.onStickerDeleted(view)
}

// When sticker transformation are currently updating (touch still pressed)
override fun onTransformationRealTimeUpdate(view: View?) {
    super.onTransformationRealTimeUpdate(view)
}

// When sticker transformation are finished (touch is released)
override fun onTransformationUpdate(view: View?) {
    super.onTransformationUpdate(view)
}
```

You can add list of stickers:

```kotlin
// list of stickers data (ImageStickerData or TextStickerData)
val listOfStickers = mutableListOf<Any>()

val sticker1 = ImageStickerData()
val sticker2 = TextStickerData()
val sticker3 = ImageStickerData()
val sticker4 = TextStickerData()
val sticker5 = TextStickerData()

listOfStickers.add(sticker1)
listOfStickers.add(sticker2)
listOfStickers.add(sticker3)
listOfStickers.add(sticker4)
listOfStickers.add(sticker5)

// Add all stickers to sticker holder 
stickerHolderView.addAllStickers(listOfStickers, true)
// Second parametar is ClearPreviousStickers;; Set false if you want to keep previous stickers, and true if you want complete new stickers in holder (remove all current stickers from holder)
```

You can get bitmap of Sticker holder with all stickers:

```kotlin
// W - width of bitmap, h - height of bitmap ;; Size must keep aspect ratio of stickerHolderView
stickerHolderView.getStickersImage(Size(w, h))
```

# License
```
    Copyright 2022, Nemanja Mladenović (nemanjaMet)
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```


