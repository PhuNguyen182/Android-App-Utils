# StrictModeUtils for Unity Android

A comprehensive utility library for managing Android StrictMode in Unity applications. This library provides easy-to-use methods to enable, configure, and manage StrictMode to detect common issues that can cause app crashes and performance problems.

## Features

- **Unity-Optimized Configurations**: Pre-configured settings specifically designed for Unity applications
- **Flexible Configuration**: Customizable StrictMode settings for different use cases
- **Temporary Policy Relaxation**: Safe methods to temporarily allow operations that would normally violate StrictMode
- **Status Monitoring**: Check if StrictMode is enabled and get current configuration
- **Production Ready**: Different configurations for development and production environments

## Quick Start

### 1. Basic Usage

```kotlin
// Enable StrictMode with default Unity-optimized settings
StrictModeUtils.enableStrictModeForUnity()

// Check if StrictMode is enabled
val isEnabled = StrictModeUtils.isStrictModeEnabled()
```

### 2. Custom Configuration

```kotlin
val customConfig = StrictModeUtils.StrictModeConfig(
    enableThreadPolicy = true,
    enableVmPolicy = true,
    detectDiskReads = true,
    detectDiskWrites = true,
    detectNetwork = true,
    penaltyLog = true,
    penaltyDialog = false, // Avoid blocking Unity UI
    penaltyDeath = false   // Don't crash the app
)

StrictModeUtils.enableStrictMode(customConfig)
```

### 3. Temporary Policy Relaxation

```kotlin
// Allow disk operations temporarily (for Unity file operations)
StrictModeUtils.allowDiskOperations {
    // Unity file operations here
    // File.ReadAllBytes(), File.WriteAllText(), etc.
}

// Allow network operations temporarily (for Unity network calls)
StrictModeUtils.allowNetworkOperations {
    // Unity network operations here
    // UnityWebRequest, WWW, etc.
}

// Allow all operations temporarily (use sparingly)
StrictModeUtils.allowAllOperations {
    // Critical Unity operations here
}
```

## Unity Integration

### Application Class Setup

```kotlin
class MyUnityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize StrictMode for Unity
        UnityStrictModeHelper.initializeForUnity()
    }
}
```

### Unity-Specific Helpers

```kotlin
// Handle Unity file operations safely
UnityStrictModeHelper.performUnityFileOperation {
    // Your Unity file operations here
}

// Handle Unity network operations safely
UnityStrictModeHelper.performUnityNetworkOperation {
    // Your Unity network operations here
}

// Handle Unity initialization safely
UnityStrictModeHelper.performUnityInitialization {
    // Critical Unity initialization code here
}
```

## Configuration Options

### StrictModeConfig Properties

| Property | Description | Default |
|----------|-------------|---------|
| `enableThreadPolicy` | Enable thread policy violations detection | `true` |
| `enableVmPolicy` | Enable VM policy violations detection | `true` |
| `detectDiskReads` | Detect disk read operations on main thread | `true` |
| `detectDiskWrites` | Detect disk write operations on main thread | `true` |
| `detectNetwork` | Detect network operations on main thread | `true` |
| `detectCustomSlowCalls` | Detect custom slow calls | `true` |
| `detectResourceMismatches` | Detect resource mismatches | `true` |
| `detectUnbufferedIo` | Detect unbuffered I/O operations | `true` |
| `detectLeakedClosableObjects` | Detect leaked closable objects | `true` |
| `detectLeakedRegistrationObjects` | Detect leaked registration objects | `true` |
| `detectLeakedSqlLiteObjects` | Detect leaked SQLite objects | `true` |
| `penaltyLog` | Log violations | `true` |
| `penaltyDialog` | Show dialog on violations | `false` |
| `penaltyDeath` | Crash app on violations | `false` |
| `penaltyDropBox` | Send violations to DropBox | `true` |

## Pre-configured Settings

### Unity Development
```kotlin
StrictModeUtils.enableStrictModeForUnity()
```
- Enables all detection policies
- Logs violations but doesn't crash the app
- Avoids dialogs that could block Unity UI
- Optimized for development workflow

### Production
```kotlin
StrictModeUtils.enableStrictModeForProduction()
```
- Enables all detection policies
- Logs violations for monitoring
- Conservative penalty settings
- Safe for production use

## Best Practices

### 1. Initialize Early
Enable StrictMode as early as possible in your application lifecycle, preferably in the `Application.onCreate()` method.

### 2. Use Unity-Specific Helpers
Use `UnityStrictModeHelper` methods for Unity operations to ensure proper handling of StrictMode violations.

### 3. Temporary Relaxation
Use temporary policy relaxation methods sparingly and only when necessary:
- `allowDiskOperations()` for Unity file operations
- `allowNetworkOperations()` for Unity network calls
- `allowAllOperations()` only for critical initialization

### 4. Monitor Logs
Check Android logs for StrictMode violations to identify and fix issues:
```bash
adb logcat | grep StrictMode
```

### 5. Don't Disable Completely
Avoid completely disabling StrictMode unless absolutely necessary. Instead, use temporary relaxation methods.

## Common Unity Operations

### File Operations
```kotlin
UnityStrictModeHelper.performUnityFileOperation {
    // Unity file operations that might trigger StrictMode
    val bytes = File.ReadAllBytes("path/to/file")
    File.WriteAllText("path/to/file", "content")
}
```

### Network Operations
```kotlin
UnityStrictModeHelper.performUnityNetworkOperation {
    // Unity network operations that might trigger StrictMode
    val request = UnityWebRequest.Get("https://api.example.com")
    request.SendWebRequest()
}
```

### Initialization
```kotlin
UnityStrictModeHelper.performUnityInitialization {
    // Critical Unity initialization that needs full access
    // Unity engine initialization, critical file operations, etc.
}
```

## Troubleshooting

### StrictMode Violations
If you see StrictMode violations in logs:
1. Identify the operation causing the violation
2. Wrap it in appropriate temporary relaxation method
3. Consider moving the operation to a background thread if possible

### Performance Issues
If StrictMode causes performance issues:
1. Use more specific temporary relaxation methods
2. Consider moving heavy operations to background threads
3. Review if the operation is necessary on the main thread

### App Crashes
If StrictMode causes app crashes:
1. Check if `penaltyDeath` is enabled
2. Use `penaltyLog` instead of `penaltyDeath` for debugging
3. Review violation logs to identify the root cause

## API Reference

### StrictModeUtils
- `enableStrictMode()` - Enable with default configuration
- `enableStrictMode(config)` - Enable with custom configuration
- `enableStrictModeForUnity()` - Enable with Unity-optimized settings
- `enableStrictModeForProduction()` - Enable with production settings
- `disableStrictMode()` - Disable StrictMode (use with caution)
- `isStrictModeEnabled()` - Check if StrictMode is enabled
- `getCurrentConfig()` - Get current configuration
- `allowDiskOperations(operation)` - Temporarily allow disk operations
- `allowNetworkOperations(operation)` - Temporarily allow network operations
- `allowAllOperations(operation)` - Temporarily allow all operations

### UnityStrictModeHelper
- `initializeForUnity()` - Initialize StrictMode for Unity
- `performUnityFileOperation(operation)` - Safe Unity file operations
- `performUnityNetworkOperation(operation)` - Safe Unity network operations
- `performUnityInitialization(operation)` - Safe Unity initialization

## License

This library is provided as-is for use in Unity Android applications. Use responsibly and ensure compliance with your project's licensing requirements. 