pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                //useModule("com.android.tools.build:gradle:7.0.0-alpha04")
                useModule("com.android.tools.build:gradle:4.1.2")
            }
        }
    }
}
rootProject.name = "KmmSample"


include(":androidApp")
include(":shared")
// Needed for share preferences library (KISSME lib)
enableFeaturePreview("GRADLE_METADATA")

