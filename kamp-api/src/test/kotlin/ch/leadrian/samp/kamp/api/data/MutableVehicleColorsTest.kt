package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MutableVehicleColorsTest {

    @Test
    fun toMutableVehicleColorsShouldReturnSameInstance() {
        val expectedMutableVehicleColors = mutableVehicleColorsOf(
                color1 = VehicleColor[65],
                color2 = VehicleColor[127]
        )

        val mutableVehicleColors: VehicleColors = expectedMutableVehicleColors.toMutableVehicleColors()

        assertThat(mutableVehicleColors)
                .isSameAs(expectedMutableVehicleColors)
    }

    @Test
    fun toVehicleColorsShouldReturnImmutableInstance() {
        val mutableVehicleColors = mutableVehicleColorsOf(
                color1 = VehicleColor[65],
                color2 = VehicleColor[127]
        )

        val vehicleColors: VehicleColors = mutableVehicleColors.toVehicleColors()

        assertThat(vehicleColors)
                .isNotInstanceOf(MutableVehicleColors::class.java)
                .satisfies {
                    assertThat(it.color1)
                            .isEqualTo(VehicleColor[65])
                    assertThat(it.color2)
                            .isEqualTo(VehicleColor[127])
                }
    }
}