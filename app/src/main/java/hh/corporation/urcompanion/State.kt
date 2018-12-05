package hh.corporation.urcompanion

import java.util.*

/**
 * Created by maartendegoede on 04/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
data class State(
        var page: Page,
        var dice: Dice = Dice(0, 0, 0)
) {
    enum class Page { MAIN }
    data class Dice(val x: Int, val y: Int, val z: Int) {
        val sum = x + y + z
    }

    fun throwDice() {
        dice = Random().run { Dice(nextInt(3), nextInt(3), nextInt(3)) }
    }
}