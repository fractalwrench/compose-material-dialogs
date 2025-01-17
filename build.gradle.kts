buildscript {
    repositories {
        google()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath("org.jmailen.gradle:kotlinter-gradle:3.3.0")
        classpath("com.android.tools.build:gradle:7.0.0-alpha09")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx/") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi"
            )

        }
    }
}