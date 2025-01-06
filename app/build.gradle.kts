plugins {
    application
    checkstyle
    jacoco
//    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    //Проверка на последние версии зависимостей
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("io.javalin:javalin:6.4.0") {
        exclude(group = "ch.qos.logback", module = "logback-core")
    }
    implementation("io.javalin:javalin-bundle:6.4.0") {
        exclude(group = "ch.qos.logback", module = "logback-core")
    }
    implementation("io.javalin:javalin-rendering:6.4.0")
    implementation("gg.jte:jte:3.1.15")

    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("org.postgresql:postgresql:42.7.4")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}