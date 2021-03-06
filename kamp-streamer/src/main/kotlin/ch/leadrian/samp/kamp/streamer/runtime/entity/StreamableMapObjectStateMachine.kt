package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateFactory

internal class StreamableMapObjectStateMachine(
        initialState: StreamableMapObjectState,
        private val streamableMapObject: StreamableMapObjectImpl,
        private val streamableMapObjectStateFactory: StreamableMapObjectStateFactory,
        private val mapObjectStreamer: MapObjectStreamer
) {

    var currentState: StreamableMapObjectState = initialState
        private set

    fun transitionToFixedCoordinates(coordinates: Vector3D, rotation: Vector3D) {
        val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
        transitionTo(fixedCoordinates)
    }

    fun transitionToMoving(
            origin: Vector3D,
            destination: Vector3D,
            startRotation: Vector3D,
            targetRotation: Vector3D?,
            speed: Float
    ) {
        val moving = streamableMapObjectStateFactory.createMoving(
                origin = origin,
                destination = destination,
                startRotation = startRotation,
                targetRotation = targetRotation,
                speed = speed,
                onMoved = streamableMapObject::onMoved
        )
        transitionTo(moving)
    }

    fun transitionToAttachedToPlayer(player: Player, offset: Vector3D, rotation: Vector3D) {
        val attachedToPlayer = streamableMapObjectStateFactory.createAttachedToPlayer(
                player = player,
                offset = offset,
                attachRotation = rotation
        )
        transitionTo(attachedToPlayer)
    }

    fun transitionToAttachedToVehicle(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        val attachedToVehicle = streamableMapObjectStateFactory.createAttachedToVehicle(
                vehicle = vehicle,
                offset = offset,
                attachRotation = rotation
        )
        transitionTo(attachedToVehicle)
    }

    private fun transitionTo(newState: StreamableMapObjectState) {
        val oldState = currentState
        oldState.onLeave(streamableMapObject)
        newState.onEnter(streamableMapObject)
        currentState = newState
        mapObjectStreamer.onStateChange(streamableMapObject)
    }

}