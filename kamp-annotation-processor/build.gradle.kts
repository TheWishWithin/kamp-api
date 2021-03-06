plugins {
    `kotlin-kapt`
}

dependencies {
    implementation(project(":kamp-annotations"))
    implementation("com.squareup:kotlinpoet:1.0.0")
    implementation("com.google.auto.service:auto-service:1.0-rc4")
    kapt("com.google.auto.service:auto-service:1.0-rc4")
}