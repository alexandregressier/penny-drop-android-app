plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "dev.gressier.pennydrop"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:_")

    implementation("androidx.core:core-ktx:_")
    implementation("androidx.appcompat:appcompat:_")
    implementation("com.google.android.material:material:_")
    implementation("androidx.constraintlayout:constraintlayout:_")

    testImplementation("junit:junit:_")
    androidTestImplementation("androidx.test.ext:junit:_")
    androidTestImplementation("androidx.test.espresso:espresso-core:_")
}