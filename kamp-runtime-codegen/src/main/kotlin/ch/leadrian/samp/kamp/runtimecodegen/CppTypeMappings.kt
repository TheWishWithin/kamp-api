package ch.leadrian.samp.kamp.runtimecodegen

import ch.leadrian.samp.cidl.model.Types

val CPP_TYPE_MAPPING = mapOf(
        Types.BOOL to "jboolean",
        Types.CHAR to "jchar",
        Types.FLOAT to "jfloat",
        Types.INT to "jint",
        Types.STRING to "jstring",
        Types.VOID to "void"
)

val CPP_OUT_TYPE_MAPPING = mapOf(
        Types.FLOAT to "jobject",
        Types.INT to "jobject",
        Types.STRING to "jobject"
)

val JVM_TYPE_SIGNATURES = mapOf(
        Types.BOOL to "Z",
        Types.CHAR to "C",
        Types.FLOAT to "F",
        Types.INT to "I",
        Types.STRING to "Ljava/lang/String;",
        Types.VOID to "V"
)

val JVM_OUT_TYPE_SIGNATURE = mapOf(
        Types.FLOAT to "Lch/leadrian/samp/kamp/runtime/types/ReferenceFloat;",
        Types.INT to "Lch/leadrian/samp/kamp/runtime/types/ReferenceInt",
        Types.STRING to "Lch/leadrian/samp/kamp/runtime/types/ReferenceString;"
)

fun getCppType(typeName: String) =
        CPP_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown C++ type: $typeName")

fun getCppOutType(typeName: String) =
        CPP_OUT_TYPE_MAPPING[typeName] ?: throw IllegalStateException("Unknown C++ out type: $typeName")

fun getJvmTypeSignature(typeName: String) =
        JVM_TYPE_SIGNATURES[typeName] ?: throw IllegalStateException("Unknown C++ type: $typeName")

fun getJvmOutTypeSignature(typeName: String) =
        JVM_OUT_TYPE_SIGNATURE[typeName] ?: throw IllegalStateException("Unknown C++ out type: $typeName")