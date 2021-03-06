package ch.leadrian.samp.kamp.core.api.data

interface Rectangle : Shape2D {

    val minX: Float

    val maxX: Float

    val minY: Float

    val maxY: Float

    val width: Float

    val height: Float

    fun toRectangle(): Rectangle

    fun toMutableRectangle(): MutableRectangle

    override fun contains(coordinates: Vector2D): Boolean =
            coordinates.x in (minX..maxX) && coordinates.y in (minY..maxY)
}
