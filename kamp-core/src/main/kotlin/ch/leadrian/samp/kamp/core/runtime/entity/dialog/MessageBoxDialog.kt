package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.FunctionalDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TextKeyDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry

internal class MessageBoxDialog(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val messageTextSupplier: DialogTextSupplier,
        private val onClickLeftButton: (Dialog.(Player) -> Unit)?,
        private val onClickRightButton: (Dialog.(Player) -> OnDialogResponseListener.Result)?
) : AbstractDialog(id) {

    override fun show(forPlayer: Player) {
        forPlayer.showDialog(
                dialog = this,
                style = DialogStyle.MSGBOX,
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                caption = captionTextSupplier.getText(forPlayer),
                message = messageTextSupplier.getText(forPlayer)
        )
    }

    override fun onResponse(
            player: Player,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        return when (response) {
            DialogResponse.LEFT_BUTTON -> {
                onClickLeftButton?.invoke(this, player)
                OnDialogResponseListener.Result.Processed
            }
            DialogResponse.RIGHT_BUTTON -> onClickRightButton?.invoke(this, player)
                    ?: OnDialogResponseListener.Result.Ignored
        }
    }

    internal class Builder(
            textProvider: TextProvider,
            private val dialogRegistry: DialogRegistry
    ) : AbstractDialogBuilder<MessageBoxDialogBuilder>(textProvider), MessageBoxDialogBuilder {

        private var messageTextSupplier: DialogTextSupplier =
                TextKeyDialogTextSupplier(KampCoreTextKeys.dialog.message.missing, textProvider)

        private var onClickLeftButton: (Dialog.(Player) -> Unit)? = null

        private var onClickRightButton: (Dialog.(Player) -> OnDialogResponseListener.Result)? = null

        override fun message(text: String): Builder {
            messageTextSupplier = StringDialogTextSupplier(text)
            return self
        }

        override fun message(textKey: TextKey): Builder {
            messageTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)
            return self
        }

        override fun message(supplier: (Player) -> String): Builder {
            messageTextSupplier = FunctionalDialogTextSupplier(supplier)
            return self
        }

        override fun message(supplier: DialogTextSupplier): Builder {
            messageTextSupplier = supplier
            return self
        }

        override fun onClickLeftButton(onClickLeftButton: Dialog.(Player) -> Unit): Builder {
            this.onClickLeftButton = onClickLeftButton
            return self
        }

        override fun onClickRightButton(onClickRightButton: Dialog.(Player) -> OnDialogResponseListener.Result): Builder {
            this.onClickRightButton = onClickRightButton
            return self
        }

        override fun build(): MessageBoxDialog = dialogRegistry.register { dialogId ->
            MessageBoxDialog(
                    id = dialogId,
                    captionTextSupplier = captionTextSupplier,
                    leftButtonTextSupplier = leftButtonTextSupplier,
                    rightButtonTextSupplier = rightButtonTextSupplier,
                    messageTextSupplier = messageTextSupplier,
                    onClickLeftButton = onClickLeftButton,
                    onClickRightButton = onClickRightButton
            )
        }

        override val self: Builder = this
    }
}