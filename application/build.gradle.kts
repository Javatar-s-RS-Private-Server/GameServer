plugins {
    application
}

application {
    mainClass.set("com.arandarkt.application.Application")
}

dependencies {
    implementation(project(":cache"))
    implementation(project(":game"))
    implementation(project(":game-api"))
    implementation(project(":network"))
    implementation("io.netty:netty-all:4.1.68.Final")
    implementation("com.displee:rs-cache-library:6.8.1")
}