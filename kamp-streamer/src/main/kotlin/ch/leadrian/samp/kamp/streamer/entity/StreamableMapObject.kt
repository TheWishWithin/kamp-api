package ch.leadrian.samp.kamp.streamer.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDestructionListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.service.PlayerMapObjectService
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerEditStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnPlayerSelectStreamableMapObjectHandler
import ch.leadrian.samp.kamp.streamer.callback.OnStreamableMapObjectMovedHandler
import com.conversantmedia.util.collection.geometry.Rect3d
import java.util.*

class StreamableMapObject
internal constructor(
        val modelId: Int,
        override val priority: Int,
        override val streamDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        var interiorIds: MutableSet<Int>,
        var virtualWorldIds: MutableSet<Int>,
        private val playerMapObjectService: PlayerMapObjectService,
        private val onStreamableMapObjectMovedHandler: OnStreamableMapObjectMovedHandler,
        private val onPlayerEditStreamableMapObjectHandler: OnPlayerEditStreamableMapObjectHandler,
        private val onPlayerSelectStreamableMapObjectHandler: OnPlayerSelectStreamableMapObjectHandler,
        private val textProvider: TextProvider,
        private val streamableMapObjectStateFactory: StreamableMapObjectStateFactory
) : DistanceBasedPlayerStreamable,
        SpatiallyIndexedStreamable<StreamableMapObject, Rect3d>(),
        OnPlayerDisconnectListener,
        OnVehicleDestructionListener {

    private val playerMapObjects: MutableMap<Player, PlayerMapObject> = mutableMapOf()

    private val onMovedHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private val onEditHandlers: MutableList<StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit> = mutableListOf()

    private val onSelectHandlers: MutableList<StreamableMapObject.(Player, Int, Vector3D) -> Unit> = mutableListOf()

    private val onStateChangeHandlers: MutableList<StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit> = mutableListOf()

    private val onDestroyHandlers: MutableList<StreamableMapObject.() -> Unit> = mutableListOf()

    private var state: StreamableMapObjectState

    private val materialsByIndex: MutableMap<Int, Material> = mutableMapOf()

    private val materialTextsByIndex: MutableMap<Int, MaterialText> = mutableMapOf()

    init {
        state = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
    }

    override val self: StreamableMapObject = this

    override fun onStreamIn(forPlayer: Player) {
        requireNotDestroyed()
        if (playerMapObjects.contains(forPlayer)) {
            throw IllegalStateException("Streamable map object is already streamed in")
        }
        playerMapObjects[forPlayer] = createPlayerMapObject(forPlayer).apply(this::initializePlayerMapObject)
    }

    private fun createPlayerMapObject(forPlayer: Player): PlayerMapObject =
            playerMapObjectService.createPlayerMapObject(
                    player = forPlayer,
                    modelId = modelId,
                    coordinates = coordinates,
                    rotation = rotation,
                    drawDistance = streamDistance
            )

    private fun initializePlayerMapObject(playerMapObject: PlayerMapObject) {
        materialsByIndex.forEach { _, material -> material.apply(playerMapObject) }
        materialTextsByIndex.forEach { _, materialText -> materialText.apply(playerMapObject) }
        state.onStreamIn(playerMapObject)
        if (isCameraCollisionDisabled) {
            playerMapObject.disableCameraCollision()
        }
        playerMapObject.onEdit { objectEditResponse, offset, rotation ->
            this@StreamableMapObject.onEdit(this.player, objectEditResponse, offset, rotation)
        }
        playerMapObject.onSelect { modelId, offset ->
            this@StreamableMapObject.onSelect(this.player, modelId, offset)
        }
    }

    override fun onStreamOut(forPlayer: Player) {
        requireNotDestroyed()
        val playerMapObject = playerMapObjects.remove(forPlayer)
                ?: throw IllegalStateException("Streamable player map object was not streamed it")
        playerMapObject.destroy()
    }

    override fun isStreamedIn(forPlayer: Player): Boolean = playerMapObjects.contains(forPlayer)

    override var streamInCondition: (Player) -> Boolean = { true }

    private fun transitionToState(newState: StreamableMapObjectState) {
        val oldState = state
        if (oldState.isStreamOutRequiredOnLeave(newState)) {
            streamOutAllPlayerMapObjects()
        }
        val playerMapObjects = playerMapObjects.values
        oldState.onLeave(playerMapObjects)
        newState.onEnter(playerMapObjects)
        state = newState
        onStateChange(oldState, newState)
    }

    private fun onStateChange(oldState: StreamableMapObjectState, newState: StreamableMapObjectState) {
        onStateChangeHandlers.forEach { it.invoke(this, oldState, newState) }
    }

    internal fun onStateChange(onStateChange: StreamableMapObject.(StreamableMapObjectState, StreamableMapObjectState) -> Unit) {
        onStateChangeHandlers += onStateChange
    }

    private fun streamOutAllPlayerMapObjects() {
        playerMapObjects.values.forEach { it.destroy() }
        playerMapObjects.clear()
    }

    var coordinates: Vector3D
        get() = state.coordinates
        set(value) {
            requireNotDestroyed()
            val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                    coordinates = value,
                    rotation = rotation
            )
            transitionToState(fixedCoordinates)
            onBoundingBoxChanged()
        }

    var rotation: Vector3D
        get() = state.rotation
        set(value) {
            requireNotDestroyed()
            val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                    coordinates = coordinates,
                    rotation = value
            )
            transitionToState(fixedCoordinates)
        }

    private fun fixCoordinates() {
        val fixedCoordinates = streamableMapObjectStateFactory.createFixedCoordinates(
                coordinates = coordinates,
                rotation = rotation
        )
        transitionToState(fixedCoordinates)
    }

    var isCameraCollisionDisabled: Boolean = false
        private set

    fun disableCameraCollision() {
        requireNotDestroyed()
        if (isCameraCollisionDisabled) return
        isCameraCollisionDisabled = true
        playerMapObjects.forEach { _, playerMapObject ->
            playerMapObject.disableCameraCollision()
        }
    }

    @JvmOverloads
    fun moveTo(
            destination: Vector3D,
            speed: Float,
            targetRotation: Vector3D? = null
    ) {
        requireNotDestroyed()
        val moving = streamableMapObjectStateFactory.createMoving(
                origin = coordinates,
                destination = destination,
                startRotation = rotation,
                targetRotation = targetRotation,
                speed = speed,
                onMoved = this::onMoved
        )
        transitionToState(moving)
    }

    fun stop() {
        requireNotDestroyed()
        if (isMoving) {
            fixCoordinates()
        }
    }

    val isMoving: Boolean
        get() = state is StreamableMapObjectState.Moving

    private fun onMoved() {
        onMovedHandlers.forEach { it.invoke(this) }
        onStreamableMapObjectMovedHandler.onStreamableMapObjectMoved(this)
    }

    fun onMoved(onMoved: StreamableMapObject.() -> Unit) {
        onMovedHandlers += onMoved
    }

    fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        requireNotDestroyed()
        val material = Material(index, modelId, txdName, textureName, color)
        materialsByIndex[index] = material
        playerMapObjects.forEach { _, playerMapObject -> material.apply(playerMapObject) }
    }

    @JvmOverloads
    fun setMaterialText(
            text: String,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = Colors.WHITE,
            backColor: Color = Colors.TRANSPARENT,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
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

    @JvmOverloads
    fun setMaterialText(
            textKey: TextKey,
            index: Int = 0,
            size: ObjectMaterialSize = ObjectMaterialSize.SIZE_256X128,
            fontFace: String = "Arial",
            fontSize: Int = 24,
            isBold: Boolean = true,
            fontColor: Color = Colors.WHITE,
            backColor: Color = Colors.TRANSPARENT,
            textAlignment: ObjectMaterialTextAlignment = ObjectMaterialTextAlignment.LEFT
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
        playerMapObjects.forEach { _, playerMapObject -> materialText.apply(playerMapObject) }
    }

    fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        val newState = streamableMapObjectStateFactory.createAttachedToPlayer(
                player = player,
                offset = offset,
                attachRotation = rotation
        )
        transitionToState(newState)
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        requireNotDestroyed()
        val newState = streamableMapObjectStateFactory.createAttachedToVehicle(
                vehicle = vehicle,
                offset = offset,
                attachRotation = rotation
        )
        transitionToState(newState)
    }

    fun detach() {
        requireNotDestroyed()
        if (isAttached) {
            fixCoordinates()
        }
    }

    val isAttached: Boolean
        get() = state is StreamableMapObjectState.Attached

    override fun distanceTo(location: Location): Float =
            when {
                interiorIds.isNotEmpty() && !interiorIds.contains(location.interiorId) -> Float.MAX_VALUE
                virtualWorldIds.isNotEmpty() && !virtualWorldIds.contains(location.virtualWorldId) -> Float.MAX_VALUE
                else -> coordinates.distanceTo(location)
            }

    fun edit(player: Player) {
        playerMapObjects[player]?.edit(player)
    }

    fun onEdit(onEdit: StreamableMapObject.(Player, ObjectEditResponse, Vector3D, Vector3D) -> Unit) {
        onEditHandlers += onEdit
    }

    private fun onEdit(player: Player, response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        if (response == ObjectEditResponse.FINAL) {
            coordinates = offset
            this.rotation = rotation
        }
        onEditHandlers.forEach { it.invoke(this, player, response, offset, rotation) }
        onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                player = player,
                streamableMapObject = this,
                response = response,
                offset = offset,
                rotation = rotation
        )
    }

    fun onSelect(onSelect: StreamableMapObject.(Player, Int, Vector3D) -> Unit) {
        onSelectHandlers += onSelect
    }

    private fun onSelect(player: Player, modelId: Int, offset: Vector3D) {
        onSelectHandlers.forEach { it.invoke(this, player, modelId, offset) }
        onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(player, this, modelId, coordinates)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapObjects.remove(player)
        val currentState = state
        if (currentState is StreamableMapObjectState.Attached.ToPlayer && currentState.player == player) {
            fixCoordinates()
        }
    }

    override fun onVehicleDestruction(vehicle: Vehicle) {
        val currentState = state
        if (currentState is StreamableMapObjectState.Attached.ToVehicle && currentState.vehicle == vehicle) {
            fixCoordinates()
        }
    }

    fun onDestroy(onDestroy: StreamableMapObject.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) {
            return
        }

        onDestroyHandlers.forEach { it.invoke(this) }
        playerMapObjects.forEach { _, playerMapObject -> playerMapObject.destroy() }
        playerMapObjects.clear()
        isDestroyed = true
    }

    override fun getBoundingBox(): Rect3d {
        val coordinates = coordinates
        val minX = coordinates.x - streamDistance
        val minY = coordinates.y - streamDistance
        val minZ = coordinates.z - streamDistance
        val maxX = coordinates.x + streamDistance
        val maxY = coordinates.y + streamDistance
        val maxZ = coordinates.z + streamDistance

        return Rect3d(
                minX.toDouble(),
                minY.toDouble(),
                minZ.toDouble(),
                maxX.toDouble(),
                maxY.toDouble(),
                maxZ.toDouble()
        )
    }

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