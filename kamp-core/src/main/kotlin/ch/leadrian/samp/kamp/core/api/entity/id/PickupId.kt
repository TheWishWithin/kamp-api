package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class PickupId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PickupId(-1)

        private val pickupIds: Array<PickupId> = (0 until SAMPConstants.MAX_PICKUPS).map { PickupId(it) }.toTypedArray()

        fun valueOf(value: Int): PickupId =
                when {
                    0 <= value && value < pickupIds.size -> pickupIds[value]
                    value == INVALID.value -> INVALID
                    else -> PickupId(value)
                }
    }
}