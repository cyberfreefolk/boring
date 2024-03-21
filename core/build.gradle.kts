import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.room.boring"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testCompile("junit", "junit", "4.12")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.slf4j:slf4j-api:1.7.25")
    //implementation("org.slf4j:slf4j-log4j12:1.7.30")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xjvm-default=all")

}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xjvm-default=all")

}

apply("../gradle-mavenizer.gradle")