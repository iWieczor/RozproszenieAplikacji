plugins {
    id("java-library")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.0-RC2")
    }
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
}
