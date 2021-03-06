package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VehicleCallbackListenerTest {

    private lateinit var vehicleCallbackListener: VehicleCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        vehicleCallbackListener = VehicleCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        vehicleCallbackListener.initialize()

        verify { callbackListenerManager.register(vehicleCallbackListener) }
    }

    @Test
    fun shouldExecuteOnSpawn() {
        val vehicle = mockk<Vehicle> {
            every { onSpawn() } just Runs
        }

        vehicleCallbackListener.onVehicleSpawn(vehicle)

        verify { vehicle.onSpawn() }
    }

    @Test
    fun shouldExecuteOnDeath() {
        val killer = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onDeath(killer) } just Runs
        }

        vehicleCallbackListener.onVehicleDeath(vehicle, killer)

        verify { vehicle.onDeath(killer) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldExecuteOnEnter(isPassenger: Boolean) {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onEnter(any(), any()) } just Runs
        }

        vehicleCallbackListener.onPlayerEnterVehicle(player, vehicle, isPassenger)

        verify { vehicle.onEnter(player, isPassenger) }
    }

    @Test
    fun shouldExecuteOnExit() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onExit(any()) } just Runs
        }

        vehicleCallbackListener.onPlayerExitVehicle(player, vehicle)

        verify { vehicle.onExit(player) }
    }

    @Test
    fun shouldExecuteOnStreamIn() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onStreamIn(any()) } just Runs
        }

        vehicleCallbackListener.onVehicleStreamIn(vehicle, player)

        verify { vehicle.onStreamIn(player) }
    }

    @Test
    fun shouldExecuteOnStreamOut() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onStreamOut(any()) } just Runs
        }

        vehicleCallbackListener.onVehicleStreamOut(vehicle, player)

        verify { vehicle.onStreamOut(player) }
    }

    @Test
    fun shouldExecuteOnRespray() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onRespray(player, vehicleColorsOf(3, 6)) } returns OnVehicleResprayListener.Result.Sync
        }

        val result = vehicleCallbackListener.onVehicleRespray(player, vehicle, vehicleColorsOf(3, 6))

        assertThat(result)
                .isEqualTo(OnVehicleResprayListener.Result.Sync)
    }

    @Test
    fun shouldExecuteOnPaintjob() {
        val player = mockk<Player>()
        val vehicle = mockk<Vehicle> {
            every { onPaintjobChange(any(), any()) } just Runs
        }

        vehicleCallbackListener.onVehiclePaintjob(player, vehicle, 2)

        verify { vehicle.onPaintjobChange(player, 2) }
    }

}