package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerMapObjectMovedListener {

    @InlineCallback("onMoved")
    fun onPlayerMapObjectMoved(@Receiver playerMapObject: PlayerMapObject)

}
