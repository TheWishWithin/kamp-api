package ch.leadrian.samp.kamp.api.data

interface MutablePosition : Position, MutableVector3D {

    override var angle: Float

}