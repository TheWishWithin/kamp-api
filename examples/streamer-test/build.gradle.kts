import org.gradle.internal.os.OperatingSystem

dependencies {
    implementation(project(":kamp-core"))
    implementation(project(":kamp-streamer"))
    implementation(project(":kamp-geodata"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")
}

plugins {
    id("kamp-serverstarter")
}

serverStarter {
    gameModeClassName = "ch.leadrian.samp.kamp.examples.streamertest.StreamerTestGameMode"
    rconPassword = "test1234"
    jvmOption("-Xmx1G")
    configProperty("kamp.streamer.rate.ms", "300")
    configProperty("kamp.ignore.version.mismatch", "true")
}

tasks {
    configureServer {
        dependsOn(project(":kamp-plugin").tasks.getByName("build"))
    }
}