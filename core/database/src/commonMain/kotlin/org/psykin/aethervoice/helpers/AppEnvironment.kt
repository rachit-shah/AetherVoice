package org.psykin.aethervoice.helpers

sealed class AppEnvironment {
    data object Debug : AppEnvironment()
    data object Release : AppEnvironment()

    fun isDebug(): Boolean =
        this is Debug

    fun isRelease(): Boolean =
        this is Release
}