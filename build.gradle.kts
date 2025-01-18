import net.neoforged.gradle.common.tasks.IdePostSyncExecutionTask

plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.180"
    id("org.cadixdev.licenser") version "0.6.1"
}

// Variables
internal val java_version: String by rootProject.extra
internal val minecraft_version: String by rootProject.extra
internal val minecraft_version_range: String by rootProject.extra
internal val neo_version: String by rootProject.extra
internal val neo_version_range: String by rootProject.extra
internal val neo_loader_version_range: String by rootProject.extra
internal val mod_id: String by rootProject.extra
internal val mod_name: String by rootProject.extra
internal val mod_license: String by rootProject.extra
internal val mod_version: String by rootProject.extra
internal val mod_group_id: String by rootProject.extra
internal val mod_authors: String by rootProject.extra
internal val mod_description: String by rootProject.extra

// Common metadata
base.archivesName.set(mod_id.replace('_', '-'))
group = mod_group_id
version = mod_version
java.toolchain.languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))

// Source sets
internal val generatedClient: SourceSet = sourceSets.create("generated_client") {
    java.setSrcDirs(emptyList<Any>())
    resources.setSrcDirs(listOf("src/generated/client/resources"))
}
internal val generatedServer: SourceSet = sourceSets.create("generated_server") {
    java.setSrcDirs(emptyList<Any>())
    resources.setSrcDirs(listOf("src/generated/server/resources"))
}

val generateModMetadata: TaskProvider<ProcessResources> = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties: Map<String, String> = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version" to neo_version,
        "neo_version_range" to neo_version_range,
        "loader_version_range" to neo_loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from(project.relativePath("src/main/templates"))
    into(project.relativePath("build/generated/sources/modMetadata"))
}

sourceSets["main"].resources {
    source(generatedClient.resources)
    srcDir(generateModMetadata)
    exclude("./cache")
}

tasks.withType<IdePostSyncExecutionTask>().forEach { it.finalizedBy(generateModMetadata) }

// Handle run configurations
runs {
    configureEach {
        // Add system properties
        systemProperty("forge.logging.markers", "REGISTRIES")
        systemProperty("forge.logging.console.level", "debug")

        // Add mod source
        modSource(sourceSets["main"])
    }

    // Configured runs
    create("client") {
        systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
    }
    create("server") {
        systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        argument("--nogui")
    }
    create("gameTestServer") {
        systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
    }
    create("clientData") {
        arguments("--mod", mod_id, "--all", "--output", generatedClient.resources.srcDirs.first().absolutePath)
        sourceSets["main"].resources.srcDirs.forEach { arguments("--existing", it.absolutePath) }
    }
    create("serverData") {
        arguments("--mod", mod_id, "--all", "--output", generatedServer.resources.srcDirs.first().absolutePath)
        sourceSets["main"].resources.srcDirs.forEach { arguments("--existing", it.absolutePath) }
    }
}

minecraft {
    accessTransformers.file("src/main/resources/META-INF/accesstransformer.cfg")
    interfaceInjections.file("src/main/resources/META-INF/interfaceinjection.json")
}

// Handle dependencies
repositories {
    mavenLocal()
}

configurations {
    runtimeClasspath {
        extendsFrom(configurations.localRuntime.get())
    }
}

dependencies {
    // NeoForge
    implementation(group = "net.neoforged", name = "neoforge", version = neo_version)
}

// Project settings
license {
    header.set(rootProject.resources.text.fromFile("HEADER"))

    properties {
        set("mod_authors", mod_authors)
        set("mod_license", mod_license)
    }

    include("**/*.java")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

idea.module {
    isDownloadSources = true
    isDownloadJavadoc = true
}
