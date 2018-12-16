package hh.corporation.urcompanion.data

import java.util.*

/**
 * Created by maartendegoede on 11/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
object Cards {
    enum class Type { PLUS, CIRCLE }

    private val circleCards = LinkedList(arrayListOf(
            "Throw off any opponent pawn",
            "Throw off any opponent pawn",
            "Throw off 2 opponent pawns",

            "Move a pawn from an opponent of choice 3 steps back",
            "Move a pawn from an opponent of choice 2 steps back",
            "Move a pawn from an opponent of choice 1 step back",
            "Move a pawn from an opponent of choice 1 step back",

            "Remove your pawn on the O tile",
            "Remove your pawn on the O tile",

            "Move a pawn from an opponent of choice 3 steps forward",
            "Move a pawn from an opponent of choice 2 steps forward",
            "Move a pawn from an opponent of choice 1 step forward",
            "Move a pawn from an opponent of choice 1 step forward",

            "Go to the other O tile. Don't draw a card",
            "Go to the other O tile. Don't draw a card",
            "Go to the other O tile. Don't draw a card",

            "Throw all pawns off the board (including your own!)",
            "All opponent players may place on pawn on the board",
            "Let an opponent player draw a O card"
    ).shuffled())

    private val plusCards = LinkedList(arrayListOf(
            "Add a new pawn on the board",
            "Add a new pawn on the board",
            "Add 2 new pawns on the board",

            "Move one of your pawns 3 steps forward",
            "Move one of your pawns 2 steps forward",
            "Move one of your pawns 1 step forward",

            "Throw the dice again.",

            "Remove your pawn on the + tile",
            "Remove one of your own pawns",

            "Move one of your pawns 3 steps back",
            "Move one of your pawns 2 steps back",
            "Move one of your pawns 1 step back",
            "Move one of your pawns 1 step back",

            "Go to the other + tile. Don't draw a card",
            "Go to the other + tile. Don't draw a card",
            "Go to the other + tile. Don't draw a card",

            "Switch one of your pawns with one opponent pawn",
            "Move one of your pawns back to the start",
            "Let an opponent player draw a + card"
    ).shuffled())

    /**
     * Gets the first card of the cards stack of type [type], returns it and moves it to the bottom of the list.
     */
    fun obtainCard(type: Type): String =
            when (type) {
                Type.PLUS -> plusCards.pop().also { plusCards.add(it) }
                Type.CIRCLE -> circleCards.pop().also { circleCards.add(it) }
            }
}
