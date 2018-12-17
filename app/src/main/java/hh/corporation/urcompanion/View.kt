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

open class RectView(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        var radii: Float,
        var colorR: Float = 255f,
        var colorG: Float = 255f,
        var colorB: Float = 255f
) : View(x, y, width, height) {

    override fun draw(applet: PApplet): Unit = applet.run {
        fill(colorR, colorG, colorB)
        rect(x, y, this@RectView.width, this@RectView.height, radii)
    }

}

class TextView(
        x: Float,
        y: Float,
        var text: String,
        var color: Int = 0
) : View(x, y, 0f, 0f) {

    override fun draw(applet: PApplet) = applet.run {
        fill(color)
        text(text, x, y)
    }

}

open class Button(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radii: Float = 8f,
        colorR: Float = 216f,
        colorG: Float = 27f,
        colorB: Float = 96f,
        var textColor: Int = 255,
        var onClick: () -> Unit = {}
) : RectView(x, y, width, height, radii, colorR, colorG, colorB) {
    var touching = false

    var text: String = ""
        set(value) {
            field = value
            textView = TextView(x + width / 2f, y + height / 1.5f, value, textColor)
        }

    private var textView = TextView(0f, 0f, "")

    override fun draw(applet: PApplet) = applet.run {
        if (touching) stroke(0)
        else noStroke()

        textSize(45f)
        super.draw(applet)
        textView.draw(applet)
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