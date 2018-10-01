package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.inject.KampModule

internal class CallbackModule : KampModule() {

    override fun configure() {
        bind(CheckpointCallbackListener::class.java).asEagerSingleton()
        bind(DialogCallbackListener::class.java).asEagerSingleton()
        bind(MenuCallbackListener::class.java).asEagerSingleton()

        bind(CallbackListenerManager::class.java)
        newCallbackListenerRegistry().apply {
            addBinding().to(OnActorStreamInHandler::class.java)
            addBinding().to(OnActorStreamOutHandler::class.java)
            addBinding().to(OnDialogResponseHandler::class.java)
            addBinding().to(OnEnterExitModShopHandler::class.java)
            addBinding().to(OnGameModeExitHandler::class.java)
            addBinding().to(OnGameModeInitHandler::class.java)
            addBinding().to(OnIncomingConnectionHandler::class.java)
            addBinding().to(OnObjectMovedHandler::class.java)
            addBinding().to(OnPlayerClickMapHandler::class.java)
            addBinding().to(OnPlayerClickPlayerHandler::class.java)
            addBinding().to(OnPlayerClickPlayerTextDrawHandler::class.java)
            addBinding().to(OnPlayerClickTextDrawHandler::class.java)
            addBinding().to(OnPlayerCommandTextHandler::class.java)
            addBinding().to(OnPlayerConnectHandler::class.java)
            addBinding().to(OnPlayerDeathHandler::class.java)
            addBinding().to(OnPlayerDisconnectHandler::class.java)
            addBinding().to(OnPlayerEditAttachedObjectHandler::class.java)
            addBinding().to(OnPlayerEditMapObjectHandler::class.java)
            addBinding().to(OnPlayerEditPlayerMapObjectHandler::class.java)
            addBinding().to(OnPlayerEnterCheckpointHandler::class.java)
            addBinding().to(OnPlayerEnterRaceCheckpointHandler::class.java)
            addBinding().to(OnPlayerEnterVehicleHandler::class.java)
            addBinding().to(OnPlayerExitedMenuHandler::class.java)
            addBinding().to(OnPlayerExitVehicleHandler::class.java)
            addBinding().to(OnPlayerGiveDamageActorHandler::class.java)
            addBinding().to(OnPlayerGiveDamageHandler::class.java)
            addBinding().to(OnPlayerInteriorChangeHandler::class.java)
            addBinding().to(OnPlayerKeyStateChangeHandler::class.java)
            addBinding().to(OnPlayerLeaveCheckpointHandler::class.java)
            addBinding().to(OnPlayerLeaveRaceCheckpointHandler::class.java)
            addBinding().to(OnPlayerObjectMovedHandler::class.java)
            addBinding().to(OnPlayerPickUpPickupHandler::class.java)
            addBinding().to(OnPlayerRequestClassHandler::class.java)
            addBinding().to(OnPlayerRequestSpawnHandler::class.java)
            addBinding().to(OnPlayerSelectedMenuRowHandler::class.java)
            addBinding().to(OnPlayerSelectMapObjectHandler::class.java)
            addBinding().to(OnPlayerSelectPlayerMapObjectHandler::class.java)
            addBinding().to(OnPlayerSpawnHandler::class.java)
            addBinding().to(OnPlayerStateChangeHandler::class.java)
            addBinding().to(OnPlayerStreamInHandler::class.java)
            addBinding().to(OnPlayerStreamOutHandler::class.java)
            addBinding().to(OnPlayerTakeDamageHandler::class.java)
            addBinding().to(OnPlayerTextHandler::class.java)
            addBinding().to(OnPlayerUpdateHandler::class.java)
            addBinding().to(OnPlayerWeaponShotHandler::class.java)
            addBinding().to(OnProcessTickHandler::class.java)
            addBinding().to(OnRconCommandHandler::class.java)
            addBinding().to(OnRconLoginAttemptHandler::class.java)
            addBinding().to(OnTrailerUpdateHandler::class.java)
            addBinding().to(OnUnoccupiedVehicleUpdateHandler::class.java)
            addBinding().to(OnVehicleDamageStatusUpdateHandler::class.java)
            addBinding().to(OnVehicleDeathHandler::class.java)
            addBinding().to(OnVehicleModHandler::class.java)
            addBinding().to(OnVehiclePaintjobHandler::class.java)
            addBinding().to(OnVehicleResprayHandler::class.java)
            addBinding().to(OnVehicleSirenStateChangeHandler::class.java)
            addBinding().to(OnVehicleSpawnHandler::class.java)
            addBinding().to(OnVehicleStreamInHandler::class.java)
            addBinding().to(OnVehicleStreamOutHandler::class.java)
        }
    }
}