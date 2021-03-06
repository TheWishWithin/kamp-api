package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.FunctionalDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.dialog.TextKeyDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry

internal class TabListDialog<V : Any>(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val onSelectItem: (Dialog.(Player, TabListDialogItem<V>, String) -> Unit)?,
        private val onCancel: (Dialog.(Player) -> OnDialogResponseListener.Result)?,
        private val headerTextSuppliers: List<DialogTextSupplier>?,
        private val items: List<TabListDialogItem<V>>
) : AbstractDialog(id) {

    private companion object {

        val log = loggerFor<TabListDialog<*>>()

    }

    override fun show(forPlayer: Player) {
        val itemsString = buildItemsString(forPlayer)
        log.debug("Dialog {}: Showing for player {}: {}", id.value, forPlayer.id.value, itemsString)
        val style = when (headerTextSuppliers) {
            null -> DialogStyle.TABLIST
            else -> DialogStyle.TABLIST_HEADERS
        }
        forPlayer.showDialog(
                dialog = this,
                caption = captionTextSupplier.getText(forPlayer),
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                style = style,
                message = itemsString
        )
    }

    private fun buildItemsString(forPlayer: Player): String =
            StringBuilder(1024).apply {
                if (headerTextSuppliers != null) {
                    headerTextSuppliers.forEachIndexed { index, header ->
                        if (index > 0) {
                            append('\t')
                        }
                        append(header.getText(forPlayer))
                    }
                    append('\n')
                }
                items.forEach { item ->
                    item.getTabbedContent(forPlayer).forEachIndexed { index, tabbedContent ->
                        if (index > 0) {
                            append('\t')
                        }
                        append(tabbedContent)
                    }
                    append('\n')
                }
            }.toString()

    override fun onResponse(
            player: Player,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        return when (response) {
            DialogResponse.LEFT_BUTTON -> handleLeftButtonClick(player, listItem, inputText)
            DialogResponse.RIGHT_BUTTON -> handleRightButtonClick(player)
        }
    }

    private fun handleLeftButtonClick(
            player: Player,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        val item = items.getOrNull(listItem)
        return if (item != null) {
            item.onSelect(player, inputText)
            onSelectItem?.invoke(this, player, item, inputText)
            OnDialogResponseListener.Result.Processed
        } else {
            log.warn(
                    "Dialog {}: Invalid dialog item selected by player {}: {}, {} items available",
                    id.value,
                    player.name,
                    listItem,
                    items.size
            )
            onCancel?.invoke(this, player) ?: OnDialogResponseListener.Result.Ignored
        }
    }

    private fun handleRightButtonClick(player: Player): OnDialogResponseListener.Result =
            onCancel?.invoke(this, player) ?: OnDialogResponseListener.Result.Ignored

    internal class Builder<V : Any>(
            textProvider: TextProvider,
            private val dialogRegistry: DialogRegistry
    ) : AbstractDialogBuilder<TabListDialogBuilder<V>>(textProvider), TabListDialogBuilder<V> {

        private var items = mutableListOf<TabListDialogItem<V>>()

        private var headerTextSuppliers: List<DialogTextSupplier>? = null

        private var onCancel: (Dialog.(Player) -> OnDialogResponseListener.Result)? = null

        private var onSelectItem: (Dialog.(Player, TabListDialogItem<V>, String) -> Unit)? = null

        override fun headerContent(vararg text: String): Builder<V> {
            headerTextSuppliers = text.map { StringDialogTextSupplier(it) }
            return self
        }

        override fun headerContent(vararg textKey: TextKey): Builder<V> {
            headerTextSuppliers = textKey.map { TextKeyDialogTextSupplier(it, textProvider) }
            return self
        }

        override fun headerContent(vararg supplier: (Player) -> String): Builder<V> {
            headerTextSuppliers = supplier.map { FunctionalDialogTextSupplier(it) }
            return self
        }

        override fun headerContent(vararg supplier: DialogTextSupplier): Builder<V> {
            headerTextSuppliers = supplier.toList()
            return self
        }

        override fun item(builder: TabListDialogItem.Builder<V>.() -> Unit): Builder<V> {
            val item = DefaultTabListDialogItem.Builder<V>(textProvider).apply(builder).build()
            items.add(item)
            return self
        }

        override fun item(item: TabListDialogItem<V>): Builder<V> {
            items.add(item)
            return self
        }

        override fun items(vararg item: TabListDialogItem<V>): Builder<V> {
            items.addAll(item)
            return self
        }

        override fun items(items: Collection<TabListDialogItem<V>>): Builder<V> {
            this.items.addAll(items)
            return self
        }

        override fun onCancel(onCancel: Dialog.(Player) -> OnDialogResponseListener.Result): Builder<V> {
            this.onCancel = onCancel
            return self
        }

        override fun onSelectItem(onSelectItem: Dialog.(Player, TabListDialogItem<V>, String) -> Unit): Builder<V> {
            this.onSelectItem = onSelectItem
            return self
        }

        override fun build(): TabListDialog<V> = dialogRegistry.register { dialogId ->
            TabListDialog(
                    id = dialogId,
                    captionTextSupplier = captionTextSupplier,
                    leftButtonTextSupplier = leftButtonTextSupplier,
                    rightButtonTextSupplier = rightButtonTextSupplier,
                    headerTextSuppliers = headerTextSuppliers,
                    items = items,
                    onCancel = onCancel,
                    onSelectItem = onSelectItem
            )
        }

        override val self: Builder<V> = this

    }
}