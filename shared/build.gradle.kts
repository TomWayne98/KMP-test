import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("SomeDatabase") {
        packageName = "com.jetbrains.kmm"
    }
}

group = "com.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

val ktorVersion = "1.4.0"
val sqlDelightVersion: String by project
val napierVersion = "1.5.0-alpha1"
val kcron = "0.2.0"
val kissmeVersion: String by project

kotlin {
    android()
    // Revert to just ios() when gradle plugin can properly resolve it
    // This block is needed to make NativeSQLDriver visible - to make DB work on iOS
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation ("com.github.aakira:napier:$napierVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
              //  implementation("com.netguru.kissme:common:$kissmeVersion")
               // implementation("com.ucasoft.kcron:kcron-common:$kcron")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.google.android.material:material:1.2.0")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
             //   implementation("com.netguru.kissme:android:0.2.5")

            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
           //     implementation("com.netguru.kissme:ios:0.2.5")
            }
        }

        val iosTest by getting
    }
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)