package hh.corporation.urcompanion

import hh.corporation.urcompanion.util.loadImageAsset
import hh.corporation.urcompanion.util.openPdf
import processing.core.PApplet
import processing.event.TouchEvent
import java.util.*

/**
 * Created by maartendegoede on 04/12/2018.
 */
const val PAGE_MAIN = "MAIN"
const val PAGE_CARD = "CARD"

const val CARD_PLUS = "PLUS"
const val CARD_CIRCLE = "CIRCLE"


class Sketch(private val w: Int, private val h: Int) : PApplet() {
    private var currentPage = PAGE_MAIN
    private var currentDice = listOf(0, 0, 0)

    private var currentCardType = CARD_PLUS
    private var currentCard = ""
    private var showingCard = false

    private val circleCards = LinkedList(listOf(
            "Throw off any opponent pawn",
            "Throw off any opponent pawn",
            "Throw off 2 opponent pawns",

            "Move a pawn from an opponent\nof choice 3 steps back",
            "Move a pawn from an opponent\nof choice 2 steps back",
            "Move a pawn from an opponent\nof choice 1 step back",
            "Move a pawn from an opponent\nof choice 1 step back",

            "Remove your pawn on the O tile",
            "Remove your pawn on the O tile",

            "Move a pawn from an opponent\nof choice 3 steps forward",
            "Move a pawn from an opponent\nof choice 2 steps forward",
            "Move a pawn from an opponent\nof choice 1 step forward",
            "Move a pawn from an opponent\nof choice 1 step forward",

            "Go to the other O tile.\nDon't draw a card",
            "Go to the other O tile.\nDon't draw a card",
            "Go to the other O tile.\nDon't draw a card",

            "Throw all pawns off the\nboard (including your own!)",
            "All opponent players may\nplace on pawn on the board",
            "Let an opponent player\ndraw a O card"
    ).shuffled())
    private val plusCards = LinkedList(listOf(
            "Add a new pawn on the board",
            "Add a new pawn on the board",
            "Add 2 new pawns on the board",

            "Move one of your pawns\n3 steps forward",
            "Move one of your pawns\n2 steps forward",
            "Move one of your pawns\n1 step forward",

            "Throw the dice again.",

            "Remove your pawn on\nthe + tile",
            "Remove one of your\nown pawns",

            "Move one of your pawns\n3 steps back",
            "Move one of your pawns\n2 steps back",
            "Move one of your pawns\n1 step back",
            "Move one of your pawns\n1 step back",

            "Go to the other + tile.\nDon't draw a card",
            "Go to the other + tile.\nDon't draw a card",
            "Go to the other + tile.\nDon't draw a card",

            "Switch one of your pawns\nwith one opponent pawn",
            "Move one of your pawns\nback to the start",
            "Let an opponent player\ndraw a + card"
    ).shuffled())

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
        listOf(IconButton(
                image = loadImageAsset("ic_back.png")!!,
                x = 32f,
                y = 100f,
                w = 72f,
                h = 72f,
                onClick = ::onBackPressed
        ), CircularIconButton(
                image = loadImageAsset("ic_view.png")!!,
                x = (width - 72f) / 2f,
                y = height - 148f,
                padding = 4f,
                w = 72f,
                h = 72f,
                onClick = { showingCard = true }
        ), IconButton(
                image = loadImageAsset("ic_next.png")!!,
                x = (width - 72f) / 1.3f,
                y = height - 132f,
                w = 72f,
                h = 72f,
                onClick = { drawCard() }
        ))
    }
    private val mainPageButtons by lazy {
        listOf(Button(
                x = width / 2f - 128f,
                y = 672f,
                w = 256f,
                h = 96f,
                text = "+ PLUS",
                onClick = { drawCard(CARD_PLUS) }
        ), Button(
                x = width / 2f - 128f,
                y = 800f,
                w = 256f,
                h = 96f,
                text = "O CIRCLE",
                onClick = { drawCard(CARD_CIRCLE) }
        ), Button(
                x = width / 2f - 144f,
                y = height - 650f,
                w = 288f,
                h = 96f,
                text = "THROW DICE",
                onClick = ::throwDice
        ))
    }

    override fun settings() {
        size(w, h)
    }

    private fun drawRect(
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

    private fun drawDice(
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

        when (currentPage) {
            PAGE_MAIN -> drawMainPage()
            PAGE_CARD -> drawCardsPage()
        }
    }

    private fun drawMainPage() {
        fill(0)
        text("Draw a card", width / 2f, 640f)
        mainPageButtons.forEach { it.draw(this) }

        // Draw the dice
        val diceSize = 200f
        val diceY = height - diceSize - 256f
        drawDice(50f, diceY, diceSize, currentDice[0])
        drawDice(width / 2f - diceSize / 2, diceY, diceSize, currentDice[1])
        drawDice(width - 50f - diceSize, diceY, diceSize, currentDice[2])

        textSize(45f)
        fill(0)
        val sum = currentDice.sum()
        if (sum == 0 || sum == 3 || sum == 6)
            text("You may add a pawn!", width / 2f, height - 192f)
        text("Move $sum step(s)", width / 2f, height - 128f)
    }

    private fun drawCardsPage() {
        cardPageButtons.forEach { it.draw(this) }

        textAlign(CENTER)
        textSize(45f)
        fill(0)
        image(card, 64f, 224f, width - 128f, height - 480f)
        text(currentCard, width / 2f, height / 2f)

        if (!showingCard) {
            when (currentCardType) {
                CARD_CIRCLE -> image(circleCard, 64f, 224f, width - 128f, height - 480f)
                CARD_PLUS -> image(plusCard, 64f, 224f, width - 128f, height - 480f)
            }
        }
    }

    override fun onBackPressed() {
        if (currentPage == PAGE_CARD) currentPage = PAGE_MAIN
        else super.onBackPressed()
    }

    override fun touchStarted(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.touchStarted(p.x, p.y)

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchStarted(p.x, p.y) }
            PAGE_CARD -> cardPageButtons.forEach { it.touchStarted(p.x, p.y) }
        }
    }

    override fun touchMoved(event: TouchEvent) {
        val p = event.getPointer(0)
        appBarButton.touchMoved(p.x, p.y)

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchMoved(p.x, p.y) }
            PAGE_CARD -> cardPageButtons.forEach { it.touchMoved(p.x, p.y) }
        }
    }

    override fun touchEnded(event: TouchEvent) {
        appBarButton.touchEnded()

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchEnded() }
            PAGE_CARD -> cardPageButtons.forEach { it.touchEnded() }
        }
    }

    private fun throwDice() {
        currentDice = Random().run { listOf(nextInt(3), nextInt(3), nextInt(3)) }
    }

    private fun drawCard(cardType: String = currentCardType) {
        when (cardType) {
            CARD_PLUS -> plusCards.pop().also { plusCards.add(it) }
            CARD_CIRCLE -> circleCards.pop().also { circleCards.add(it) }
        }
    }
}
