package ch.leadrian.samp.kamp.api.data

internal data class LocationImpl(
        override val x: Float,
        override val y: Float,
        override val z: Float,
        override val interiorId: Int,
        override val worldId: Int
) : Location {

    override fun toLocation(): Location = this

    override fun toMutableLocation(): MutableLocation = MutableLocationImpl(
            x = x,
            y = y,
            z = z,
            interiorId = interiorId,
            worldId = worldId
    )

    override fun toVector3D(): Vector3D = this

    override fun toMutableVector3D(): MutableVector3D = MutableVector3DImpl(
            x = x,
            y = y,
            z = z
    )

    override fun toVector2D(): Vector2D = this

    override fun toMutableVector2D(): MutableVector2D = MutableVector2DImpl(
            x = x,
            y = y
    )
}