package ch.leadrian.samp.kamp.view.factory

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.BackgroundView
import ch.leadrian.samp.kamp.view.base.ModelView
import ch.leadrian.samp.kamp.view.base.SpriteView
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.composite.ButtonView
import ch.leadrian.samp.kamp.view.composite.HorizontalScrollBarView
import ch.leadrian.samp.kamp.view.composite.ScrollBarAdapter
import ch.leadrian.samp.kamp.view.composite.VerticalScrollBarView

interface ViewFactory {

    val viewContext: ViewContext

    val textProvider: TextProvider

    val textFormatter: TextFormatter

    val playerTextDrawService: PlayerTextDrawService

    fun view(player: Player, buildingBlock: View.() -> Unit): View {
        val view = View(player, viewContext)
        buildingBlock(view)
        return view
    }

    fun View.view(buildingBlock: View.() -> Unit): View {
        val view = view(player, buildingBlock)
        addChild(view)
        return view
    }

    fun textView(player: Player, buildingBlock: TextView.() -> Unit): TextView {
        val textView = TextView(player, viewContext, textProvider, textFormatter, playerTextDrawService)
        buildingBlock(textView)
        return textView
    }

    fun View.textView(buildingBlock: TextView.() -> Unit): TextView {
        val textView = textView(player, buildingBlock)
        addChild(textView)
        return textView
    }

    fun backgroundView(player: Player, buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = BackgroundView(player, viewContext, playerTextDrawService)
        buildingBlock(backgroundView)
        return backgroundView
    }

    fun View.backgroundView(buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = backgroundView(player, buildingBlock)
        addChild(backgroundView)
        return backgroundView
    }

    fun modelView(player: Player, buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = ModelView(player, viewContext, playerTextDrawService)
        buildingBlock(modelView)
        return modelView
    }

    fun View.modelView(buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = modelView(player, buildingBlock)
        addChild(modelView)
        return modelView
    }

    fun spriteView(player: Player, buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = SpriteView(player, viewContext, playerTextDrawService)
        buildingBlock(spriteView)
        return spriteView
    }

    fun View.spriteView(buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = spriteView(player, buildingBlock)
        addChild(spriteView)
        return spriteView
    }

    fun buttonView(player: Player, buildingBlock: ButtonView.() -> Unit): ButtonView {
        val buttonView = ButtonView(player, viewContext, this)
        buildingBlock(buttonView)
        return buttonView
    }

    fun View.buttonView(buildingBlock: ButtonView.() -> Unit): ButtonView {
        val buttonView = buttonView(player, buildingBlock)
        addChild(buttonView)
        return buttonView
    }

    fun verticalScrollBarView(player: Player, adapter: ScrollBarAdapter, buildingBlock: VerticalScrollBarView.() -> Unit): VerticalScrollBarView {
        val verticalScrollBarView = VerticalScrollBarView(player, viewContext, this, adapter)
        buildingBlock(verticalScrollBarView)
        return verticalScrollBarView
    }

    fun View.verticalScrollBarView(adapter: ScrollBarAdapter, buildingBlock: VerticalScrollBarView.() -> Unit): VerticalScrollBarView {
        val verticalScrollBarView = verticalScrollBarView(player, adapter, buildingBlock)
        addChild(verticalScrollBarView)
        return verticalScrollBarView
    }

    fun horizontalScrollBarView(player: Player, adapter: ScrollBarAdapter, buildingBlock: HorizontalScrollBarView.() -> Unit): HorizontalScrollBarView {
        val horizontalScrollBarView = HorizontalScrollBarView(player, viewContext, this, adapter)
        buildingBlock(horizontalScrollBarView)
        return horizontalScrollBarView
    }

    fun View.horizontalScrollBarView(adapter: ScrollBarAdapter, buildingBlock: HorizontalScrollBarView.() -> Unit): HorizontalScrollBarView {
        val horizontalScrollBarView = horizontalScrollBarView(player, adapter, buildingBlock)
        addChild(horizontalScrollBarView)
        return horizontalScrollBarView
    }

}