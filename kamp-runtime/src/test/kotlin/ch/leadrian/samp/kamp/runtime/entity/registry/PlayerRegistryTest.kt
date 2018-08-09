package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PlayerRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_PLAYERS - 1])
    fun shouldRegisterAndGetPlayer(playerId: Int) {
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val playerRegistry = PlayerRegistry()

        playerRegistry.register(player)

        val registeredPlayer = playerRegistry.getPlayer(playerId)
        assertThat(registeredPlayer)
                .isSameAs(player)
    }

    @Test
    fun givenAnotherPlayerWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val playerId = 50
        val alreadyRegisteredPlayer = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val newPlayer = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val playerRegistry = PlayerRegistry()
        playerRegistry.register(alreadyRegisteredPlayer)

        val caughtThrowable = catchThrowable { playerRegistry.register(newPlayer) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayer = playerRegistry.getPlayer(playerId)
        assertThat(registeredPlayer)
                .isSameAs(alreadyRegisteredPlayer)
    }

    @Test
    fun shouldUnregisterRegisteredPlayer() {
        val playerId = 50
        val player = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val playerRegistry = PlayerRegistry()
        playerRegistry.register(player)

        playerRegistry.unregister(player)

        val registeredPlayer = playerRegistry.getPlayer(playerId)
        assertThat(registeredPlayer)
                .isNull()
    }


    @Test
    fun givenPlayerIsNotRegisteredItShouldThrowAnException() {
        val playerId = PlayerId.valueOf(50)
        val player = mockk<Player> {
            every { id } returns playerId
        }
        val playerRegistry = PlayerRegistry()

        val caughtThrowable = catchThrowable { playerRegistry.unregister(player) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherPlayerWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val playerId = 50
        val alreadyRegisteredPlayer = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val newPlayer = mockk<Player> {
            every { id } returns PlayerId.valueOf(playerId)
        }
        val playerRegistry = PlayerRegistry()
        playerRegistry.register(alreadyRegisteredPlayer)

        val caughtThrowable = catchThrowable { playerRegistry.unregister(newPlayer) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredPlayer = playerRegistry.getPlayer(playerId)
        assertThat(registeredPlayer)
                .isSameAs(alreadyRegisteredPlayer)
    }

    @Test
    fun shouldReturnAllRegisteredPlayers() {
        val playerId1 = PlayerId.valueOf(10)
        val player1 = mockk<Player> {
            every { id } returns playerId1
        }
        val playerId2 = PlayerId.valueOf(15)
        val player2 = mockk<Player> {
            every { id } returns playerId2
        }
        val playerId3 = PlayerId.valueOf(30)
        val player3 = mockk<Player> {
            every { id } returns playerId3
        }
        val playerRegistry = PlayerRegistry()
        playerRegistry.register(player1)
        playerRegistry.register(player2)
        playerRegistry.register(player3)

        val allPlayers = playerRegistry.getAllPlayers()

        assertThat(allPlayers)
                .containsExactly(player1, player2, player3)
    }

}