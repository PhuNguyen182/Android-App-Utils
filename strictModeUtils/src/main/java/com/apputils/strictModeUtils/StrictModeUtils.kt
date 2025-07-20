package com.apputils.strictModeUtils

import android.os.StrictMode
import android.util.Log

/**
 * Utility class for managing StrictMode in Unity applications.
 * Provides methods to enable StrictMode with various configurations
 * and detect common issues that can cause app crashes.
 */
class StrictModeUtils {
    object Constants {
        const val TAG = "StrictModeUtils"
    }
    
    /**
     * Enable StrictMode with default configuration
     */
    fun enableStrictMode() {
        enableStrictMode(StrictModeConfig())
    }
    
    /**
     * Enable StrictMode with custom configuration
     */
    fun enableStrictMode(config: StrictModeConfig) {
        try {
            if (config.enableThreadPolicy) {
                enableThreadPolicy(config)
            }
            
            if (config.enableVmPolicy) {
                enableVmPolicy(config)
            }
            
            Log.i(Constants.TAG, "StrictMode enabled successfully")
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Failed to enable StrictMode: ${e.message}")
        }
    }
    
    /**
     * Enable ThreadPolicy with specified configuration
     */
    private fun enableThreadPolicy(config: StrictModeConfig) {
        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
        
        // Detection policies
        if (config.detectDiskReads) {
            threadPolicyBuilder.detectDiskReads()
        }
        if (config.detectDiskWrites) {
            threadPolicyBuilder.detectDiskWrites()
        }
        if (config.detectNetwork) {
            threadPolicyBuilder.detectNetwork()
        }
        if (config.detectCustomSlowCalls) {
            threadPolicyBuilder.detectCustomSlowCalls()
        }
        if (config.detectResourceMismatches) {
            threadPolicyBuilder.detectResourceMismatches()
        }
        if (config.detectUnbufferedIo) {
            threadPolicyBuilder.detectUnbufferedIo()
        }
        
        // Penalty policies
        if (config.penaltyLog) {
            threadPolicyBuilder.penaltyLog()
        }
        if (config.penaltyDialog) {
            threadPolicyBuilder.penaltyDialog()
        }
        if (config.penaltyDeath) {
            threadPolicyBuilder.penaltyDeath()
        }
        if (config.penaltyDropBox) {
            threadPolicyBuilder.penaltyDropBox()
        }
        if (config.penaltyFlashScreen) {
            threadPolicyBuilder.penaltyFlashScreen()
        }
        if (config.penaltyDeathOnNetwork) {
            threadPolicyBuilder.penaltyDeathOnNetwork()
        }

        
        StrictMode.setThreadPolicy(threadPolicyBuilder.build())
    }
    
    /**
     * Enable VmPolicy with specified configuration
     */
    private fun enableVmPolicy(config: StrictModeConfig) {
        val vmPolicyBuilder = StrictMode.VmPolicy.Builder()
        
        // Detection policies
        if (config.detectLeakedClosableObjects) {
            vmPolicyBuilder.detectLeakedClosableObjects()
        }
        if (config.detectLeakedRegistrationObjects) {
            vmPolicyBuilder.detectLeakedRegistrationObjects()
        }
        if (config.detectLeakedSqlLiteObjects) {
            vmPolicyBuilder.detectLeakedSqlLiteObjects()
        }
        
        // Penalty policies
        if (config.penaltyLog) {
            vmPolicyBuilder.penaltyLog()
        }
        if (config.penaltyDropBox) {
            vmPolicyBuilder.penaltyDropBox()
        }
        
        StrictMode.setVmPolicy(vmPolicyBuilder.build())
    }
    
    /**
     * Enable StrictMode for Unity development (recommended settings)
     */
    fun enableStrictModeForUnity() {
        val unityConfig = StrictModeConfig(
            enableThreadPolicy = true,
            enableVmPolicy = true,
            detectDiskReads = true,
            detectDiskWrites = true,
            detectNetwork = true,
            detectCustomSlowCalls = true,
            detectResourceMismatches = true,
            detectUnbufferedIo = true,
            detectLeakedClosableObjects = true,
            detectLeakedRegistrationObjects = true,
            detectLeakedSqlLiteObjects = true,
            penaltyLog = true,
            penaltyDialog = false, // Avoid blocking Unity UI
            penaltyDeath = false, // Avoid crashing during development
            penaltyDropBox = true,
            penaltyFlashScreen = false,
            penaltyDeathOnNetwork = false
        )
        
        enableStrictMode(unityConfig)
    }
    
