package ch.leadrian.samp.kamp.streamer.runtime

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.service.PlayerService
import ch.leadrian.samp.kamp.streamer.runtime.entity.DistanceBasedPlayerStreamable
import javax.inject.Inject

class DistanceBasedPlayerStreamerFactory
@Inject
internal constructor(
        private val asyncExecutor: AsyncExecutor,
        private val playerService: PlayerService
) {

    fun <T : DistanceBasedPlayerStreamable> create(
            maxCapacity: Int,
            streamInCandidateSupplier: StreamInCandidateSupplier<T>
    ): DistanceBasedPlayerStreamer<T> = DistanceBasedPlayerStreamer(
            maxCapacity = maxCapacity,
            asyncExecutor = asyncExecutor,
            streamInCandidateSupplier = streamInCandidateSupplier,
            playerService = playerService
    )

}