package hh.corporation.urcompanion

import hh.corporation.urcompanion.State.Page
import hh.corporation.urcompanion.data.Cards
import hh.corporation.urcompanion.util.loadImageAsset
import hh.corporation.urcompanion.util.openPdf
import processing.core.PApplet
import processing.event.TouchEvent

/**
 * Created by maartendegoede on 04/12/2018.
 */
class Sketch(private val w: Int, private val h: Int) : PApplet() {
    private val state = State(State.Page.MAIN)
    private val background by lazy { loadImageAsset("app_background.png")?.apply { resize(g.width, g.height) } }
    private val logo by lazy { loadImageAsset("ur.png") }
    private val card by lazy { loadImageAsset("card_background.png") }
    private val appBarButton by lazy {
        IconButton(
                image = loadImageAsset("ic_book.png")!!,
                x = width - 104f,
                y = 92f,
                width = 72f,
                height = 72f,
                onClick = { openPdf("ur_manual.pdf") }
        )
    }

    private val mainPageButtons by lazy {
        listOf(
                Button(
                        x = width / 2f - 128f,
                        y = 672f,
                        width = 256f,
                        height = 96f,
                        onClick = {
                            state.drawCard(Cards.Type.PLUS)
                            state.page = Page.CARDS
                        }
                ).apply { text = "+ PLUS" },
                Button(
                        x = width / 2f - 128f,
                        y = 800f,
                        width = 256f,
                        height = 96f,
                        onClick = {
                            state.drawCard(Cards.Type.CIRCLE)
                            state.page = Page.CARDS
                        }
                ).apply { text = "O CIRCLE" },
                Button(
                        x = width / 2f - 144f,
                        y = height - 650f,
                        width = 288f,
                        height = 96f,
                        onClick = state::throwDice
                ).apply { text = "THROW DICE" }
        )
    }

    override fun settings() {
        size(w, h)
    }

    override fun setup() {
    }

    override fun draw() {
        fill(255f, 255f, 255f)
        background(background)

        image(logo, width / 2f - 88f, 56f, 176f, 160f)
        appBarButton.draw(this)
        if (state.page == Page.MAIN) {
            TextView(width / 2f, 640f, "Draw a card").draw(this)
            mainPageButtons.forEach { it.draw(this) }

            // region dice
            val diceSize = 200f
            val diceY = height - diceSize - 256f
            val dices = listOf(
                    RectView(50f, diceY, diceSize, diceSize, 16f),
                    TextView(50f + diceSize / 2, diceY + diceSize / 3 * 2, "${state.dice.x}"),

                    RectView(width / 2f - (diceSize / 2), diceY, diceSize, diceSize, 16f),
                    TextView(width / 2f, diceY + diceSize / 3 * 2, "${state.dice.y}"),

                    RectView(width - 50f - diceSize, diceY, diceSize, diceSize, 16f),
                    TextView(width - 50f - diceSize / 2, diceY + diceSize / 3 * 2, "${state.dice.z}")
            )

            textSize(100f)
            textAlign(CENTER)
            dices.forEach { it.draw(this) }

            textSize(45f)
            fill(0)
            if (state.dice.sum == 0 || state.dice.sum == 3 || state.dice.sum == 6)
                text("You may add a pawn!", width / 2f, height - 192f)
            text("Move ${state.dice.sum} step(s)", width / 2f, height - 128f)
            // endregion
        }

        if (state.page == Page.CARDS) {
            textAlign(CENTER)
            textSize(45f)
            fill(0)
            image(card, 64f, 224f, width - 128f, height - 480f)
            state.card.card.run {
                for (i in 0..length step 26)
                    text(substring(i, (i + 26).coerceAtMost(length)), width / 2f, height / 2f + i * 2)
            }
        }
    }

    override fun onBackPressed() {
        if (state.page == Page.CARDS) state.page = Page.MAIN
        else super.onBackPressed()
    }

    override fun touchStarted(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.apply { touching = collides(p.x, p.y) }
        if (state.page == Page.MAIN) {
            mainPageButtons.forEach {
                it.touching = it.collides(p.x, p.y)
            }
        }
    }

    override fun touchMoved(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.apply { touching = touching && collides(p.x, p.y) }
        if (state.page == Page.MAIN)
            mainPageButtons.forEach { it.touching = it.touching && it.collides(p.x, p.y) }
    }

    override fun touchEnded(event: TouchEvent) {
        appBarButton.apply {
            if (touching) onClick()
            touching = false
        }
        if (state.page == Page.MAIN) {
            mainPageButtons.forEach {
                if (it.touching) it.onClick()
                it.touching = false
            }
        }
    }
}
