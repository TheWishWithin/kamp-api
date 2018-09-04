package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Menu
import ch.leadrian.samp.kamp.api.entity.id.MenuId
import javax.inject.Singleton

@Singleton
internal class MenuRegistry : EntityRegistry<Menu, MenuId>(arrayOfNulls(SAMPConstants.MAX_MENUS))