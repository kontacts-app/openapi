import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("java")
    id("org.openapi.generator") version "7.5.0"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "red.razvan.kontacts"
version = "1.0.0"

tasks.register<GenerateTask>("genKotlinSpring") {
    val groupId = rootProject.group.toString()
    val artifactId = "spring"
    val basePackage = "$groupId.$artifactId"

    dependsOn("spotlessCheck")
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/specs/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("api-kotlin").map { it.asFile.path })
    apiPackage.set("$basePackage.api")
    modelPackage.set("$basePackage.model")
    invokerPackage.set("$basePackage.invoker")
    configOptions =
        mapOf(
            "dateLibrary" to "java8",
            "groupId" to groupId,
            "artifactId" to artifactId,
            "artifactVersion" to rootProject.version.toString(),
            "packageName" to basePackage,
            "title" to "Kontacts API",
            "serviceInterface" to "true",
            "serviceImplementation" to "true",
            "implementationPackageScan" to basePackage,
            "useSpringBoot3" to "true",
        )
    generateModelTests.set(false)
    generateModelDocumentation.set(true)
    generateApiTests.set(false)
    generateApiDocumentation.set(true)
    withXml.set(true)
    verbose.set(false)
}

tasks.register<GenerateTask>("genKtorClient") {
    val groupId = rootProject.group.toString()
    val artifactId = "ktor-client"
    val basePackage = "$groupId.ktor.client"

    dependsOn("spotlessCheck")
    generatorName.set("kotlin")
    verbose.set(false)
    inputSpec.set("$rootDir/specs/openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("api-ktor-client").map { it.asFile.path })
    apiPackage.set("$basePackage.api")
    modelPackage.set("$basePackage.model")
    invokerPackage.set("$basePackage.invoker")
    configOptions =
        mapOf(
            "dateLibrary" to "kotlinx-datetime",
            "library" to "multiplatform",
            "groupId" to groupId,
            "artifactId" to artifactId,
            "artifactVersion" to rootProject.version.toString(),
            "packageName" to basePackage,
        )
    generateModelTests.set(false)
    generateModelDocumentation.set(true)
    generateApiTests.set(false)
    generateApiDocumentation.set(true)
    withXml.set(true)
}

spotless {
    yaml {
        prettier()
        target("**/*.yaml")
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("${layout.buildDirectory}/**/*.kts")
        ktlint("1.2.1")
    }
}

openApiValidate {
    inputSpec.set("$rootDir/specs/openapi.yaml")
}
