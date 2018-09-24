package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerRequestClassHandler
@Inject
constructor() : CallbackListenerRegistry<OnPlayerRequestClassListener>(OnPlayerRequestClassListener::class), OnPlayerRequestClassListener {

    override fun onPlayerRequestClass(player: Player, playerClass: PlayerClass) {
        listeners.forEach {
            it.onPlayerRequestClass(player, playerClass)
        }
    }

}