package hh.corporation.urcompanion

import processing.core.PApplet
import processing.core.PImage

/**
 * Created by maartendegoede on 05/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
open class Button(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float,
        var radii: Float = 8f,
        var colorR: Float = 216f,
        var colorG: Float = 27f,
        var colorB: Float = 96f,
        var textSize: Float = 45f,
        var textColor: Int = 255,
        var text: String = "",
        var strokeColor: Int = 0,
        var pressing: Boolean = false,
        var onClick: () -> Unit = {}
) {
    private fun collides(x: Float, y: Float) =
            x > this.x && x < this.x + w &&
                    y > this.y && y < this.y + h

    fun touchStarted(x: Float, y: Float) {
        pressing = collides(x, y)
    }

    fun touchMoved(x: Float, y: Float) {
        pressing = pressing && collides(x, y)
    }

    fun touchEnded() {
        if (pressing) onClick()
        pressing = false
    }

    open fun draw(applet: PApplet) = applet.run {
        if (pressing) stroke(strokeColor)
        else noStroke()

        textSize(textSize)
        fill(colorR, colorG, colorB)
        rect(x, y, w, h, radii)

        fill(textColor)
        text(text, x + w / 2f, y + h / 1.5f)
    }
}

open class IconButton(
        val image: PImage,
        x: Float,
        y: Float,
        w: Float = image.width.toFloat(),
        h: Float = image.height.toFloat(),
        onClick: () -> Unit = {}
) : Button(x, y, w, h, onClick = onClick) {

    init {
        image.resize(w.toInt(), h.toInt())
    }

    override fun draw(applet: PApplet) = applet.run {
        image(image, x, y)
    }
}

class CircularIconButton(
        image: PImage,
        val backgroundColorR: Float = 216f,
        val backgroundColorG: Float = 27f,
        val backgroundColorB: Float = 96f,
        x: Float,
        y: Float,
        w: Float = image.width.toFloat(),
        h: Float = image.height.toFloat(),
        val padding: Float,
        onClick: () -> Unit = {}
) : IconButton(image, x, y, w, h, onClick) {

    override fun draw(applet: PApplet) = applet.run {
        noStroke()
        fill(backgroundColorR, backgroundColorG, backgroundColorB)
        val w = w + padding
        val h = h + padding
        ellipse(x + w / 2, y + h / 2, w * 2, h * 2)

        super.draw(this)
    }
}