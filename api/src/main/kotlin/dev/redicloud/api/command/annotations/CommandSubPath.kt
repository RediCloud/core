package dev.redicloud.api.command.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandSubPath(
    val path: String = "",
    val permission: String = "",
    val description: String = ""
)