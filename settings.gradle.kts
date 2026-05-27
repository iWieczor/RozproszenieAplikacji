rootProject.name = "RozproszenieAplikacji"

pluginManagement {
    repositories {
        maven("https://repo.spring.io/milestone")
        gradlePluginPortal()
    }
}

include("shared", "writer", "reader", "cleaner")
