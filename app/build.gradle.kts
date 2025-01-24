import com.android.build.gradle.internal.utils.ANDROID_BUILT_IN_KAPT_PLUGIN_ID

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    kotlin("plugin.serialization") version "2.0.21"
    //id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.smamysuperalarm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smamysuperalarm"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //implementation(libs.androidx.room.runtime.v252)
    //kapt(libs.androidx.room.compiler.v252)
    // optional - Kotlin Extensions and Coroutines support for Room
    //implementation(libs.androidx.room.ktx.v252)

    //ksp(libs.room.compiler)

    implementation(libs.androidx.room.runtime)

        // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
        // See Add the KSP plugin to your project
        //ksp(libs.androidx.room.compiler)

        // If this project only uses Java source, use the Java annotationProcessor
        // No additional plugins are necessary
    annotationProcessor(libs.androidx.room.compiler)

        // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

        // optional - RxJava2 support for Room
    implementation(libs.androidx.room.rxjava2)

        // optional - RxJava3 support for Room
    implementation(libs.androidx.room.rxjava3)

        // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

        // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

        // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

   /*

    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation (androidx.appcompat:appcompat:1.6.1)
    implementation (androidx.compose.foundation:foundation-layout-android:1.7.6)
    implementation (androidx.compose.foundation:foundation-layout-desktop:1.7.0)
    implementation (androidx.compose.foundation:foundation-layout-jvmstubs:1.7.6)

    def composeBom = platform('androidx.compose:compose-bom:2023.10.00')
    implementation composeBom
            implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    debugImplementation 'androidx.compose.ui:ui-tooling'
    implementation 'androidx.activity:activity-compose:1.8.0'

    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"*/

    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
}


