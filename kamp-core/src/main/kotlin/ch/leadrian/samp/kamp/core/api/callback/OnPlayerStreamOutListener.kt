package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerStreamOutListener {

    fun onPlayerStreamOut(player: Player, forPlayer: Player)

}