package dev.redicloud.api.language.placeholder

interface PlaceholderReference {
    fun reference(provider: PlaceholderProvider)
}