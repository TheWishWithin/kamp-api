package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import javax.inject.Singleton

@Singleton
class PlayerRegistry {

    private val players: Array<Player?> = arrayOfNulls(SAMPConstants.MAX_PLAYERS)

    fun register(player: Player) {
        if (players[player.id.value] != null) {
            throw IllegalStateException("There is already a player with ID ${player.id.value} registered")
        }
        players[player.id.value] = player
    }

    fun unregister(player: Player) {
        if (players[player.id.value] !== player) {
            throw IllegalStateException("Trying to unregister player with ID ${player.id.value} that is not registered")
        }
        players[player.id.value] = null
    }

    fun getPlayer(playerId: PlayerId): Player? = players[playerId.value]

    fun getAllPlayers(): List<Player> = players.filterNotNull()

}