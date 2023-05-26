plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.model"
    compileSdk = Config.compile_sdk

    defaultConfig {
        minSdk = Config.min_sdk
        targetSdk = Config.target_sdk

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
        sourceCompatibility =Config.java_version
        targetCompatibility =Config.java_version
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
}

dependencies {
    implementation(Retrofit.converter_gson)
    implementation(Retrofit.logging_interceptor)
    implementation(Koin.koin_android)
    implementation(Koin.koin_core)
    implementation(Koin.koin_compat)
    //Design
    implementation(Design.appcompat)
    implementation(Design.material)
    implementation(Design.fragment_ktx)
    implementation(Design.ui_ktx)

//Kotlin
    implementation(Kotlin.core)
    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)
    //TestImpl
    testImplementation(TestImpl.junit)
    androidTestImplementation(TestImpl.espresso)
    androidTestImplementation(TestImpl.test_imlement_junit)
}