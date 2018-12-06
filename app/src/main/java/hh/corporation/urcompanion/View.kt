package hh.corporation.urcompanion

import processing.core.PApplet

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

class Button(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radii: Float,
        colorR: Float = 255f,
        colorG: Float = 255f,
        colorB: Float = 255f,
        var textColor: Int = 0,
        var onClick: () -> Unit = {}
) : RectView(x, y, width, height, radii, colorR, colorG, colorB) {
    var touching = false

    var text: String = ""
        set(value) {
            field = value
            textView = TextView(x + width / 2f, y + height / 1.5f, value, textColor)
        }

    private var textView = TextView(0f, 0f, "")

    override fun draw(applet: PApplet) {
        if (touching) applet.stroke(0)
        else applet.noStroke()

        applet.textSize(45f)
        super.draw(applet)
        textView.draw(applet)
        applet.stroke(0)
    }
}