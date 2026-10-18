plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter.active("26.2-fabric")

stonecutter.parameters {
    val currentProject = current.project
    val currentLoader = currentProject.substringAfterLast('-')
    constants.match(currentLoader, "fabric", "neoforge")
}