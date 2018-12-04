package eu.insertcode.urcompanion

import processing.core.PApplet
import java.util.*

/**
 * Created by maartendegoede on 04/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class Sketch : PApplet() {
    private val random = Random()

    override fun settings() {
        fullScreen()
    }

    override fun setup() {
        noStroke()
        fill(0)
    }

    override fun draw() {
        if (mousePressed) {
            fill(220f, 220f, 220f)
            noStroke()
            rect(175f, 230f, 500f, 300f)
            val a = random.nextInt(2)
            val b = random.nextInt(2)
            val c = random.nextInt(2)
            val diceSum = a + b + c

            fill(255f, 255f, 255f)
            rect(50f, 20f, 200f, 200f, 7f)
            rect(300f, 20f, 200f, 200f, 7f)
            rect(550f, 20f, 200f, 200f, 7f)

            if (diceSum == 0 || diceSum == 3 || diceSum == 6) {
                textSize(45f)
                fill(0)
                text("You may add a pawn!", 175f, 360f)
            }

            fill(0)
            textSize(100f)
            text(a, 115f, 150f)
            text(b, 370f, 150f)
            text(c, 620f, 150f)
            textSize(45f)
            text("Move $diceSum step(s)", 245f, 280f)
        }
    }
}
