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
    private val circleCard by lazy { loadImageAsset("card_background_circle.png") }
    private val plusCard by lazy { loadImageAsset("card_background_plus.png") }
    private val appBarButton by lazy {
        IconButton(
                image = loadImageAsset("ic_book.png")!!,
                x = width - 104f,
                y = 92f,
                w = 72f,
                h = 72f,
                onClick = { openPdf("ur_manual.pdf") }
        )
    }

    private val cardPageButtons by lazy {
        listOf(
                IconButton(
                        image = loadImageAsset("ic_back.png")!!,
                        x = 32f,
                        y = 100f,
                        w = 72f,
                        h = 72f,
                        onClick = ::onBackPressed
                ),
                CircularIconButton(
                        image = loadImageAsset("ic_view.png")!!,
                        x = (width - 72f) / 2f,
                        y = height - 148f,
                        padding = 4f,
                        w = 72f,
                        h = 72f,
                        onClick = { state.card.showing = true }
                ),
                IconButton(
                        image = loadImageAsset("ic_next.png")!!,
                        x = (width - 72f) / 1.3f,
                        y = height - 132f,
                        w = 72f,
                        h = 72f,
                        onClick = { state.drawCard(state.card.type); state.card.showing = false }
                )
        )
    }
    private val mainPageButtons by lazy {
        listOf(
                Button(
                        x = width / 2f - 128f,
                        y = 672f,
                        w = 256f,
                        h = 96f,
                        text = "+ PLUS",
                        onClick = {
                            state.drawCard(Cards.Type.PLUS)
                            state.page = Page.CARDS
                        }
                ),
                Button(
                        x = width / 2f - 128f,
                        y = 800f,
                        w = 256f,
                        h = 96f,
                        text = "O CIRCLE",
                        onClick = {
                            state.drawCard(Cards.Type.CIRCLE)
                            state.page = Page.CARDS
                        }
                ),
                Button(
                        x = width / 2f - 144f,
                        y = height - 650f,
                        w = 288f,
                        h = 96f,
                        text = "THROW DICE",
                        onClick = state::throwDice
                )
        )
    }

    override fun settings() {
        size(w, h)
    }

    fun drawRect(
            x: Float,
            y: Float,
            width: Float,
            height: Float,
            radii: Float,
            colorR: Float = 255f,
            colorG: Float = 255f,
            colorB: Float = 255f
    ) {
        fill(colorR, colorG, colorB)
        rect(x, y, width, height, radii)
    }

    fun drawDice(
            x: Float,
            y: Float,
            size: Float,
            value: Int,
            radii: Float = 16f
    ) {
        textSize(100f)
        textAlign(CENTER)
        drawRect(x, y, size, size, radii)
        fill(0)
        text(value, x + size / 2, y + size / 3 * 2)
    }

    override fun draw() {
        fill(255f, 255f, 255f)
        background(background)

        image(logo, width / 2f - 88f, 56f, 176f, 160f)
        appBarButton.draw(this)
        if (state.page == Page.MAIN) {
            drawMainPage()
        }

        if (state.page == Page.CARDS) {
            drawCardsPage()
        }
    }

    fun drawMainPage() {
        fill(0)
        text("Draw a card", width / 2f, 640f)
        mainPageButtons.forEach { it.draw(this) }

        // Draw the dice
        val diceSize = 200f
        val diceY = height - diceSize - 256f
        drawDice(50f, diceY, diceSize, state.dice.x)
        drawDice(width / 2f - diceSize / 2, diceY, diceSize, state.dice.y)
        drawDice(width - 50f - diceSize, diceY, diceSize, state.dice.z)

        textSize(45f)
        fill(0)
        if (state.dice.sum == 0 || state.dice.sum == 3 || state.dice.sum == 6)
            text("You may add a pawn!", width / 2f, height - 192f)
        text("Move ${state.dice.sum} step(s)", width / 2f, height - 128f)
    }

    fun drawCardsPage() {
        cardPageButtons.forEach { it.draw(this) }

        textAlign(CENTER)
        textSize(45f)
        fill(0)
        image(card, 64f, 224f, width - 128f, height - 480f)
        text(state.card.card, width / 2f, height / 2f)

        if (!state.card.showing) {
            if (state.card.type == Cards.Type.CIRCLE)
                image(circleCard, 64f, 224f, width - 128f, height - 480f)
            else
                image(plusCard, 64f, 224f, width - 128f, height - 480f)
        }
    }

    override fun onBackPressed() {
        if (state.page == Page.CARDS) state.page = Page.MAIN
        else super.onBackPressed()
    }

    override fun touchStarted(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.touchStarted(p.x, p.y)

        when (state.page) {
            Page.MAIN -> mainPageButtons.forEach { it.touchStarted(p.x, p.y) }
            Page.CARDS -> cardPageButtons.forEach { it.touchStarted(p.x, p.y) }
        }
    }

    override fun touchMoved(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.touchMoved(p.x, p.y)

        when (state.page) {
            Page.MAIN -> mainPageButtons.forEach { it.touchMoved(p.x, p.y) }
            Page.CARDS -> cardPageButtons.forEach { it.touchMoved(p.x, p.y) }
        }
    }

    override fun touchEnded(event: TouchEvent) {
        appBarButton.touchEnded()

        when (state.page) {
            Page.MAIN -> mainPageButtons.forEach { it.touchEnded() }
            Page.CARDS -> cardPageButtons.forEach { it.touchEnded() }
        }
    }
}
