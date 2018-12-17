package hh.corporation.urcompanion

import hh.corporation.urcompanion.data.Cards
import hh.corporation.urcompanion.data.Cards.Type
import java.util.*

/**
 * Created by maartendegoede on 04/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
data class State(
        var page: Page,
        var dice: Dice = Dice(0, 0, 0),
        var card: Card = Card(Type.PLUS, "")
) {
    enum class Page { MAIN, CARDS }
    data class Dice(val x: Int, val y: Int, val z: Int) {
        val sum = x + y + z
    }

    data class Card(val type: Type, val card: String, var showing: Boolean = false)

    fun throwDice() {
        dice = Random().run { Dice(nextInt(3), nextInt(3), nextInt(3)) }
    }

    fun drawCard(type: Type) {
        card = Card(type, Cards.obtainCard(type))
    }
}