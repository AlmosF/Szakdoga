import org.gradle.kotlin.dsl.implementation
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    //alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.android)

    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt.android)


}

android {
    namespace = "com.example.szakdogafitness"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.szakdogafitness"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        val apiKey: String = localProperties.getProperty("apiKey") ?: ""
        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}
dependencies {

    //Camera
    val camerax_version = "1.3.0" // or the latest version

    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")

    //Permission
    implementation ("com.google.accompanist:accompanist-permissions:0.36.0")

    //Generative Ai
    implementation(libs.generativeai)

    //CoilImage
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Chart
    //implementation("com.github.fracassi-marco:JetChart:1.4.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Material Icons
    implementation(libs.androidx.material.icons.extended)

    //phone
    implementation ("com.googlecode.libphonenumber:libphonenumber:8.12.50")
    implementation ("io.michaelrocks:libphonenumber-android:8.12.50")

    //Constraint Layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    //Compose Navigation
    implementation(libs.androidx.navigation.compose)

    //ViewModel Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}