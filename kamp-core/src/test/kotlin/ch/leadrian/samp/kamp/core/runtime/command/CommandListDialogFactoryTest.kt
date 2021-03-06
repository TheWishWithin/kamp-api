package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandGroup
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.Server
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.TabListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.extension.EntityExtensionModule
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistryModule
import ch.leadrian.samp.kamp.core.runtime.inject.InjectorFactory
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import com.netflix.governator.guice.BootstrapModule
import com.netflix.governator.lifecycle.LifecycleManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale
import javax.inject.Singleton

internal class CommandListDialogFactoryTest {

    private lateinit var commandListDialogFactory: CommandListDialogFactory

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private lateinit var commands: TestCommands
    private lateinit var groupedCommands: TestGroupedCommands

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.getMaxPlayers() } returns 128
        val injector = InjectorFactory.createInjector(
                setOf(),
                BootstrapModule { },
                TestModule(),
                TextModule(),
                EntityRegistryModule(),
                CommandModule(),
                CallbackModule(),
                EntityExtensionModule()
        )
        val lifecycleManager = injector.getInstance<LifecycleManager>()
        lifecycleManager.start()
        commandListDialogFactory = injector.getInstance()
        commands = injector.getInstance()
        groupedCommands = injector.getInstance()
    }

    @Nested
    inner class SinglePageListTests {

        @Test
        fun shouldCreateDialog() {
            val caughtThrowable = catchThrowable { commandListDialogFactory.create(commands, 10) }

            assertThat(caughtThrowable)
                    .isNull()
        }

        @Test
        fun shouldShowDialog() {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
                every { id } returns PlayerId.valueOf(69)
                every { showDialog(any(), any(), any(), any(), any(), any()) } just Runs
            }
            val dialog = commandListDialogFactory.create(commands, 10)

            dialog.show(player)

            verify {
                player.showDialog(
                        dialog = dialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Schliessen",
                        button2 = "",
                        caption = "Befehle",
                        message = "Befehl\tParameter\n" +
                                "bar\t[int] [String] [List] \n" +
                                "batman\t[WeaponModel] \n" +
                                "baz\t[Bla] [Blub] \n" +
                                "foo\t\n" +
                                "qux\t[String] [Param 2] \n"
                )
            }
        }

        @Test
        fun givenCommandGroupItShouldShowDialog() {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
                every { id } returns PlayerId.valueOf(69)
                every { showDialog(any(), any(), any(), any(), any(), any()) } just Runs
            }
            val dialog = commandListDialogFactory.create(groupedCommands, 10)

            dialog.show(player)

            verify {
                player.showDialog(
                        dialog = dialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Schliessen",
                        button2 = "",
                        caption = "Befehle",
                        message = "Befehl\tParameter\n" +
                                "funny haha\t[int] [String] [List] \n" +
                                "funny lol\t\n"
                )
            }
        }
    }

    @Nested
    inner class PagedListTests {

        @Test
        fun shouldCreateDialog() {
            val caughtThrowable = catchThrowable { commandListDialogFactory.create(commands, 2) }

            assertThat(caughtThrowable)
                    .isNull()
        }

        @Test
        fun shouldShowDialog() {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
                every { id } returns PlayerId.valueOf(69)
                every { showDialog(any(), any(), any(), any(), any(), any()) } just Runs
            }
            val dialog = commandListDialogFactory.create(commands, 2)

            dialog.show(player)

            verify {
                player.showDialog(
                        dialog = dialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Weiter",
                        button2 = "Schliessen",
                        caption = "Befehle (1/3)",
                        message = "Befehl\tParameter\n" +
                                "bar\t[int] [String] [List] \n" +
                                "batman\t[WeaponModel] \n"
                )
            }
        }

        @Suppress("UNCHECKED_CAST")
        @Test
        fun shouldNavigateForward() {
            val player = mockk<Player> {
                every { locale } returns Locale.GERMANY
                every { id } returns PlayerId.valueOf(69)
                every { showDialog(any(), any(), any(), any(), any(), any()) } just Runs
            }
            val dialogNavigation = DialogNavigation(player)
            every { player.dialogNavigation } returns dialogNavigation
            val dialog = commandListDialogFactory.create(commands, 2)

            dialog.show(player)
            (dialog as TabListDialog<String>).onResponse(player, DialogResponse.LEFT_BUTTON, 0, "")
            (dialogNavigation.top as TabListDialog<String>).onResponse(player, DialogResponse.LEFT_BUTTON, 0, "")
            (dialogNavigation.top as TabListDialog<String>).onResponse(player, DialogResponse.LEFT_BUTTON, 0, "")

            verifyOrder {
                player.showDialog(
                        dialog = match { it.id.value == 1 },
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Weiter",
                        button2 = "Schliessen",
                        caption = "Befehle (1/3)",
                        message = "Befehl\tParameter\n" +
                                "bar\t[int] [String] [List] \n" +
                                "batman\t[WeaponModel] \n"
                )
                player.showDialog(
                        dialog = match { it.id.value == 2 },
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Weiter",
                        button2 = "Zurück",
                        caption = "Befehle (2/3)",
                        message = "Befehl\tParameter\n" +
                                "baz\t[Bla] [Blub] \n" +
                                "foo\t\n"
                )
                player.showDialog(
                        dialog = match { it.id.value == 3 },
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "Schliessen",
                        button2 = "Zurück",
                        caption = "Befehle (3/3)",
                        message = "Befehl\tParameter\n" +
                                "qux\t[String] [Param 2] \n"
                )
            }
        }
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    @Singleton
    private class TestCommands : Commands() {

        @Command
        fun foo(player: Player) {
        }

        @Command
        fun bar(player: Player, intParam: Int, stringParam: String, listParam: List<String>) {
        }

        @Command
        fun baz(player: Player, @Parameter("Bla") bla: Float, @Parameter("Blub") blub: Long) {
        }

        @Command
        fun qux(player: Player, param1: String, @Parameter("Param 2") param2: Int) {
        }

        @Command("batman")
        fun bat(player: Player, weapon: WeaponModel) {
        }

        @Unlisted
        @Command
        fun hideMe(player: Player) {
        }

    }

    @Suppress("unused", "UNUSED_PARAMETER")
    @Singleton
    @CommandGroup("funny")
    private class TestGroupedCommands : Commands() {

        @Command
        fun lol(player: Player) {
        }

        @Command
        fun haha(player: Player, intParam: Int, stringParam: String, listParam: List<String>) {
        }

    }

    private inner class TestModule : KampModule() {

        override fun configure() {
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
            bind(Server::class.java).toInstance(mockk())
            bind(TestCommands::class.java)
            bind(TestGroupedCommands::class.java)
            newCommandsSetBinder().apply {
                addBinding().to(TestCommands::class.java)
                addBinding().to(TestGroupedCommands::class.java)
            }
        }

    }
}