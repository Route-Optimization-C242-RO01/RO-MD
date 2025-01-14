import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

val localProperties = Properties()
localProperties.load(FileInputStream("local.properties"))
val googleDirectionApiKey : String = localProperties.getProperty("GOOGLE_DIRECTION_API_KEY")

android {
    namespace = "com.example.myrouteoptimization"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myrouteoptimization"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = false
            buildConfigField("String", "GOOGLE_DIRECTION_API_KEY", googleDirectionApiKey)
        }
        debug {
            buildConfigField("String", "GOOGLE_DIRECTION_API_KEY", googleDirectionApiKey)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Image
    implementation(libs.glide)
    implementation(libs.circleimageview)

    //Maps
    implementation(libs.play.services.maps)
    implementation(libs.android.maps.utils)


    // Firebase Platform
    //noinspection GradleDependency,UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Preference
    implementation(libs.androidx.datastore.preferences)
}