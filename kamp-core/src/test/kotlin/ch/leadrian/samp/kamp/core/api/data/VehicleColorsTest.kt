package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VehicleColorsTest {

    @Test
    fun toVehicleColorsShouldReturnSameInstance() {
        val expectedVehicleColors = vehicleColorsOf(
                color1 = VehicleColor[65],
                color2 = VehicleColor[127]
        )

        val vehicleColors: VehicleColors = expectedVehicleColors.toVehicleColors()

        assertThat(vehicleColors)
                .isNotInstanceOf(MutableVehicleColors::class.java)
                .isSameAs(expectedVehicleColors)
    }

    @Test
    fun toMutableVehicleColorsShouldReturnMutableInstance() {
        val vehicleColors = vehicleColorsOf(
                color1 = VehicleColor[65],
                color2 = VehicleColor[127]
        )

        val mutableVehicleColors: MutableVehicleColors = vehicleColors.toMutableVehicleColors()

        assertThat(mutableVehicleColors)
                .satisfies {
                    assertThat(it.color1)
                            .isEqualTo(VehicleColor[65])
                    assertThat(it.color2)
                            .isEqualTo(VehicleColor[127])
                }
    }

    @Test
    fun shouldCreateVehicleColorsWithIntColors() {
        val vehicleColors = vehicleColorsOf(color1 = 65, color2 = 127)

        assertThat(vehicleColors)
                .isEqualTo(
                        vehicleColorsOf(
                                color1 = VehicleColor[65],
                                color2 = VehicleColor[127]
                        )
                )
    }
}