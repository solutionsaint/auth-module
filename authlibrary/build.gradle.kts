plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
}

android {
    namespace = "com.techlambda.authlibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.icons)
    implementation(libs.androidx.material3.icons.extended)
    implementation(libs.androidx.navigation.compose)

    
    //Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    implementation(libs.accompanist.systemuicontroller)

    //Ktor
    implementation(libs.ktor.client.core)
    //Only needed when you want to use Kotlin Serialization
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.android)

    //DataStore
    implementation (libs.androidx.datastore.preferences)

    //Coil
    implementation(libs.coil.compose)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit.interceptor)

    // Google ML Kit
    implementation("com.google.mlkit:barcode-scanning:17.0.2")

    // ZXing
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}


