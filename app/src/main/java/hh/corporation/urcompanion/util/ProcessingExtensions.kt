package hh.corporation.urcompanion.util

import android.graphics.BitmapFactory
import processing.core.PApplet
import processing.core.PImage
import java.io.IOException
import java.io.InputStream

/**
 * Created by maartendegoede on 05/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
fun PApplet.loadImageAsset(asset: String): PImage? {
    var stream: InputStream? = null

    try {
        stream = context.assets.open(asset)
        val bitmap = BitmapFactory.decodeStream(stream)
        return PImage(bitmap).apply { parent = this@loadImageAsset }
    } catch (e: IOException) {
        System.err.println("Could not load the image $asset.")
    } finally {
        stream?.close()
    }

    return null
}