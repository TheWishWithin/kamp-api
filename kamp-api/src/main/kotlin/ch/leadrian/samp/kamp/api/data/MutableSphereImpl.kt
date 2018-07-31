package ch.leadrian.samp.kamp.api.data

internal data class MutableSphereImpl(
        override var x: Float,
        override var y: Float,
        override var z: Float,
        override var radius: Float
) : MutableSphere {

    override val volume: Float
        get() {
            val d = 2 * radius
            return (Math.PI.toFloat() * (d * d * d)) / 6f
        }

    override fun toSphere(): Sphere = SphereImpl(
            x = x,
            y = y,
            z = z,
            radius = radius
    )

    override fun toMutableSphere(): MutableSphere = this
}