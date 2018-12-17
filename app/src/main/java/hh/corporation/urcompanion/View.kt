package hh.corporation.urcompanion

import processing.core.PApplet
import processing.core.PImage

/**
 * Created by maartendegoede on 05/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
abstract class View(
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float
) {
    fun collides(x: Float, y: Float) =
            x > this.x && x < this.x + width &&
                    y > this.y && y < this.y + height

    abstract fun draw(applet: PApplet)
}

open class Button(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        var radii: Float = 8f,
        var colorR: Float = 216f,
        var colorG: Float = 27f,
        var colorB: Float = 96f,
        var textColor: Int = 255,
        var text: String = "",
        var onClick: () -> Unit = {}
) : View(x, y, width, height) {
    var touching = false

    override fun draw(applet: PApplet) = applet.run {
        if (touching) stroke(0)
        else noStroke()

        textSize(45f)
        fill(colorR, colorG, colorB)
        rect(x, y, this@Button.width, this@Button.height, radii)

        fill(textColor)
        text(text, x + this@Button.width / 2f, y + this@Button.height / 1.5f)
        stroke(0)
    }
}

open class IconButton(
        val image: PImage,
        x: Float,
        y: Float,
        width: Float = image.width.toFloat(),
        height: Float = image.height.toFloat(),
        onClick: () -> Unit = {}
) : Button(x, y, width, height, onClick = onClick) {

    init {
        image.resize(width.toInt(), height.toInt())
    }

    override fun draw(applet: PApplet) = applet.run {
        image(image, x, y)
    }
}

class CircleIconButton(
        image: PImage,
        val backgroundColorR: Float = 216f,
        val backgroundColorG: Float = 27f,
        val backgroundColorB: Float = 96f,
        x: Float,
        y: Float,
        width: Float = image.width.toFloat(),
        height: Float = image.height.toFloat(),
        val padding: Float,
        onClick: () -> Unit = {}
) : IconButton(image, x, y, width, height, onClick) {

    override fun draw(applet: PApplet) = applet.run {
        noStroke()
        fill(backgroundColorR, backgroundColorG, backgroundColorB)
        val w = this@CircleIconButton.width + padding
        val h = this@CircleIconButton.height + padding
        ellipse(x + w / 2, y + h / 2, w * 2, h * 2)

        super.draw(this)
    }
}