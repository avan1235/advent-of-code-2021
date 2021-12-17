import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.0"
  application
}

group = "ml.kotlin.dev"
version = "1.0"

repositories {
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClass.set("AdventKt")
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  implementation(kotlin("reflect"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC3")
  testImplementation(kotlin("test"))
}
