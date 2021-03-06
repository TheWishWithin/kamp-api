package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import javax.inject.Inject

class PlayerMessageSender
@Inject
internal constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val playerRegistry: PlayerRegistry,
        private val messagePreparer: MessagePreparer
) {

    fun sendPlayerMessageToAll(fromPlayer: Player, message: String) {
        nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayer.id.value, message)
    }

    fun sendPlayerMessageToAll(fromPlayer: Player, message: String, vararg args: Any) {
        messagePreparer.prepareForAllPlayers(
                color = Colors.WHITE,
                text = message,
                args = args,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendPlayerMessageToPlayer(
                            playerid = player.id.value,
                            message = playerMessage,
                            senderid = fromPlayer.id.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayer.id.value, it) }
        )
    }

    fun sendPlayerMessageToAll(fromPlayer: Player, textKey: TextKey) {
        messagePreparer.prepareForAllPlayers(
                textKey = textKey,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendPlayerMessageToPlayer(
                            playerid = player.id.value,
                            message = playerMessage,
                            senderid = fromPlayer.id.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayer.id.value, it) }
        )
    }

    fun sendPlayerMessageToAll(fromPlayer: Player, textKey: TextKey, vararg args: Any) {
        messagePreparer.prepareForAllPlayers(
                color = Colors.WHITE,
                textKey = textKey,
                args = args,
                consumer = { player, playerMessage ->
                    nativeFunctionExecutor.sendPlayerMessageToPlayer(
                            playerid = player.id.value,
                            message = playerMessage,
                            senderid = fromPlayer.id.value
                    )
                },
                consumerForAll = { nativeFunctionExecutor.sendPlayerMessageToAll(fromPlayer.id.value, it) }
        )
    }

    fun sendPlayerMessageToPlayer(toPlayer: Player, fromPlayer: Player, message: String) {
        nativeFunctionExecutor.sendPlayerMessageToPlayer(
                playerid = toPlayer.id.value,
                senderid = fromPlayer.id.value,
                message = message
        )
    }

    fun sendPlayerMessageToPlayer(toPlayer: Player, fromPlayer: Player, message: String, vararg args: Any) {
        messagePreparer.prepareForPlayer(Colors.WHITE, toPlayer, message, args) { _, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = toPlayer.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

    fun sendPlayerMessageToPlayer(toPlayer: Player, fromPlayer: Player, textKey: TextKey) {
        messagePreparer.prepareForPlayer(toPlayer, textKey) { _, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = toPlayer.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

    fun sendPlayerMessageToPlayer(toPlayer: Player, fromPlayer: Player, textKey: TextKey, vararg args: Any) {
        messagePreparer.prepareForPlayer(Colors.WHITE, toPlayer, textKey, args) { _, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = toPlayer.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

    fun sendPlayerMessage(fromPlayer: Player, message: String, playerFilter: (Player) -> Boolean) {
        playerRegistry
                .getAll()
                .asSequence()
                .filter { playerFilter(it) }
                .forEach {
                    nativeFunctionExecutor.sendPlayerMessageToPlayer(
                            playerid = it.id.value,
                            senderid = fromPlayer.id.value,
                            message = message
                    )
                }
    }

    fun sendPlayerMessage(fromPlayer: Player, message: String, vararg args: Any, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(Colors.WHITE, playerFilter, message, args) { player, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = player.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

    fun sendPlayerMessage(fromPlayer: Player, textKey: TextKey, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(playerFilter, textKey) { player, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = player.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

    fun sendPlayerMessage(fromPlayer: Player, textKey: TextKey, vararg args: Any, playerFilter: (Player) -> Boolean) {
        messagePreparer.prepare(Colors.WHITE, playerFilter, textKey, args) { player, playerMessage ->
            nativeFunctionExecutor.sendPlayerMessageToPlayer(
                    playerid = player.id.value,
                    senderid = fromPlayer.id.value,
                    message = playerMessage
            )
        }
    }

}