plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.softengineeringmessanger"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.softengineeringmessanger"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.8.2")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // https://mvnrepository.com/artifact/com.google.dagger/dagger
    implementation("com.google.dagger:dagger:2.51.1")
    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    kapt("com.google.dagger:dagger-compiler:2.51.1")
}
kapt {
    correctErrorTypes = true
}
