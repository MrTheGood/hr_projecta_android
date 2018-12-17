@file:Suppress("MemberVisibilityCanBePrivate")

package hh.corporation.urcompanion

import processing.core.PApplet
import processing.core.PImage

/**
 * Created by maartendegoede on 05/12/2018.
 */

/**
 * This class represents a button.
 *
 * I really tried to remove as much classes as possible, but I couldn't get rid of these without making the code more complicated.
 *
 * A class isn't actually that hard.
 * It is a blueprint for a variable with it's own internal state and it's own internal functions.
 * Kind of like a set {"variable":value,"variable2":anotherValue,"anotherVariable":yetAnotherValue}, except it also has functions.
 *
 *
 * Anyway, this class represents a single button.
 */
open class Button(
        var x: Float, // x coordinate of the button
        var y: Float, // y coordinate of the button
        var w: Float, // button width
        var h: Float, // button height
        var radii: Float = 8f, // Corner radii for the button background rect
        var colorR: Float = 216f, // background color values for the button background rect, default is the pink-ish accent colour
        var colorG: Float = 27f,
        var colorB: Float = 96f,
        var textSize: Float = 45f, // Size for the included text, default set to 45
        var textColor: Int = 255, // Color for the included text, default set to white
        var text: String = "", // The text value for the included text (duh)
        var strokeColor: Int = 0, // The colour of the stroke when the button is pressed
        var pressing: Boolean = false, // True when the button is being hold down, false if not
        var onClick: () -> Unit = {} // A lambda function, executed when the button is released
) {
    // Returns true if the touchX or touchY are within the button
    private fun isTouching(touchX: Float, touchY: Float) =
            touchX > x && touchX < x + w &&
                    touchY > y && touchY < y + h

    /**
     * A touch has started.
     * Pressing is set to true if the button is touched.
     */
    fun touchStarted(x: Float, y: Float) {
        pressing = isTouching(x, y)
    }

    /**
     * The touch position has moved.
     * Pressing is set to false if the touch has stopped touching the button.
     */
    fun touchMoved(x: Float, y: Float) {
        pressing = pressing && isTouching(x, y)
    }

    /**
     * The touch has ended.
     * Executes the onClick only if the button was still pressed.
     */
    fun touchEnded() {
        if (pressing) onClick()
        pressing = false
    }

    /**
     * Draws a single button.
     */
    open fun draw(applet: PApplet) = applet.run {
        if (pressing) {
            // Draws a stroke and a slightly lighter button colour if the button is pressed
            stroke(strokeColor)
            fill(colorR + 20, colorG + 20, colorB + 20)
        } else {
            noStroke()
            fill(colorR, colorG, colorB)
        }

        // Draw the background rectangle
        rect(x, y, w, h, radii)

        // Draw the button text
        fill(textColor)
        textSize(textSize)
        text(text, x + w / 2f, y + h / 1.5f)
    }
}

/**
 * This class represents a button that is only an icon
 *
 * It 'inherits' all functions from the Button class (see above).
 * But it overrides the draw function, so that the function in this class is executed instead of the one in the Button class.
 * This because the functionality is the same. The only difference is in the appearance.
 */
open class IconButton(
        val image: PImage,
        x: Float,
        y: Float,
        w: Float = image.width.toFloat(),
        h: Float = image.height.toFloat(),
        padding: Float = 0f,
        onClick: () -> Unit = {}
) : Button(x, y, (w + padding) * 2, (h + padding) * 2, onClick = onClick) {

    init {
        image.resize(w.toInt(), h.toInt())
    }

    override fun draw(applet: PApplet) = applet.run {
        if (pressing) {
            // Draws a slightly transparent circular background if the button is pressed
            noStroke()
            fill(0f, 80f)
            ellipse(x + w / 4, y + h / 4, w, h)
        }
        image(image, x, y)
    }
}

/**
 * This class represents a button with an icon and a circular background.
 *
 * It 'inherits' all functions from the Button class (see above).
 * But it overrides the draw function, so that the function in this class is executed instead of the one in the Button class.
 * This because the functionality is the same. The only difference is in the appearance.
 */
class CircularIconButton(
        val image: PImage,
        x: Float,
        y: Float,
        w: Float = image.width.toFloat(),
        h: Float = image.height.toFloat(),
        colorR: Float = 216f,
        colorG: Float = 27f,
        colorB: Float = 96f,
        val padding: Float,
        onClick: () -> Unit = {}
) : Button(x, y, (w + padding) * 2, (h + padding) * 2, colorR = colorR, colorG = colorG, colorB = colorB, onClick = onClick) {

    init {
        image.resize(w.toInt(), h.toInt())
    }

    override fun draw(applet: PApplet) = applet.run {
        if (pressing) {
            // Draws a stroke and a slightly lighter button colour if the button is pressed
            stroke(strokeColor)
            fill(colorR + 20, colorG + 20, colorB + 20)
        } else {
            noStroke()
            fill(colorR, colorG, colorB)
        }

        ellipse(x + w / 4, y + h / 4, w, h)

        image(image, x, y)
    }
}