package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidator
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.Locale

internal class InputDialogTest {

    private lateinit var builder: InputDialog.Builder

    private val textProvider = mockk<TextProvider>()
    private val dialogRegistry = mockk<DialogRegistry>()
    private val playerId = PlayerId.valueOf(50)
    private val player = mockk<Player>()
    private val locale = Locale.GERMANY
    private val dialogId = DialogId.valueOf(69)

    @BeforeEach
    fun setUp() {
        every { dialogRegistry.register<AbstractDialog>(any()) } answers {
            firstArg<(DialogId) -> AbstractDialog>().invoke(dialogId)
        }
        every { player.locale } returns locale
        every { player.id } returns playerId
        builder = InputDialog.Builder(textProvider, dialogRegistry)
    }

    @Nested
    inner class ShowTests {

        @BeforeEach
        fun setUp() {
            every { player.showDialog(any(), any(), any(), any(), any(), any()) } just Runs
        }

        @Test
        fun shouldBuildDialogWithStringDialogTextSuppliers() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = DialogStyle.INPUT,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithTextKeyDialogTextSuppliers() {
            val captionTextKey = TextKey("test.caption")
            val leftButtonTextKey = TextKey("test.left.button")
            val rightButtonTextKey = TextKey("test.right.button")
            val messageTextKey = TextKey("test.message")
            textProvider.apply {
                every { getText(locale, captionTextKey) } returns "Hi there"
                every { getText(locale, leftButtonTextKey) } returns "OK"
                every { getText(locale, rightButtonTextKey) } returns "Cancel"
                every { getText(locale, messageTextKey) } returns "How are you?"
            }
            val inputDialog = builder.apply {
                caption(captionTextKey)
                leftButton(leftButtonTextKey)
                rightButton(rightButtonTextKey)
                message(messageTextKey)
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = DialogStyle.INPUT,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithFunctionalDialogTextSuppliers() {
            val inputDialog = builder.apply {
                caption { "Hi there" }
                leftButton { "OK" }
                rightButton { "Cancel" }
                message { "How are you?" }
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = DialogStyle.INPUT,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithSuppliedDialogTextSuppliers() {
            val inputDialog = builder.apply {
                caption(StringDialogTextSupplier("Hi there"))
                leftButton(StringDialogTextSupplier("OK"))
                rightButton(StringDialogTextSupplier("Cancel"))
                message(StringDialogTextSupplier("How are you?"))
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = DialogStyle.INPUT,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @ParameterizedTest
        @CsvSource(
                "true, PASSWORD",
                "false, INPUT"
        )
        fun shouldBuildDialogWithFixedIsPasswordInputValue(isPasswordInput: Boolean, style: DialogStyle) {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                isPasswordInput(isPasswordInput)
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = style,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @ParameterizedTest
        @CsvSource(
                "true, PASSWORD",
                "false, INPUT"
        )
        fun shouldBuildDialogWithProvidedIsPasswordInputValue(isPasswordInput: Boolean, style: DialogStyle) {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                isPasswordInput { isPasswordInput }
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = style,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?"
                )
            }
        }

        @Test
        fun givenNoMessageItShouldShowWithBlankMessageAsString() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
            }.build()

            inputDialog.show(player)

            verify {
                player.showDialog(
                        dialog = inputDialog,
                        style = DialogStyle.INPUT,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = " "
                )
            }
        }
    }

    @Nested
    inner class OnResponseTests {

        private val onCancel = mockk<Dialog.(Player) -> OnDialogResponseListener.Result>()
        private val onSubmit = mockk<Dialog.(Player, String) -> Unit>()
        private val onInvalidInput = mockk<Dialog.(Player, Any) -> Unit>()

        @BeforeEach
        fun setUp() {
            every { onCancel.invoke(any(), any()) } returns OnDialogResponseListener.Result.Processed
            every { onSubmit.invoke(any(), any(), any()) } returns Unit
            every { onInvalidInput.invoke(any(), any(), any()) } returns Unit
        }

        @Test
        fun givenResponseIsRightButtonItShouldCancel() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onSubmit(onSubmit)
                onInvalidInput(onInvalidInput)
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "test")

            verify {
                onCancel.invoke(inputDialog, player)
                onSubmit wasNot Called
                onInvalidInput wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsRightButtonAndNoCancelCallbackItShouldDoNothing() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onSubmit(onSubmit)
                onInvalidInput(onInvalidInput)
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit wasNot Called
                onInvalidInput wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Ignored)
        }

        @Test
        fun givenResponseIsLeftButtonAndNoValidatorsItShouldSubmit() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onSubmit(onSubmit)
                onInvalidInput(onInvalidInput)
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit.invoke(inputDialog, player, "test")
                onInvalidInput wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsLeftButtonAndNoValidatorsAndNoOnSubmitItShouldDoNothing() {
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onInvalidInput(onInvalidInput)
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit wasNot Called
                onInvalidInput wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsLeftButtonAndAllValidatorsReturnNullItShouldSubmit() {
            val validator1 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator2 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator3 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator4 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onSubmit(onSubmit)
                onInvalidInput(onInvalidInput)
                validator(validator1)
                validators(validator2, validator3)
                validators(listOf(validator4))
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit.invoke(inputDialog, player, "test")
                onInvalidInput wasNot Called
                validator1.validate(player, "test")
                validator2.validate(player, "test")
                validator3.validate(player, "test")
                validator4.validate(player, "test")
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsLeftButtonAndAllValidatorsReturnNullAndNoOnSubmitItShouldDoNothing() {
            val validator1 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator2 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator3 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator4 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onInvalidInput(onInvalidInput)
                validator(validator1)
                validators(validator2, validator3)
                validators(listOf(validator4))
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit wasNot Called
                onInvalidInput wasNot Called
                validator1.validate(player, "test")
                validator2.validate(player, "test")
                validator3.validate(player, "test")
                validator4.validate(player, "test")
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenValidationErrorItShouldCallOnInvalidInput() {
            val validator1 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator2 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator3 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns "ERROR!!!"
            }
            val validator4 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onCancel(onCancel)
                onSubmit(onSubmit)
                onInvalidInput(onInvalidInput)
                validator(validator1)
                validators(validator2, validator3)
                validators(listOf(validator4))
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit wasNot Called
                onInvalidInput.invoke(inputDialog, player, "ERROR!!!")
                validator1.validate(player, "test")
                validator2.validate(player, "test")
                validator3.validate(player, "test")
                validator4 wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenValidationErrorAndNoOnInvalidInputItShouldDoNothing() {
            val validator1 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator2 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val validator3 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns "ERROR!!!"
            }
            val validator4 = mockk<DialogInputValidator> {
                every { validate(any(), any()) } returns null
            }
            val inputDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                isPasswordInput(false)
                onCancel(onCancel)
                onSubmit(onSubmit)
                validator(validator1)
                validators(validator2, validator3)
                validators(listOf(validator4))
            }.build()

            val result = inputDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "test")

            verify {
                onCancel wasNot Called
                onSubmit wasNot Called
                onInvalidInput wasNot Called
                validator1.validate(player, "test")
                validator2.validate(player, "test")
                validator3.validate(player, "test")
                validator4 wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }
    }

}