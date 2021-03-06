package ch.leadrian.samp.kamp.core.api.constants

enum class ObjectMaterialSize(override val value: Int) : ConstantValue<Int> {
    SIZE_32X32(SAMPConstants.OBJECT_MATERIAL_SIZE_32x32),
    SIZE_64X32(SAMPConstants.OBJECT_MATERIAL_SIZE_64x32),
    SIZE_64X64(SAMPConstants.OBJECT_MATERIAL_SIZE_64x64),
    SIZE_128X32(SAMPConstants.OBJECT_MATERIAL_SIZE_128x32),
    SIZE_128X64(SAMPConstants.OBJECT_MATERIAL_SIZE_128x64),
    SIZE_128X128(SAMPConstants.OBJECT_MATERIAL_SIZE_128x128),
    SIZE_256X32(SAMPConstants.OBJECT_MATERIAL_SIZE_256x32),
    SIZE_256X64(SAMPConstants.OBJECT_MATERIAL_SIZE_256x64),
    SIZE_256X128(SAMPConstants.OBJECT_MATERIAL_SIZE_256x128),
    SIZE_256X256(SAMPConstants.OBJECT_MATERIAL_SIZE_256x256),
    SIZE_512X64(SAMPConstants.OBJECT_MATERIAL_SIZE_512x64),
    SIZE_512X128(SAMPConstants.OBJECT_MATERIAL_SIZE_512x128),
    SIZE_512X256(SAMPConstants.OBJECT_MATERIAL_SIZE_512x256),
    SIZE_512X512(SAMPConstants.OBJECT_MATERIAL_SIZE_512x512);

    companion object : ConstantValueRegistry<Int, ObjectMaterialSize>(ObjectMaterialSize.values())
}