    /**
     * Enable StrictMode for production (strict settings)
     */
    fun enableStrictModeForProduction() {
        val productionConfig = StrictModeConfig(
            enableThreadPolicy = true,
            enableVmPolicy = true,
            detectDiskReads = true,
            detectDiskWrites = true,
            detectNetwork = true,
            detectCustomSlowCalls = true,
            detectResourceMismatches = true,
            detectUnbufferedIo = true,
            detectLeakedClosableObjects = true,
            detectLeakedRegistrationObjects = true,
            detectLeakedSqlLiteObjects = true,
            penaltyLog = true,
            penaltyDialog = false,
            penaltyDeath = false, // Be careful with this in production
            penaltyDropBox = true,
            penaltyFlashScreen = false,
            penaltyDeathOnNetwork = false
        )
        
        enableStrictMode(productionConfig)
    }
    
    /**
     * Disable StrictMode (use with caution)
     */
    fun disableStrictMode() {
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
            StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
            Log.i(Constants.TAG, "StrictMode disabled")
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Failed to disable StrictMode: ${e.message}")
        }
    }
    
    /**
     * Check if StrictMode is currently enabled
     */
    fun isStrictModeEnabled(): Boolean {
        return try {
            val threadPolicy = StrictMode.getThreadPolicy()
            val vmPolicy = StrictMode.getVmPolicy()
            
            // Check if policies are not LAX
            threadPolicy != StrictMode.ThreadPolicy.LAX || vmPolicy != StrictMode.VmPolicy.LAX
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Failed to check StrictMode status: ${e.message}")
            false
        }
    }
    
    /**
     * Get current StrictMode configuration
     */
    fun getCurrentConfig(): StrictModeConfig? {
        return try {
            val threadPolicy = StrictMode.getThreadPolicy()
            val vmPolicy = StrictMode.getVmPolicy()
            
            // Note: This is a simplified check as we can't directly access all policy flags
            StrictModeConfig(
                enableThreadPolicy = threadPolicy != StrictMode.ThreadPolicy.LAX,
                enableVmPolicy = vmPolicy != StrictMode.VmPolicy.LAX,
                penaltyLog = true, // Assume logging is enabled if StrictMode is active
                penaltyDialog = false,
                penaltyDeath = false,
                penaltyDropBox = true
            )
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Failed to get current StrictMode config: ${e.message}")
            null
        }
    }
    
    /**
     * Allow disk operations temporarily (useful for Unity file operations)
     */
    fun allowDiskOperations(operation: () -> Unit) {
        val originalPolicy = StrictMode.getThreadPolicy()
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
            operation()
        } finally {
            StrictMode.setThreadPolicy(originalPolicy)
        }
    }
    
    /**
     * Allow network operations temporarily (useful for Unity network calls)
     */
    fun allowNetworkOperations(operation: () -> Unit) {
        val originalPolicy = StrictMode.getThreadPolicy()
        try {
            val newPolicy = StrictMode.ThreadPolicy.Builder(originalPolicy)
                .permitNetwork()
                .build()
            StrictMode.setThreadPolicy(newPolicy)
            operation()
        } finally {
            StrictMode.setThreadPolicy(originalPolicy)
        }
    }
    
    /**
     * Allow all operations temporarily (use sparingly)
     */
    fun allowAllOperations(operation: () -> Unit) {
        val originalThreadPolicy = StrictMode.getThreadPolicy()
        val originalVmPolicy = StrictMode.getVmPolicy()
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
            StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
            operation()
        } finally {
            StrictMode.setThreadPolicy(originalThreadPolicy)
            StrictMode.setVmPolicy(originalVmPolicy)
        }
    }
}
