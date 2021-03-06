package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import javax.inject.Inject

/**
 * Default [InvalidCommandParameterValueHandler] that will be used if none other was provided through a [ch.leadrian.samp.kamp.core.api.inject.KampModule].
 */
open class DefaultInvalidCommandParameterValueHandler
@Inject
constructor(
        private val messageSender: MessageSender,
        private val textProvider: TextProvider
) : InvalidCommandParameterValueHandler {

    /**
     * Sends the player a message describing how to use the command properly.
     *
     * Example:
     * ```
     * Usage: /pm [Player] [Text]
     * ```
     */
    override fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>,
            parameterIndex: Int?
    ): OnPlayerCommandTextListener.Result {
        val message = StringBuilder().apply {
            append(textProvider.getText(player.locale, KampCoreTextKeys.command.usage.prefix))
            append(": /")
            append(commandDefinition.name)
            commandDefinition.parameters.forEach {
                append(" [")
                append(it.getName(player.locale))
                append(']')
            }
        }.toString()
        messageSender.sendMessageToPlayer(player, Colors.RED, message)
        return OnPlayerCommandTextListener.Result.Processed
    }
}