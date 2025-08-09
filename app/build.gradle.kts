    plugins {
        alias(libs.plugins.android.application)
    }

    android {
        namespace = "com.example.foodie"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.example.foodie"
            minSdk = 23
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"

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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    dependencies {

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.play.services.analytics.impl)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation (libs.volley)
        implementation("androidx.core:core:1.16.0")
        implementation(libs.gson)
        implementation(libs.glide)
        annotationProcessor(libs.glideCompiler)


    }