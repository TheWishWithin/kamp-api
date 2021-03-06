package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.view.base.View
import java.util.LinkedList

class ViewNavigation
internal constructor(private val viewNavigationElementFactory: ViewNavigationElementFactory) : AbstractDestroyable() {

    private val stack = LinkedList<ViewNavigationElement>()

    val top: View?
        get() = stack.peek()?.view

    val isEmpty: Boolean
        get() = stack.isEmpty()

    val size: Int
        get() = stack.size

    val isManualNavigationAllowed: Boolean
        get() = stack.peek()?.allowManualNavigation ?: true

    val isMouseUsed: Boolean
        get() = stack.peek()?.useMouse ?: false

    @JvmOverloads
    fun push(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true) {
        requireNotDestroyed()
        top?.hide()
        val element = viewNavigationElementFactory.create(
                view = view,
                allowManualNavigation = allowManualNavigation,
                useMouse = useMouse
        )
        stack.push(element)
        element.navigateTo()
    }

    @JvmOverloads
    fun setRoot(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true) {
        requireNotDestroyed()
        clear()
        push(view = view, allowManualNavigation = allowManualNavigation, useMouse = useMouse)
    }

    @JvmOverloads
    fun replaceTop(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true) {
        requireNotDestroyed()
        removeTop()
        push(view = view, allowManualNavigation = allowManualNavigation, useMouse = useMouse)
    }

    fun pop() {
        requireNotDestroyed()
        removeTop()
        stack.peek()?.navigateTo()
    }

    private fun removeTop() {
        if (stack.isNotEmpty()) {
            stack.pop().view.hide()
        }
    }

    fun clear() {
        stack.reversed().forEach { it.view.hide() }
        stack.clear()
    }

    override fun onDestroy() {
        stack.forEach { it.view.destroy() }
        stack.clear()
    }

}

val Player.viewNavigation: ViewNavigation
    get() = extensions[ViewNavigation::class]