package ch.leadrian.samp.kamp.core.api.data

interface MutableLocation : Location, MutableVector3D {

    override var interiorId: Int

    override var virtualWorldId: Int

    override fun plus(other: Vector2D): MutableLocation

    override fun plus(other: Vector3D): MutableLocation

    override fun minus(other: Vector2D): MutableLocation

    override fun minus(other: Vector3D): MutableLocation

    override fun times(value: Float): MutableLocation

    override fun div(value: Float): MutableLocation

}