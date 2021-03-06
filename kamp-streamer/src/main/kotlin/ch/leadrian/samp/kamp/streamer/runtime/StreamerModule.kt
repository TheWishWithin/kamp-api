package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerDamageStreamableActorHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEnterStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableAreaHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerLeaveStreamableRaceCheckpointHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerPickUpStreamablePickupHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableActorStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapIconStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamablePickupStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableRaceCheckpointStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableTextLabelStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.entity.PlayerMapIconIdAllocator

internal class StreamerModule : AbstractStreamerModule() {

    override fun configure() {
        newCallbackListenerRegistrySetBinder().apply {
            addBinding().to(OnPlayerSelectStreamableMapObjectHandler::class.java)
            addBinding().to(OnPlayerEditStreamableMapObjectHandler::class.java)
            addBinding().to(OnStreamableMapObjectMovedHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamInHandler::class.java)
            addBinding().to(OnStreamableMapObjectStreamOutHandler::class.java)

            addBinding().to(OnStreamableActorStreamInHandler::class.java)
            addBinding().to(OnStreamableActorStreamOutHandler::class.java)
            addBinding().to(OnPlayerDamageStreamableActorHandler::class.java)

            addBinding().to(OnStreamableTextLabelStreamInHandler::class.java)
            addBinding().to(OnStreamableTextLabelStreamOutHandler::class.java)

            addBinding().to(OnStreamablePickupStreamInHandler::class.java)
            addBinding().to(OnStreamablePickupStreamOutHandler::class.java)
            addBinding().to(OnPlayerPickUpStreamablePickupHandler::class.java)

            addBinding().to(OnStreamableMapIconStreamInHandler::class.java)
            addBinding().to(OnStreamableMapIconStreamOutHandler::class.java)

            addBinding().to(OnStreamableCheckpointStreamInHandler::class.java)
            addBinding().to(OnStreamableCheckpointStreamOutHandler::class.java)
            addBinding().to(OnPlayerEnterStreamableCheckpointHandler::class.java)
            addBinding().to(OnPlayerLeaveStreamableCheckpointHandler::class.java)

            addBinding().to(OnStreamableRaceCheckpointStreamInHandler::class.java)
            addBinding().to(OnStreamableRaceCheckpointStreamOutHandler::class.java)
            addBinding().to(OnPlayerEnterStreamableRaceCheckpointHandler::class.java)
            addBinding().to(OnPlayerLeaveStreamableRaceCheckpointHandler::class.java)

            addBinding().to(OnPlayerEnterStreamableAreaHandler::class.java)
            addBinding().to(OnPlayerLeaveStreamableAreaHandler::class.java)
        }
        newStreamerSetBinder().apply {
            addBinding().to(MapObjectStreamer::class.java)
            addBinding().to(TextLabelStreamer::class.java)
            addBinding().to(ActorStreamer::class.java)
            addBinding().to(PickupStreamer::class.java)
            addBinding().to(MapIconStreamer::class.java)
            addBinding().to(CheckpointStreamer::class.java)
            addBinding().to(RaceCheckpointStreamer::class.java)
            addBinding().to(Area2DStreamer::class.java)
            addBinding().to(Area3DStreamer::class.java)
        }
        bind(StreamerExecutor::class.java).asEagerSingleton()
        bind(PlayerMapIconIdAllocator::class.java).asEagerSingleton()
    }

}