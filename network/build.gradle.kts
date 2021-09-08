dependencies {
    implementation("io.netty:netty-all:4.1.67.Final")
    implementation("com.displee:rs-cache-library:6.8.1")
    implementation(group = "io.guthix", name = "jagex-bytebuf", version = "0.1.4")
    implementation(project(":cache"))
    implementation(project(":game-api"))

}