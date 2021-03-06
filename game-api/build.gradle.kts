dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.34.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.34.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.34.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.34.1")
    implementation("io.netty:netty-buffer:4.1.68.Final")
    implementation("com.displee:rs-cache-library:6.8.1")
    implementation("org.rsmod:pathfinder:1.2.4")
    implementation(group = "io.guthix", name = "jagex-bytebuf", version = "0.1.4")
    implementation(project(":cache"))

    testImplementation(project(":game"))
}