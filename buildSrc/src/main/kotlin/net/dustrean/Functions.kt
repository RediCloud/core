package net.dustrean;

object Functions {
    fun isCi(): Boolean = System.getenv("CI") == "true"
}
