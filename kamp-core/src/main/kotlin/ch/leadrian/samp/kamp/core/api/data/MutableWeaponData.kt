package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.runtime.data.MutableWeaponDataImpl

interface MutableWeaponData : WeaponData {

    override var model: WeaponModel

    override var ammo: Int

}

fun mutableWeaponDataOf(model: WeaponModel, ammo: Int): MutableWeaponData = MutableWeaponDataImpl(
        model = model,
        ammo = ammo
)