package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.OnDestroyListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEditStreamableMapObjectReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerSelectStreamableMapObjectReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectMovedReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamInReceiver
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamOutReceiver
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import ch.leadrian.samp.kamp.streamer.runtime.MapObjectStreamer
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerEditStreamableMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnPlayerSelectStreamableMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamInReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutHandler
import ch.leadrian.samp.kamp.streamer.runtime.callback.OnStreamableMapObjectStreamOutReceiverDelegate
import ch.leadrian.samp.kamp.streamer.runtime.entity.factory.StreamableMapObjectStateMachineFactory
import ch.leadrian.samp.kamp.streamer.runtime.util.toRect3d
import com.conversantmedia.util.collection.geometry.Rect3d
import java.util.Locale

internal class StreamableMapObjectImpl
constructor(
        override val modelId: Int,
        override val priority: Int,
        override val streamDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        override var interiorIds: MutableSet<Int>,
        override var virtualWorldIds: MutableSet<Int>,
        private val playerMapObjectService: PlayerMapObjectService,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val onPlayerEditStreamableMapObjectHandler: OnPlayerEditStreamableMapObjectHandler,
        private val onPlayerSelectStreamableMapObjectHandler: OnPlayerSelectStreamableMapObjectHandler,
        private val onStreamableMapObjectStreamInHandler: OnStreamableMapObjectStreamInHandler,
        private val onStreamableMapObjectStreamOutHandler: OnStreamableMapObjectStreamOutHandler,
        private val textProvider: TextProvider,
        private val mapObjectStreamer: MapObjectStreamer,
        streamableMapObjectStateMachineFactory: StreamableMapObjectStateMachineFactory,
        private val onStreamableMapObjectMovedReceiver: OnStreamableMapObjectMovedReceiverDelegate = OnStreamableMapObjectMovedReceiverDelegate(),
        private val onPlayerEditStreamableMapObjectReceiver: OnPlayerEditStreamableMapObjectReceiverDelegate = OnPlayerEditStreamableMapObjectReceiverDelegate(),
        private val onPlayerSelectStreamableMapObjectReceiver: OnPlayerSelectStreamableMapObjectReceiverDelegate = OnPlayerSelectStreamableMapObjectReceiverDelegate(),
        private val onStreamableMapObjectStreamInReceiver: OnStreamableMapObjectStreamInReceiverDelegate = OnStreamableMapObjectStreamInReceiverDelegate(),
        private val onStreamableMapObjectStreamOutReceiver: OnStreamableMapObjectStreamOutReceiverDelegate = OnStreamableMapObjectStreamOutReceiverDelegate()
) : CoordinatesBasedPlayerStreamable<StreamableMapObjectImpl, Rect3d>(),
        OnPlayerDisconnectListener,
        OnDestroyListener,
        OnPlayerEditPlayerMapObjectListener,
        OnPlayerSelectPlayerMapObjectListener,
        StreamableMapObject,
        OnStreamableMapObjectMovedReceiver by onStreamableMapObjectMovedReceiver,
        OnPlayerEditStreamableMapObjectReceiver by onPlayerEditStreamableMapObjectReceiver,
        OnPlayerSelectStreamableMapObjectReceiver by onPlayerSelectStreamableMapObjectReceiver,
        OnStreamableMapObjectStreamInReceiver by onStreamableMapObjectStreamInReceiver,
        OnStreamableMapObjectStreamOutReceiver by onStreamableMapObjectStreamOutReceiver {

    private val playerMapObjectsByPlayer: MutableMap<Player, PlayerMapObject> = mutableMapOf()

    private val stateMachine: StreamableMapObjectStateMachine = streamableMapObjectStateMachineFactory.create(
            streamableMapObject = this,
            mapObjectStreamer = mapObjectStreamer,
            coordinates = coordinates,
            rotation = rotation
    )

    private val materialsByIndex: MutableMap<Int, Material> = mutableMapOf()

    private val materialTextsByIndex: MutableMap<Int, MaterialText> = mutableMapOf()

    internal val playerMapObjects: Collection<PlayerMapObject>
        get() = playerMapObjectsByPlayer.values

    override val drawDistance: Float = streamDistance

    private var visibilityCondition: StreamableMapObject.(Player) -> Boolean = { true }

    override fun isVisible(forPlayer: Player): Boolean = visibilityCondition.invoke(this, forPlayer)

    override fun visibleWhen(condition: StreamableMapObject.(Player) -> Boolean) {
        visibilityCondition = condition
    }

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (playerMapObjectsByPlayer.contains(forPlayer)) {
            throw IllegalStateException("Streamable map object is already streamed in")
        }
        playerMapObjectsByPlayer[forPlayer] = createPlayerMapObject(forPlayer)
        onStreamableMapObjectStreamInReceiver.onStreamableMapObjectStreamIn(this, forPlayer)
        onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(this, forPlayer)
    }

    private fun createPlayerMapObject(forPlayer: Player): PlayerMapObject =
            playerMapObjectService.createPlayerMapObject(
                    player = forPlayer,
                    modelId = modelId,
                    coordinates = coordinates,
                    rotation = rotation,
                    drawDistance = streamDistance
            ).apply(this::initializePlayerMapObject)

    private fun initializePlayerMapObject(playerMapObject: PlayerMapObject) {
        materialsByIndex.forEach { _, material -> material.apply(playerMapObject) }
        materialTextsByIndex.forEach { _, materialText -> materialText.apply(playerMapObject) }
        stateMachine.currentState.onStreamIn(playerMapObject)
        if (isCameraCollisionDisabled) {
            playerMapObject.disableCameraCollision()
        }
        playerMapObject.addOnPlayerEditPlayerMapObjectListener(this)
        playerMapObject.addOnPlayerSelectPlayerMapObjectListener(this)
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        val playerMapObject = playerMapObjectsByPlayer.remove(forPlayer)
                ?: throw IllegalStateException("Streamable player map object was not streamed in")
        playerMapObject.destroy()
        onStreamableMapObjectStreamOutReceiver.onStreamableMapObjectStreamOut(this, forPlayer)
        onStreamableMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(this, forPlayer)
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerMapObjectsByPlayer.contains(forPlayer)

    override fun refresh() {
        playerMapObjectsByPlayer.replaceAll { player, playerMapObject ->
            playerMapObject.destroy()
            createPlayerMapObject(player)
        }
    }

    override var coordinates: Vector3D
        get() = stateMachine.currentState.coordinates
        set(value) {
            requireNotDestroyed()
            removeOnDestroyListener()
            stateMachine.transitionToFixedCoordinates(coordinates = value, rotation = rotation)
            mapObjectStreamer.onBoundingBoxChange(this)
        }

    override var rotation: Vector3D
        get() = stateMachine.currentState.rotation
        set(value) {
            requireNotDestroyed()
            removeOnDestroyListener()
            stateMachine.transitionToFixedCoordinates(coordinates = coordinates, rotation = value)
        }

    private fun fixCoordinates() {
        removeOnDestroyListener()
        stateMachine.transitionToFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
    }

    override var isCameraCollisionDisabled: Boolean = false
        private set

    override fun disableCameraCollision() {
        requireNotDestroyed()
        if (isCameraCollisionDisabled) return
        isCameraCollisionDisabled = true
        playerMapObjectsByPlayer.forEach { _, playerMapObject ->
            playerMapObject.disableCameraCollision()
        }
    }

    override fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D?): Int {
        requireNotDestroyed()
        removeOnDestroyListener()
        stateMachine.transitionToMoving(
                origin = this.coordinates,
                destination = coordinates,
                startRotation = this.rotation,
                targetRotation = rotation,
                speed = speed
        )
        return (stateMachine.currentState as StreamableMapObjectState.Moving).duration.toInt()
    }

    override fun stop() {
        requireNotDestroyed()
        if (isMoving) {
            fixCoordinates()
        }
    }

    override val isMoving: Boolean
        get() = stateMachine.currentState is StreamableMapObjectState.Moving

    internal fun onMoved() {
        onStreamableMapObjectMovedReceiver.onStreamableMapObjectMoved(this)
        onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(this)
    }

    override fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        requireNotDestroyed()
        val material = Material(index, modelId, txdName, textureName, color)
        materialsByIndex[index] = material
        playerMapObjectsByPlayer.forEach { _, playerMapObject -> material.apply(playerMapObject) }
    }

    override fun setMaterialText(
            text: String,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) {
        requireNotDestroyed()
        val materialText = SimpleMaterialText(
                text = text,
                index = index,
                size = size,
                fontFace = fontFace,
                fontSize = fontSize,
                isBold = isBold,
                fontColor = fontColor,
                backColor = backColor,
                textAlignment = textAlignment
        )
        setMaterialText(index, materialText)
    }

    override fun setMaterialText(
            textKey: TextKey,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) {
        requireNotDestroyed()
        val materialText = TranslateMaterialText(
                textKey = textKey,
                textProvider = textProvider,
                index = index,
                size = size,
                fontFace = fontFace,
                fontSize = fontSize,
                isBold = isBold,
                fontColor = fontColor,
                backColor = backColor,
                textAlignment = textAlignment
        )
        setMaterialText(index, materialText)
    }

    private fun setMaterialText(index: Int, materialText: MaterialText) {
        materialTextsByIndex[index] = materialText
        playerMapObjectsByPlayer.forEach { _, playerMapObject -> materialText.apply(playerMapObject) }
    }

    override fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        removeOnDestroyListener()
        stateMachine.transitionToAttachedToPlayer(
                player = player,
                offset = offset,
                rotation = rotation
        )
    }

    override fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        removeOnDestroyListener()
        stateMachine.transitionToAttachedToVehicle(
                vehicle = vehicle,
                offset = offset,
                rotation = rotation
        )
        // TODO unregister somewhere
        vehicle.addOnDestroyListener(this)
    }

    override fun detach() {
        requireNotDestroyed()
        if (isAttached) {
            fixCoordinates()
        }
    }

    override val isAttached: Boolean
        get() = stateMachine.currentState is StreamableMapObjectState.Attached

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    override fun edit(player: Player) {
        playerMapObjectsByPlayer[player]?.edit(player)
    }

    override fun onPlayerEditPlayerMapObject(
            playerMapObject: PlayerMapObject,
            response: ObjectEditResponse,
            offset: Vector3D,
            rotation: Vector3D
    ) {
        requireNotDestroyed()
        if (response == ObjectEditResponse.FINAL) {
            removeOnDestroyListener()
            stateMachine.transitionToFixedCoordinates(coordinates = offset, rotation = rotation)
            mapObjectStreamer.onBoundingBoxChange(this)
        }
        val player = playerMapObject.player
        onPlayerEditStreamableMapObjectReceiver.onPlayerEditStreamableMapObject(
                player = player,
                streamableMapObject = this,
                response = response,
                offset = offset,
                rotation = rotation
        )
        onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                player = player,
                streamableMapObject = this,
                response = response,
                offset = offset,
                rotation = rotation
        )
    }

    override fun onPlayerSelectPlayerMapObject(playerMapObject: PlayerMapObject, modelId: Int, coordinates: Vector3D) {
        val player = playerMapObject.player
        onPlayerSelectStreamableMapObjectReceiver.onPlayerSelectStreamableMapObject(player, this, modelId, coordinates)
        onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(player, this, modelId, coordinates)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapObjectsByPlayer.remove(player)
        val currentState = stateMachine.currentState
        if (currentState is StreamableMapObjectState.Attached.ToPlayer && currentState.player == player) {
            fixCoordinates()
        }
    }

    override fun onDestroy(destroyable: Destroyable) {
        val currentState = stateMachine.currentState
        if (currentState is StreamableMapObjectState.Attached.ToVehicle && currentState.vehicle == destroyable) {
            fixCoordinates()
        }
    }

    override fun onDestroy() {
        removeOnDestroyListener()
        playerMapObjectsByPlayer.values.forEach { it.destroy() }
        playerMapObjectsByPlayer.clear()
    }

    private fun removeOnDestroyListener() {
        val currentState = stateMachine.currentState
        if (currentState is StreamableMapObjectState.Attached.ToVehicle) {
            currentState.vehicle.removeOnDestroyListener(this)
        }
    }

    override val boundingBox: Rect3d
        get() = coordinates.toRect3d(streamDistance)

    private class Material(
            private val index: Int,
            private val modelId: Int,
            private val txdName: String,
            private val textureName: String,
            private val color: Color
    ) {

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.setMaterial(
                    index = index,
                    modelId = modelId,
                    txdName = txdName,
                    textureName = textureName,
                    color = color
            )
        }

    }

    private abstract class MaterialText(
            private val index: Int,
            private val size: ObjectMaterialSize,
            private val fontFace: String,
            private val fontSize: Int,
            private val isBold: Boolean,
            private val fontColor: Color,
            private val backColor: Color,
            private val textAlignment: ObjectMaterialTextAlignment
    ) {

        protected abstract fun getText(locale: Locale): String

        fun apply(playerMapObject: PlayerMapObject) {
            playerMapObject.setMaterialText(
                    text = getText(playerMapObject.player.locale),
                    index = index,
                    size = size,
                    fontFace = fontFace,
                    fontSize = fontSize,
                    isBold = isBold,
                    fontColor = fontColor,
                    backColor = backColor,
                    textAlignment = textAlignment
            )
        }

    }

    private class SimpleMaterialText(
            private val text: String,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) : MaterialText(
            index = index,
            size = size,
            fontFace = fontFace,
            fontSize = fontSize,
            isBold = isBold,
            fontColor = fontColor,
            backColor = backColor,
            textAlignment = textAlignment
    ) {

        override fun getText(locale: Locale): String = text

    }

    private class TranslateMaterialText(
            private val textKey: TextKey,
            private val textProvider: TextProvider,
            index: Int,
            size: ObjectMaterialSize,
            fontFace: String,
            fontSize: Int,
            isBold: Boolean,
            fontColor: Color,
            backColor: Color,
            textAlignment: ObjectMaterialTextAlignment
    ) : MaterialText(
            index = index,
            size = size,
            fontFace = fontFace,
            fontSize = fontSize,
            isBold = isBold,
            fontColor = fontColor,
            backColor = backColor,
            textAlignment = textAlignment
    ) {

        override fun getText(locale: Locale): String = textProvider.getText(locale, textKey)

    }

}