package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableRaceCheckpoint

@CallbackListener("ch.leadrian.samp.kamp.streamer.runtime.callback")
interface OnPlayerEnterStreamableRaceCheckpoint {

    @InlineCallback("onEnter")
    fun onPlayerEnterStreamableRaceCheckpoint(player: Player, @Receiver streamableRaceCheckpoint: StreamableRaceCheckpoint)

}