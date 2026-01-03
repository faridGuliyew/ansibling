import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    //
    id("androidx.room").version("2.8.4")
    id("com.google.devtools.ksp").version("2.2.21-2.0.4")
}

dependencies {
    commonMainImplementation("androidx.room:room-runtime:2.8.4")
    commonMainImplementation("androidx.sqlite:sqlite-bundled:2.6.2")

    commonMainImplementation("com.jcraft:jsch:0.1.55")

//    add("kspJvmMain", "androidx.room:room-compiler:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "dev.faridg.ansibling.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.faridg.ansibling"
            packageVersion = "1.0.0"
        }
    }
}


room {
    schemaDirectory("$projectDir/schemas")
    schemaDirectory("debug", "$projectDir/schemas/debug")
    schemaDirectory("release", "$projectDir/schemas/release")
}