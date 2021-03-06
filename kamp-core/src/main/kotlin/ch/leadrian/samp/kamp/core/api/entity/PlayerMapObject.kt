package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerMapObjectMovedReceiver
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectReceiver
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize
import ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEditPlayerMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerMapObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerSelectPlayerMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerMapObjectCoordinatesProperty
import ch.leadrian.samp.kamp.core.runtime.entity.property.PlayerMapObjectRotationProperty

class PlayerMapObject
internal constructor(
        override val player: Player,
        override val modelId: Int,
        override val drawDistance: Float,
        coordinates: Vector3D,
        rotation: Vector3D,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerMapObjectMovedReceiver: OnPlayerMapObjectMovedReceiverDelegate = OnPlayerMapObjectMovedReceiverDelegate(),
        private val onPlayerEditPlayerMapObjectReceiver: OnPlayerEditPlayerMapObjectReceiverDelegate = OnPlayerEditPlayerMapObjectReceiverDelegate(),
        private val onPlayerSelectPlayerMapObjectReceiver: OnPlayerSelectPlayerMapObjectReceiverDelegate = OnPlayerSelectPlayerMapObjectReceiverDelegate()
) : Entity<PlayerMapObjectId>,
        AbstractDestroyable(),
        HasPlayer,
        MapObjectBase,
        OnPlayerMapObjectMovedReceiver by onPlayerMapObjectMovedReceiver,
        OnPlayerEditPlayerMapObjectReceiver by onPlayerEditPlayerMapObjectReceiver,
        OnPlayerSelectPlayerMapObjectReceiver by onPlayerSelectPlayerMapObjectReceiver {

    override val id: PlayerMapObjectId
        get() = requireNotDestroyed { field }

    override var coordinates: Vector3D by PlayerMapObjectCoordinatesProperty(nativeFunctionExecutor)

    override var rotation: Vector3D by PlayerMapObjectRotationProperty(nativeFunctionExecutor)

    override val isMoving: Boolean
        get() = nativeFunctionExecutor.isPlayerObjectMoving(playerid = player.id.value, objectid = id.value)

    override var isDestroyed: Boolean = false
        get() = field || !player.isConnected

    init {
        val playerMapObjectId = nativeFunctionExecutor.createPlayerObject(
                playerid = player.id.value,
                modelid = modelId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                rX = rotation.x,
                rY = rotation.y,
                rZ = rotation.z,
                DrawDistance = drawDistance
        )

        if (playerMapObjectId == SAMPConstants.INVALID_OBJECT_ID) {
            throw CreationFailedException("Could not create map object")
        }

        id = PlayerMapObjectId.valueOf(playerMapObjectId)
    }

    override fun attachTo(player: Player, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachPlayerObjectToPlayer(
                objectid = id.value,
                objectplayer = this.player.id.value,
                attachplayer = player.id.value,
                OffsetX = offset.x,
                OffsetY = offset.y,
                OffsetZ = offset.z,
                rX = rotation.x,
                rY = rotation.y,
                rZ = rotation.z
        )
    }

    fun edit(player: Player) {
        nativeFunctionExecutor.editPlayerObject(playerid = player.id.value, objectid = id.value)
    }

    override fun attachTo(vehicle: Vehicle, offset: Vector3D, rotation: Vector3D) {
        nativeFunctionExecutor.attachPlayerObjectToVehicle(
                playerid = player.id.value,
                objectid = id.value,
                vehicleid = vehicle.id.value,
                fOffsetX = offset.x,
                fOffsetY = offset.y,
                fOffsetZ = offset.z,
                fRotX = rotation.x,
                fRotY = rotation.y,
                RotZ = rotation.z
        )
    }

    override fun disableCameraCollision() {
        nativeFunctionExecutor.setPlayerObjectNoCameraCol(playerid = player.id.value, objectid = id.value)
    }

    override fun moveTo(coordinates: Vector3D, speed: Float, rotation: Vector3D?): Int {
        return nativeFunctionExecutor.movePlayerObject(
                playerid = player.id.value,
                objectid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                Speed = speed,
                RotX = rotation?.x ?: -1000f,
                RotY = rotation?.y ?: -1000f,
                RotZ = rotation?.z ?: -1000f
        )
    }

    override fun stop() {
        nativeFunctionExecutor.stopPlayerObject(playerid = player.id.value, objectid = id.value)
    }

    override fun setMaterial(index: Int, modelId: Int, txdName: String, textureName: String, color: Color) {
        nativeFunctionExecutor.setPlayerObjectMaterial(
                playerid = player.id.value,
                objectid = id.value,
                materialindex = index,
                modelid = modelId,
                materialcolor = color.argb,
                texturename = textureName,
                txdname = txdName
        )
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
        nativeFunctionExecutor.setPlayerObjectMaterialText(
                playerid = player.id.value,
                objectid = id.value,
                materialindex = index,
                text = text,
                materialsize = size.value,
                fontface = fontFace,
                fontsize = fontSize,
                bold = isBold,
                fontcolor = fontColor.argb,
                backcolor = backColor.argb,
                textalignment = textAlignment.value
        )
    }

    internal fun onMoved() {
        onPlayerMapObjectMovedReceiver.onPlayerMapObjectMoved(this)
    }

    internal fun onEdit(response: ObjectEditResponse, offset: Vector3D, rotation: Vector3D) {
        onPlayerEditPlayerMapObjectReceiver.onPlayerEditPlayerMapObject(
                playerMapObject = this,
                response = response,
                offset = offset,
                rotation = rotation
        )
    }

    internal fun onSelect(modelId: Int, coordinates: Vector3D) {
        onPlayerSelectPlayerMapObjectReceiver.onPlayerSelectPlayerMapObject(this, modelId, coordinates)
    }

    override fun onDestroy() {
        nativeFunctionExecutor.destroyPlayerObject(playerid = player.id.value, objectid = id.value)
    }
}