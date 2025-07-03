plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.com.wandersonfelipe.listadecompras"
    compileSdk = 35

    dataBinding {
        enable = true
    }

    buildFeatures {
        dataBinding = true
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "br.com.wandersonfelipe.listadecompras"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}