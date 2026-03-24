plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.2.21"
}

android {
    namespace = "com.example.bakalarka"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.bakalarka"
        minSdk = 29
        targetSdk = 36
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // CORE
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // COMPOSE
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.foundation)

    // NAVIGATION
    implementation(libs.androidx.navigation.compose)

    // CONSTRAINT
    implementation(libs.androidx.constraintlayout.compose)

    // ❌ ZMAZANÉ (bolo zbytočné / problémové)
    // implementation(libs.androidx.compose.ui.test)
    // implementation(libs.androidx.compose.remote.creation.compose)
    // implementation(libs.androidx.constraintlayout.compose.v110)

    // TESTY
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // 🔥 DÔLEŽITÉ: nechaj Compose riadiť espresso
    // androidTestImplementation(libs.androidx.espresso.core) ← ZMAZANÉ

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // SUPABASE
    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.6"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    // KTOR
    implementation("io.ktor:ktor-client-android:3.3.3")

    // PASSWORD HASH
    implementation("org.mindrot:jbcrypt:0.4")

    // GRAFY
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("co.yml:ycharts:2.1.0")


}