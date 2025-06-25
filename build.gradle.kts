plugins {
    alias(libs.plugins.android.application) apply false


}
buildscript {
    dependencies {
        // Add this line:
        classpath ("com.google.gms:google-services:4.4.1")
    }
}

