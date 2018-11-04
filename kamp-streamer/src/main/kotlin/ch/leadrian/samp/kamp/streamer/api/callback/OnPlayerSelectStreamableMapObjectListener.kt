package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObject

interface OnPlayerSelectStreamableMapObjectListener {

    fun onPlayerSelectStreamableMapObject(player: Player, streamableMapObject: StreamableMapObject, modelId: Int, coordinates: Vector3D)

}