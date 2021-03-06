package ch.leadrian.samp.kamp.codegen.java

import ch.leadrian.samp.kamp.cidl.model.Constant
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.io.File
import java.io.Writer
import javax.lang.model.element.Modifier

internal class SAMPConstantsJavaGenerator(
        private val constants: List<Constant>,
        private val javaPackageName: String,
        private val kampCoreVersion: String,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPConstants.java"

    override fun generate(writer: Writer) {
        val sampConstantsTypeSpecBuilder = TypeSpec
                .classBuilder("SAMPConstants")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addGeneratedAnnotation(this@SAMPConstantsJavaGenerator::class)
                .addPrivateConstructor()
                .addFields()
        writer.writeJavaFile(sampConstantsTypeSpecBuilder.build())
    }

    private fun TypeSpec.Builder.addPrivateConstructor(): TypeSpec.Builder {
        return addMethod(
                MethodSpec
                        .constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build()
        )
    }

    private fun TypeSpec.Builder.addFields(): TypeSpec.Builder {
        addField(
                FieldSpec
                        .builder(
                                String::class.java,
                                "KAMP_CORE_VERSION",
                                Modifier.PUBLIC,
                                Modifier.STATIC,
                                Modifier.FINAL
                        )
                        .initializer("\$S", kampCoreVersion)
                        .build()
        )
        constants.forEach { addField(it.toFieldSpec()) }
        return this
    }

    private fun Constant.toFieldSpec(): FieldSpec {
        return FieldSpec
                .builder(getJavaType(type), name, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", value.data)
                .build()
    }

    private fun Writer.writeJavaFile(typeSpec: TypeSpec) {
        JavaFile
                .builder(javaPackageName, typeSpec)
                .skipJavaLangImports(true)
                .build()
                .writeTo(this)
    }

}