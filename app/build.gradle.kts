plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
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
}
