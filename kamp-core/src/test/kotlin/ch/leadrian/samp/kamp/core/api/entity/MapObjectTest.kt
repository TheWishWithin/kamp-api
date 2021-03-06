package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.id.MapObjectId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnMapObjectMovedReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerEditMapObjectReceiverDelegate
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerSelectMapObjectReceiverDelegate
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MapObjectTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructMapObject() {
            val mapObjectId = MapObjectId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createObject(
                            modelid = 1337,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rX = 4f,
                            rY = 5f,
                            rZ = 6f,
                            DrawDistance = 7f
                    )
                } returns mapObjectId.value
            }

            val mapObject = MapObject(
                    modelId = 1337,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    drawDistance = 7f,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )

            assertThat(mapObject.id)
                    .isEqualTo(mapObjectId)
        }

        @Test
        fun givenCreateMapObjectReturnsInvalidMapObjectIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createObject(
                            modelid = 1337,
                            x = 1f,
                            y = 2f,
                            z = 3f,
                            rX = 4f,
                            rY = 5f,
                            rZ = 6f,
                            DrawDistance = 7f
                    )
                } returns SAMPConstants.INVALID_OBJECT_ID
            }

            val caughtThrowable = catchThrowable {
                MapObject(
                        modelId = 1337,
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                        drawDistance = 7f,
                        nativeFunctionExecutor = nativeFunctionExecutor
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val mapObjectId = MapObjectId.valueOf(50)
        private lateinit var mapObject: MapObject
        private val onMapObjectMovedReceiver = mockk<OnMapObjectMovedReceiverDelegate>()
        private val onPlayerEditMapObjectReceiver = mockk<OnPlayerEditMapObjectReceiverDelegate>()
        private val onPlayerSelectMapObjectReceiver = mockk<OnPlayerSelectMapObjectReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.createObject(any(), any(), any(), any(), any(), any(), any(), any())
            } returns mapObjectId.value
            mapObject = MapObject(
                    modelId = 1337,
                    coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    drawDistance = 7f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    onMapObjectMovedReceiver = onMapObjectMovedReceiver,
                    onPlayerEditMapObjectReceiver = onPlayerEditMapObjectReceiver,
                    onPlayerSelectMapObjectReceiver = onPlayerSelectMapObjectReceiver
            )
        }

        @Test
        fun shouldEdit() {
            val playerId = PlayerId.valueOf(69)
            val player = mockk<Player> {
                every { id } returns playerId
            }
            every { nativeFunctionExecutor.editObject(any(), any()) } returns true

            mapObject.edit(player)

            verify { nativeFunctionExecutor.editObject(playerid = playerId.value, objectid = mapObjectId.value) }
        }

        @Test
        fun shouldAttachToPlayer() {
            every {
                nativeFunctionExecutor.attachObjectToPlayer(any(), any(), any(), any(), any(), any(), any(), any())
            } returns true
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(69)
            }

            mapObject.attachTo(
                    player = player,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                nativeFunctionExecutor.attachObjectToPlayer(
                        objectid = mapObjectId.value,
                        playerid = 69,
                        fOffsetX = 1f,
                        fOffsetY = 2f,
                        fOffsetZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f
                )
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldAttachToMapObject(syncRotation: Boolean) {
            every {
                nativeFunctionExecutor.attachObjectToObject(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true
            val otherMapObject = mockk<MapObject> {
                every { id } returns MapObjectId.valueOf(69)
            }

            mapObject.attachTo(
                    mapObject = otherMapObject,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                    syncRotation = syncRotation
            )

            verify {
                nativeFunctionExecutor.attachObjectToObject(
                        objectid = mapObjectId.value,
                        attachtoid = 69,
                        fOffsetX = 1f,
                        fOffsetY = 2f,
                        fOffsetZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f,
                        SyncRotation = syncRotation
                )
            }
        }

        @Test
        fun shouldAttachToVehicle() {
            every {
                nativeFunctionExecutor.attachObjectToVehicle(any(), any(), any(), any(), any(), any(), any(), any())
            } returns true
            val vehicle = mockk<Vehicle> {
                every { id } returns VehicleId.valueOf(69)
            }

            mapObject.attachTo(
                    vehicle = vehicle,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                nativeFunctionExecutor.attachObjectToVehicle(
                        objectid = mapObjectId.value,
                        vehicleid = 69,
                        fOffsetX = 1f,
                        fOffsetY = 2f,
                        fOffsetZ = 3f,
                        fRotX = 4f,
                        fRotY = 5f,
                        fRotZ = 6f
                )
            }
        }

        @Test
        fun shouldDisableCameraCollisions() {
            every { nativeFunctionExecutor.setObjectNoCameraCol(any()) } returns true

            mapObject.disableCameraCollision()

            verify { nativeFunctionExecutor.setObjectNoCameraCol(mapObjectId.value) }
        }

        @Nested
        inner class MoveToTests {

            @Test
            fun shouldMoveObject() {
                every {
                    nativeFunctionExecutor.moveObject(
                            objectid = mapObjectId.value,
                            X = 1f,
                            Y = 2f,
                            Z = 3f,
                            RotX = 4f,
                            RotY = 5f,
                            RotZ = 6f,
                            Speed = 7f
                    )
                } returns 150

                val result = mapObject.moveTo(
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        rotation = vector3DOf(x = 4f, y = 5f, z = 6f),
                        speed = 7f
                )

                assertThat(result)
                        .isEqualTo(150)
            }

            @Test
            fun givenNoRotationItShouldUseFallbackValues() {
                every {
                    nativeFunctionExecutor.moveObject(
                            objectid = mapObjectId.value,
                            X = 1f,
                            Y = 2f,
                            Z = 3f,
                            RotX = -1000f,
                            RotY = -1000f,
                            RotZ = -1000f,
                            Speed = 7f
                    )
                } returns 150

                val result = mapObject.moveTo(
                        coordinates = vector3DOf(x = 1f, y = 2f, z = 3f),
                        speed = 7f
                )

                assertThat(result)
                        .isEqualTo(150)
            }
        }

        @Test
        fun shouldStopObject() {
            every { nativeFunctionExecutor.stopObject(any()) } returns true

            mapObject.stop()

            verify { nativeFunctionExecutor.stopObject(mapObjectId.value) }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun isMovingShouldReturnExpectedValue(expectedResult: Boolean) {
            every { nativeFunctionExecutor.isObjectMoving(mapObjectId.value) } returns expectedResult

            val result = mapObject.isMoving

            assertThat(result)
                    .isEqualTo(expectedResult)
        }

        @Test
        fun shouldSetMaterial() {
            every { nativeFunctionExecutor.setObjectMaterial(any(), any(), any(), any(), any(), any()) } returns true

            mapObject.setMaterial(
                    index = 187,
                    modelId = 1337,
                    color = colorOf(0x11223344),
                    textureName = "texture A",
                    txdName = "txd A"
            )

            verify {
                nativeFunctionExecutor.setObjectMaterial(
                        objectid = mapObjectId.value,
                        materialindex = 187,
                        modelid = 1337,
                        materialcolor = 0x44112233,
                        texturename = "texture A",
                        txdname = "txd A"
                )
            }
        }

        @ParameterizedTest
        @ValueSource(strings = ["true", "false"])
        fun shouldSetMaterialText(isBold: Boolean) {
            every {
                nativeFunctionExecutor.setObjectMaterialText(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true

            mapObject.setMaterialText(
                    text = "hi",
                    index = 187,
                    size = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize.SIZE_256X128,
                    fontSize = 16,
                    textAlignment = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment.CENTER,
                    fontFace = "Comic Sans",
                    isBold = isBold,
                    fontColor = colorOf(0x11223344),
                    backColor = colorOf(0x55667788)
            )

            verify {
                nativeFunctionExecutor.setObjectMaterialText(
                        objectid = mapObjectId.value,
                        text = "hi",
                        materialindex = 187,
                        materialsize = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialSize.SIZE_256X128.value,
                        fontsize = 16,
                        textalignment = ch.leadrian.samp.kamp.core.api.constants.ObjectMaterialTextAlignment.CENTER.value,
                        fontface = "Comic Sans",
                        bold = isBold,
                        fontcolor = 0x44112233,
                        backcolor = 0x88556677.toInt()
                )
            }
        }

        @Test
        fun shouldCallOnMapObjectMovedReceiverDelegate() {
            every { onMapObjectMovedReceiver.onMapObjectMoved(any()) } just Runs

            mapObject.onMoved()

            verify { onMapObjectMovedReceiver.onMapObjectMoved(mapObject) }
        }

        @Test
        fun shouldCallOnPlayerEditMapObjectReceiverDelegate() {
            val player = mockk<Player>()
            every { onPlayerEditMapObjectReceiver.onPlayerEditMapObject(any(), any(), any(), any(), any()) } just Runs

            mapObject.onEdit(
                    player = player,
                    response = ObjectEditResponse.UPDATE,
                    offset = vector3DOf(x = 1f, y = 2f, z = 3f),
                    rotation = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                onPlayerEditMapObjectReceiver.onPlayerEditMapObject(
                        player,
                        mapObject,
                        ObjectEditResponse.UPDATE,
                        vector3DOf(x = 1f, y = 2f, z = 3f),
                        vector3DOf(x = 4f, y = 5f, z = 6f)
                )
            }
        }

        @Test
        fun shouldCallOnPlayerSelectMapObjectReceiverDelegate() {
            val player = mockk<Player>()
            every { onPlayerSelectMapObjectReceiver.onPlayerSelectMapObject(any(), any(), any(), any()) } just Runs

            mapObject.onSelect(
                    player = player,
                    modelId = 1337,
                    coordinates = vector3DOf(x = 4f, y = 5f, z = 6f)
            )

            verify {
                onPlayerSelectMapObjectReceiver.onPlayerSelectMapObject(
                        player,
                        mapObject,
                        1337,
                        vector3DOf(x = 4f, y = 5f, z = 6f)
                )
            }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.destroyObject(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = mapObject.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyMapObject() {
                val onDestroy = mockk<MapObject.() -> Unit>(relaxed = true)
                mapObject.onDestroy(onDestroy)

                mapObject.destroy()

                verifyOrder {
                    nativeFunctionExecutor.destroyObject(mapObjectId.value)
                    onDestroy.invoke(mapObject)
                }
                assertThat(mapObject.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<MapObject.() -> Unit>(relaxed = true)
                mapObject.onDestroy(onDestroy)

                mapObject.destroy()
                mapObject.destroy()

                verify(exactly = 1) {
                    onDestroy.invoke(mapObject)
                    nativeFunctionExecutor.destroyObject(mapObjectId.value)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                mapObject.destroy()

                val caughtThrowable = catchThrowable { mapObject.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }
}