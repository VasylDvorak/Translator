plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.repository"
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
    implementation (project(Modules.model))
    implementation (project(Modules.room))
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