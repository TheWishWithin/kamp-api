package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleParamsType(override val value: Int) : ConstantValue<Int> {
    UNSET(SAMPConstants.VEHICLE_PARAMS_UNSET),
    OFF(SAMPConstants.VEHICLE_PARAMS_OFF),
    ON(SAMPConstants.VEHICLE_PARAMS_ON);

    companion object : ConstantValueRegistry<Int, VehicleParamsType>(VehicleParamsType.values())

}
