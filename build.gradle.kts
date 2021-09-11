plugins {
    kotlin("jvm") version "1.5.30" apply false
}

allprojects {
    group = "com.arandarkt"
    version = "1.0-SNAPSHOT"
}

subprojects {

    repositories {
        mavenCentral()
    }

    apply {
        plugin("kotlin")
    }

    val implementation by configurations
    val testImplementation by configurations

    dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
        implementation("io.insert-koin:koin-core:3.1.2")
        implementation("com.impossibl.pgjdbc-ng:pgjdbc-ng:0.8.9")
        implementation("com.google.code.gson:gson:2.8.8")
        testImplementation(kotlin("test"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}