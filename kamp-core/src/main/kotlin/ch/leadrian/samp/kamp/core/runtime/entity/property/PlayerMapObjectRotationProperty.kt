package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class PlayerMapObjectRotationProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<PlayerMapObject, Vector3D> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: PlayerMapObject, property: KProperty<*>): Vector3D {
        nativeFunctionExecutor.getPlayerObjectRot(
                playerid = thisRef.player.id.value,
                objectid = thisRef.id.value,
                rotX = x,
                rotY = y,
                rotZ = z
        )
        return vector3DOf(x = x.value, y = y.value, z = z.value)
    }

    override fun setValue(thisRef: PlayerMapObject, property: KProperty<*>, value: Vector3D) {
        nativeFunctionExecutor.setPlayerObjectRot(
                playerid = thisRef.player.id.value,
                objectid = thisRef.id.value,
                rotX = value.x,
                rotY = value.y,
                rotZ = value.z
        )
    }

}