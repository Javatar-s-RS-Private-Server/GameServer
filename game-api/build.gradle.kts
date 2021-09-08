dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.33.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.33.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.33.1")
    implementation("io.netty:netty-buffer:4.1.67.Final")
    implementation(group = "io.guthix", name = "jagex-bytebuf", version = "0.1.4")

    testImplementation(project(":game"))
}