plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    kotlin("kapt")
}
android {
    namespace = "com.multrm.data"
    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    implementation(project(":domain"))
    implementation(project(":entity"))

    // Retrofit
    api(libs.retrofit)

    // Moshi
    api(libs.moshi.kotlin)
    api(libs.moshi)
    api(libs.moshi.converter)
    ksp(libs.moshi.kotlin.codegen)

    // Inject
    implementation(libs.javax.inject)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Android
    implementation(libs.androidx.core.ktx)

    // Room
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.common)
    kapt(libs.androidx.room.compiler)
}

