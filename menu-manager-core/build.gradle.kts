group = "io.github.bfur64"
version = providers.gradleProperty("version").get()

val tetrueTerminal: String = providers.gradleProperty("tetrueTerminal").get()

plugins {
    `java-library`
    signing
    id("com.github.gmazzo.buildconfig") version "6.0.10"
    id("com.vanniktech.maven.publish") version "0.37.0"
}

buildConfig {
    className("Versions")
    packageName(group.toString())
    useJavaOutput()
    buildConfigField("String", "MENU_MANAGER", "\"${project.version}\"")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    implementation("org.jspecify:jspecify:1.0.0")

    api("io.github.bfur64:tetrue-terminal:$tetrueTerminal")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

mavenPublishing {
    coordinates(
        group.toString(),
        "menu-manager",
        version.toString()
    )

    pom {
        name = "menu-manager"
        description = "A composable main menu system, built in Java using the Lanterna UI Library"
        inceptionYear = "2026"
        url = "https://github.com/BFUR64/menu-manager/"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit"
                distribution = "repo"
            }
        }

        developers {
            developer {
                id = "BFUR64"
                name = "Terrance"
                url = "https://github.com/BFUR64/"
            }
        }

        scm {
            url = "https://github.com/BFUR64/menu-manager/"
            connection = "scm:git:https://github.com/BFUR64/menu-manager.git"
            developerConnection = "scm:git:ssh://git@github.com/BFUR64/menu-manager.git"
        }
    }

    publishToMavenCentral()
    signAllPublications()
}

signing {
    useInMemoryPgpKeys(
        providers.fileContents(
            layout.projectDirectory.file("signing-key.asc")
        ).asText.get(),
        providers.gradleProperty("signingInMemoryKeyPassword").get()
    )
}
