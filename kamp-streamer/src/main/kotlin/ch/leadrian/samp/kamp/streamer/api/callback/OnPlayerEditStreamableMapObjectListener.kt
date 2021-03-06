package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject

@CallbackListener("ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnPlayerEditStreamableMapObjectListener {

    @InlineCallback("onEdit")
    fun onPlayerEditStreamableMapObject(
            player: Player,
            @Receiver streamableMapObject: StreamableMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    )

}
