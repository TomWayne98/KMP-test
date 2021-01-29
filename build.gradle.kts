buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    val sqlDelightVersion: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
       // classpath("com.android.tools.build:gradle:7.0.0-alpha04")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.21")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")

    }
}
group = "com.jetbrains"
version = "1.0-SNAPSHOT"


allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // soon will be just jcenter()
        maven { url = java.net.URI("https://dl.bintray.com/icerockdev/moko") }
    }
}
