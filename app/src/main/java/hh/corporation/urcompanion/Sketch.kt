package hh.corporation.urcompanion

import hh.corporation.urcompanion.State.Page
import hh.corporation.urcompanion.util.loadImageAsset
import processing.core.PApplet
import processing.event.TouchEvent

/**
 * Created by maartendegoede on 04/12/2018.
 */
class Sketch(private val w: Int, private val h: Int) : PApplet() {
    private val state = State(State.Page.MAIN)
    private val background by lazy { loadImageAsset("app_background.png") }

    override fun settings() {
        size(w, h)
    }

    override fun setup() {
    }

    override fun draw() {
        fill(255f, 255f, 255f)
        background(background)

        if (state.page == Page.MAIN) {
            val diceSize = 200f
            val diceY = height - diceSize - 256f
            val dices = listOf(
                    RectView(50f, diceY, diceSize, diceSize, 255, 16f),
                    TextView(50f + diceSize / 2, diceY + diceSize / 3 * 2, 0, "${state.dice.x}"),

                    RectView(width / 2f - (diceSize / 2), diceY, diceSize, diceSize, 255, 16f),
                    TextView(width / 2f, diceY + diceSize / 3 * 2, 0, "${state.dice.y}"),

                    RectView(width - 50f - diceSize, diceY, diceSize, diceSize, 255, 16f),
                    TextView(width - 50f - diceSize / 2, diceY + diceSize / 3 * 2, 0, "${state.dice.z}")
            )

            textSize(100f)
            textAlign(CENTER)
            for (dice in dices)
                dice.draw(this)


            textSize(45f)
            fill(0)
            if (state.dice.sum == 0 || state.dice.sum == 3 || state.dice.sum == 6)
                text("You may add a pawn!", width / 2f, height - 192f)
            text("Move ${state.dice.sum} step(s)", width / 2f, height - 128f)
        }
    }

    override fun touchEnded(event: TouchEvent) {
        super.touchEnded(event)
        state.throwDice()
    }
}
