android {
    namespace = "com.nusa.downloadmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nusa.downloadmanager"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
