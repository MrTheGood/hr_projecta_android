package hh.corporation.urcompanion

import hh.corporation.urcompanion.State.Page
import processing.core.PApplet
import processing.event.TouchEvent

/**
 * Created by maartendegoede on 04/12/2018.
 */
class Sketch : PApplet() {
    private val state = State(State.Page.MAIN)

    override fun settings() {
        fullScreen()
    }

    override fun setup() {
    }

    override fun draw() {
        fill(255f, 255f, 255f)
        background(255f, 255f, 255f)

        if (state.page == Page.MAIN) {
            rect(50f, 20f, 200f, 200f, 7f)
            rect(300f, 20f, 200f, 200f, 7f)
            rect(550f, 20f, 200f, 200f, 7f)

            fill(0)
            textSize(100f)
            text(state.dice.x, 115f, 150f)
            text(state.dice.y, 370f, 150f)
            text(state.dice.z, 620f, 150f)

            if (state.dice.sum == 0 || state.dice.sum == 3 || state.dice.sum == 6) {
                textSize(45f)
                fill(0)
                text("You may add a pawn!", 175f, 360f)
            }
            textSize(45f)
            text("Move ${state.dice.sum} step(s)", 245f, 280f)
        }
    }

    override fun touchEnded(event: TouchEvent) {
        super.touchEnded(event)
        state.throwDice()
    }
}
