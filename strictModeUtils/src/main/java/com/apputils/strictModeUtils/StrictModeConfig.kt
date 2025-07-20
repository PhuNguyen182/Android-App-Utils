package com.apputils.strictModeUtils

/**
 * StrictMode configuration options
 */
data class StrictModeConfig(
    val enableThreadPolicy: Boolean = true,
    val enableVmPolicy: Boolean = true,
    val detectDiskReads: Boolean = true,
    val detectDiskWrites: Boolean = true,
    val detectNetwork: Boolean = true,
    val detectCustomSlowCalls: Boolean = true,
    val detectResourceMismatches: Boolean = true,
    val detectUnbufferedIo: Boolean = true,
    val detectLeakedClosableObjects: Boolean = true,
    val detectLeakedRegistrationObjects: Boolean = true,
    val detectLeakedSqlLiteObjects: Boolean = true,
    val penaltyLog: Boolean = true,
    val penaltyDialog: Boolean = false,
    val penaltyDeath: Boolean = false,
    val penaltyDropBox: Boolean = true,
    val penaltyFlashScreen: Boolean = false,
    val penaltyDeathOnNetwork: Boolean = false
)
