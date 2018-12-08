package hh.corporation.urcompanion.util

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.content.FileProvider
import processing.core.PApplet
import processing.core.PImage
import java.io.File
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


// region files
/**
 * TODO: Catch ActivityNotFoundException
 */
fun PApplet.openPdf(pdf: String) {
    val uri = fileProviderUri(getPdfFile(pdf)!!)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(intent)
}


private fun PApplet.getPdfFile(pdf: String): File? {
    val file = File(context.filesDir, pdf)
    if (file.exists())
        return file

    try {
        val inputStream = context.assets.open(pdf)
        val outputStream = context.openFileOutput(file.name, Context.MODE_PRIVATE)
        inputStream.use { input ->
            outputStream.use { input.copyTo(it) }
        }
        return file
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}

fun PApplet.fileProviderUri(file: File): Uri =
        FileProvider.getUriForFile(
                context,
                "${context.applicationContext.packageName}.util.DefaultFileProvider",
                file
        )

class DefaultFileProvider : FileProvider()
// endregion files
