plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    kotlin("kapt")
}
dependencies {
    // Moshi
    implementation(libs.moshi.converter)
    ksp(libs.moshi.kotlin.codegen)

    // Room
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.common)
    kapt(libs.androidx.room.compiler)

    // Gson
    implementation(libs.gson)
}