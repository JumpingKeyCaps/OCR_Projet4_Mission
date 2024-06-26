import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.dagger.hilt.android")
  id("kotlin-kapt")
  kotlin("kapt")
}

android {
  namespace = "com.aura"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.aura"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
    languageVersion = "1.9"
  }


  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.8.0")
  implementation("androidx.annotation:annotation:1.6.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  //lifecycle
  implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
  implementation ("androidx.activity:activity-ktx:1.8.2")
  implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

  //coroutine
  implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

  //Hilt
  implementation ("com.google.dagger:hilt-android:2.44")
  kapt ("com.google.dagger:hilt-compiler:2.44")

  //retrofit + OkHttp3 + Gson
  implementation ("com.squareup.retrofit2:retrofit:2.9.0")
  implementation ("com.squareup.okhttp3:okhttp:4.9.3")
  implementation ("com.squareup.moshi:moshi:1.15.0")
  implementation ("com.squareup.moshi:moshi-kotlin:1.15.0")
  implementation ("com.squareup.retrofit2:converter-moshi:2.4.0")


  //tests
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  testImplementation ("io.mockk:mockk:1.13.2")
  testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
}

