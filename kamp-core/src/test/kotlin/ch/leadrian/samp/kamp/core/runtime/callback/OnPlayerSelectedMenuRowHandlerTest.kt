package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerSelectedMenuRowHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerSelectedMenuRowListener>(relaxed = true)
        val listener2 = mockk<OnPlayerSelectedMenuRowListener>(relaxed = true)
        val listener3 = mockk<OnPlayerSelectedMenuRowListener>(relaxed = true)
        val player = mockk<Player>()
        val menuRow = mockk<MenuRow>()
        val onPlayerSelectedMenuRowHandler = OnPlayerSelectedMenuRowHandler()
        onPlayerSelectedMenuRowHandler.register(listener1)
        onPlayerSelectedMenuRowHandler.register(listener2)
        onPlayerSelectedMenuRowHandler.register(listener3)

        onPlayerSelectedMenuRowHandler.onPlayerSelectedMenuRow(player, menuRow)

        verify(exactly = 1) {
            listener1.onPlayerSelectedMenuRow(player, menuRow)
            listener2.onPlayerSelectedMenuRow(player, menuRow)
            listener3.onPlayerSelectedMenuRow(player, menuRow)
        }
    }

}