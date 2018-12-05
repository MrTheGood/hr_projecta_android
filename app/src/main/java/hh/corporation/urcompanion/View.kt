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
        var height: Float,
        var color: Int
) {
    fun collides(x: Float, y: Float) =
            x > this.x && x < this.x + width &&
                    y > this.y && y < this.y + height

    abstract fun draw(applet: PApplet)
}

class RectView(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        var radii: Float
) : View(x, y, width, height, color) {

    override fun draw(applet: PApplet): Unit = applet.run {
        fill(color)
        rect(x, y, this@RectView.width, this@RectView.height, radii)
    }

}

class TextView(
        x: Float,
        y: Float,
        color: Int,
        var text: String
) : View(x, y, 0f, 0f, color) {

    override fun draw(applet: PApplet) = applet.run {
        fill(color)
        text(text, x, y)
    }

}
