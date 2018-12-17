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

class Sketch(private val pw: Int, private val ph: Int) : PApplet() {
    /**
     * The next couple variables represent the state.
     * These include:
     * - The current page, either MAIN or CARD
     * - The current dice value
     * - The currently drawn card
     * - Whether the card is showing or not
     * - The type of the current card, either PLUS or CIRCLE
     */
    private var currentPage = PAGE_MAIN
    private var currentDice = listOf(0, 0, 0)

    private var currentCardType = CARD_PLUS
    private var currentCard = ""
    private var currentCardShown = false

    /**
     * These lists represent all possible cards.
     * They are of type [LinkedList], which is pretty much just an ordered list with some extra functionality
     */
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

    /**
     * The next couple variables are all necessary images.
     * You can ignore the "by lazy" for now. [loadImageAsset] loads the image from the disk.
     */
    private val background by lazy { loadImageAsset("app_background.png")?.apply { resize(g.width, g.height) } }
    private val logo by lazy { loadImageAsset("ur.png") }
    private val card by lazy { loadImageAsset("card_background.png") }
    private val circleCard by lazy { loadImageAsset("card_background_circle.png") }
    private val plusCard by lazy { loadImageAsset("card_background_plus.png") }

    /**
     * The next couple variables represent all buttons.
     * Each button is an instance of the [Button] class, which is nothing more than a variable which holds the coordinates and size of the buttons.
     *
     * More explanation on the buttons can be found in Buttons.kt
     */
    private val rulebookButton by lazy {
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
                onClick = { currentCardShown = true }
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
        // Sets the size to whatever the screen size is. Required because fullscreen() sucks.
        size(pw, ph)
    }

    /**
     * A simple function to more easily draw a dice.
     */
    private fun drawDice(
            x: Float,
            y: Float,
            size: Float,
            value: Int,
            radii: Float = 16f
    ) {
        stroke(0)
        textSize(100f)
        textAlign(CENTER)
        fill(255)
        rect(x, y, size, size, radii)
        fill(0)
        text(value, x + size / 2, y + size / 3 * 2)
    }

    override fun draw() {
        fill(255f, 255f, 255f)
        background(background)

        image(logo, width / 2f - 88f, 56f, 176f, 160f) // Draw the logo
        rulebookButton.draw(this) // Draw the rulebook button using the draw function defined in Buttons.kt

        when (currentPage) {
            PAGE_MAIN -> drawMainPage() // execute drawMainPage() if the current page is the main page
            PAGE_CARD -> drawCardsPage() // execute drawCardsPage() if the current page is the cards page
        }
    }

    /**
     * Draws the main page.
     */
    private fun drawMainPage() {
        fill(0)
        text("Draw a card", width / 2f, 640f)
        mainPageButtons.forEach { it.draw(this) } // Draws all buttons using the draw function defined in Buttons.kt

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

    /**
     * Draws the cards page
     */
    private fun drawCardsPage() {
        cardPageButtons.forEach { it.draw(this) } // Draws all buttons using the draw function defined in Buttons.kt

        textAlign(CENTER)
        textSize(45f)
        fill(0)
        image(card, 64f, 224f, width - 128f, height - 480f)
        text(currentCard, width / 2f, height / 2f)

        // Draw the back of the current card on top of the text if the card shouldn't be shown yet
        // when is just a fancy if-else_if statement.
        if (!currentCardShown) {
            when (currentCardType) {
                CARD_CIRCLE -> image(circleCard, 64f, 224f, width - 128f, height - 480f)
                CARD_PLUS -> image(plusCard, 64f, 224f, width - 128f, height - 480f)
            }
        }
    }

    /**
     * Go to the main page if the Android back button is pressed while on the cards page
     */
    override fun onBackPressed() {
        if (currentPage == PAGE_CARD) currentPage = PAGE_MAIN
        else super.onBackPressed()
    }

    /**
     * Passes the touchStarted event on to the visible buttons
     */
    override fun touchStarted(event: TouchEvent) {
        val p = event.getPointer(0)
        rulebookButton.touchStarted(p.x, p.y)

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchStarted(p.x, p.y) }
            PAGE_CARD -> cardPageButtons.forEach { it.touchStarted(p.x, p.y) }
        }
    }

    /**
     * Passes the touchMoved event on to the visible buttons
     */
    override fun touchMoved(event: TouchEvent) {
        val p = event.getPointer(0)
        rulebookButton.touchMoved(p.x, p.y)

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchMoved(p.x, p.y) }
            PAGE_CARD -> cardPageButtons.forEach { it.touchMoved(p.x, p.y) }
        }
    }

    /**
     * Passes the touchEnded event on to the visible buttons
     */
    override fun touchEnded(event: TouchEvent) {
        rulebookButton.touchEnded()

        when (currentPage) {
            PAGE_MAIN -> mainPageButtons.forEach { it.touchEnded() }
            PAGE_CARD -> cardPageButtons.forEach { it.touchEnded() }
        }
    }

    /**
     * Sets the currentDice list to a new list containing 3 random integers between 0..2
     */
    private fun throwDice() {
        currentDice = Random().run { listOf(nextInt(3), nextInt(3), nextInt(3)) }
    }

    /**
     * Sets currentCard to the card from top card of the specified list, and also sends the card to the bottom of the list.
     */
    private fun drawCard(cardType: String = currentCardType) {
        currentPage = PAGE_CARD

        currentCardType = cardType
        currentCardShown = false

        currentCard = when (cardType) {
            CARD_PLUS -> plusCards.pop().also { plusCards.add(it) } // Gets the top card of the plusCards list and also adds it to the bottom
            CARD_CIRCLE -> circleCards.pop().also { circleCards.add(it) } // Gets the top card of the circleCards list and also adds it to the bottom
            else -> throw RuntimeException("cardType $cardType not found") // Crashes the app when looking for a card type that doesn't exist.
        }
    }
}
