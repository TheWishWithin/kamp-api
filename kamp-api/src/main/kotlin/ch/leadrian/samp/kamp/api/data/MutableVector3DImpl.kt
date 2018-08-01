package ch.leadrian.samp.kamp.api.data

internal data class MutableVector3DImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float
) : MutableVector3D {

    override fun toVector3D(): Vector3D = Vector3DImpl(
            x = x,
            y = y,
            z = z
    )

    override fun toMutableVector3D(): MutableVector3D = this

    override fun toVector2D(): Vector2D = Vector2DImpl(
            x = x,
            y = y
    )

    override fun toMutableVector2D(): MutableVector2D = this

    override fun plus(other: Vector2D): MutableVector3D = copy(
            x = this.x + other.x,
            y = this.y + other.y
    )

    override fun plus(other: Vector3D): MutableVector3D = copy(
            x = this.x + other.x,
            y = this.y + other.y,
            z = this.z + other.z
    )

}