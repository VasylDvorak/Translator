plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.favoritescreen"
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
    implementation(project(Modules.model))
    implementation(project(Modules.utils))
    implementation(project(Modules.core))
    implementation(project(Modules.descriptionScreen))
    implementation(project(Modules.repository))

    implementation(Koin.koin_android)
    implementation(Koin.koin_core)
    implementation(Koin.koin_compat)
    //Cicerone
    implementation(Cicerone.cicerone)
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}