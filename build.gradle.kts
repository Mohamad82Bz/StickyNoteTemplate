import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.sayandev.plugin.StickyNoteModules
import xyz.jpenilla.runpaper.task.RunServer
import kotlin.jvm.java

plugins {
    java
    kotlin("jvm") version "2.1.10"
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("org.sayandev.stickynote.project")
}

val slug = rootProject.name.lowercase()
group = "org.sayandev"
version = "1.0.0-SNAPSHOT"

stickynote {
    modules(StickyNoteModules.CORE, StickyNoteModules.BUKKIT)
    relocate(!gradle.startParameter.taskNames.any { it.startsWith("runServer") })
}

repositories {
    mavenCentral()
    mavenLocal()
    
    maven("https://repo.sayandev.org/snapshots")
    maven("https://repo.sayandev.org/releases")
    maven("https://repo.sayandev.org/private")
    //PlaceholderApi
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")

    compileOnly(fileTree("libs"))
}

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
        archiveClassifier.set(null as String?)
        destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    withType(RunServer::class.java) {
        downloadPlugins {
            modrinth("viaversion", "5.0.3")
            modrinth("essentialsx", "2.20.1")
            hangar("placeholderapi", "2.11.6")
            url("https://download.luckperms.net/1567/bukkit/loader/LuckPerms-Bukkit-5.4.150.jar")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://ci.lucko.me/job/spark/471/artifact/spark-bukkit/build/libs/spark-1.10.123-bukkit.jar")
        }
    }

    runServer {
        minecraftVersion("1.20.6")
        
        javaLauncher = project.javaToolchains.launcherFor {
            vendor = JvmVendorSpec.JETBRAINS
            languageVersion = JavaLanguageVersion.of("17")
        }
        
        jvmArgs("-XX:+AllowEnhancedClassRedefinition")
    }
    
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    build {
        dependsOn(shadowJar)
    }
    
    processResources {
        filesMatching(listOf("**plugin.yml", "**plugin.json")) {
            expand(
                "version" to project.version as String,
                "slug" to slug,
                "name" to rootProject.name,
                "description" to project.description
            )
        }
    }
}

bukkit {
    name = project.name
    version = project.version as String
    description = project.description
    website = "https://sayandev.org"
    author = "SayanDevelopment"

    main = "${project.group}.${project.name.lowercase()}.${project.name}"

    apiVersion = "1.13"

    softDepend = listOf(
        "PlaceholderAPI"
    )
}