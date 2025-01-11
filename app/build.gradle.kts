import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.11"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application { mainClass = "hexlet.code.App" }

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.4.0")
    { exclude(group = "ch.qos.logback", module = "logback-core") }
//    implementation ("org.eclipse.jetty:jetty-http:12.0.12")
    implementation("io.javalin:javalin-bundle:6.4.0")
    { exclude(group = "ch.qos.logback", module = "logback-core") }
    implementation("io.javalin:javalin-rendering:6.4.0")
    implementation("gg.jte:jte:3.1.15")

    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation(platform("com.konghq:unirest-java-bom:4.4.5"))
    implementation("com.konghq:unirest-java-core")
    implementation("com.konghq:unirest-modules-gson")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.2")

    implementation("org.jsoup:jsoup:1.18.3")

    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}