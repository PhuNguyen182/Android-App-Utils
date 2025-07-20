package com.apputils.applicationExitInfo;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationExitInfoUtil {
    private static final String TAG = "ExitInfoUtil";
    private static String currentPackageName;

    public static void setAppPackageName(String packageName) {
        currentPackageName = packageName;
    }

    /**
     * Get all historical exit information for the current package
     * Unity-friendly method that returns array of ExitInfoData
     */
    public static ExitInfoData[] getAllExitInfo(Context context) {
        return getAllExitInfo(context, currentPackageName, 0, Integer.MAX_VALUE);
    }

    /**
     * Get historical exit information with filtering options
     */
    public static ExitInfoData[] getAllExitInfo(Context context, String packageName,
                                                int pid, int maxNum) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                Log.e(TAG, "ActivityManager not available");
                return new ExitInfoData[0];
            }

            List<ApplicationExitInfo> exitInfoList;
            if (packageName != null) {
                exitInfoList = am.getHistoricalProcessExitReasons(packageName, pid, maxNum);
            } else {
                exitInfoList = am.getHistoricalProcessExitReasons(null, pid, maxNum);
            }

            ExitInfoData[] result = new ExitInfoData[exitInfoList.size()];
            for (int i = 0; i < exitInfoList.size(); i++) {
                result[i] = new ExitInfoData(exitInfoList.get(i));
            }

            return result;
        } catch (Exception e) {
            Log.e(TAG, "Error getting exit info", e);
            return new ExitInfoData[0];
        }
    }

    /**
     * Get the most recent exit information
     */
    public static ExitInfoData getLatestExitInfo(Context context) {
        ExitInfoData[] exitInfoArray = getAllExitInfo(context, currentPackageName, 0, 1);
        return exitInfoArray.length > 0 ? exitInfoArray[0] : null;
    }

    /**
     * Get exit information filtered by reason
     */
    public static ExitInfoData[] getExitInfoByReason(Context context, int reason) {
        ExitInfoData[] allExitInfo = getAllExitInfo(context);
        List<ExitInfoData> filtered = new ArrayList<>();

        for (ExitInfoData info : allExitInfo) {
            if (info.reason == reason) {
                filtered.add(info);
            }
        }

        return filtered.toArray(new ExitInfoData[0]);
    }

    /**
     * Get crashes only (both Java and Native)
     */
    public static ExitInfoData[] getCrashExitInfo(Context context) {
        ExitInfoData[] allExitInfo = getAllExitInfo(context);
        List<ExitInfoData> crashes = new ArrayList<>();

        for (ExitInfoData info : allExitInfo) {
            if (info.reason == ExitReason.CRASH || info.reason == ExitReason.CRASH_NATIVE) {
                crashes.add(info);
            }
        }

        return crashes.toArray(new ExitInfoData[0]);
    }

    /**
     * Get ANR (Application Not Responding) exit information
     */
    public static ExitInfoData[] getANRExitInfo(Context context) {
        return getExitInfoByReason(context, ExitReason.ANR);
    }

    /**
     * Convert reason code to human-readable string
     */
    public static String getReasonString(int reason) {
        switch (reason) {
            case ExitReason.UNKNOWN: return "Unknown";
            case ExitReason.EXIT_SELF: return "Exit Self";
            case ExitReason.SIGNALED: return "Signaled";
            case ExitReason.LOW_MEMORY: return "Low Memory";
            case ExitReason.CRASH: return "Crash (Java)";
            case ExitReason.CRASH_NATIVE: return "Crash (Native)";
            case ExitReason.ANR: return "ANR";
            case ExitReason.INITIALIZATION_FAILURE: return "Initialization Failure";
            case ExitReason.PERMISSION_CHANGE: return "Permission Change";
            case ExitReason.EXCESSIVE_RESOURCE_USAGE: return "Excessive Resource Usage";
            case ExitReason.USER_REQUESTED: return "User Requested";
            case ExitReason.USER_STOPPED: return "User Stopped";
            case ExitReason.DEPENDENCY_DIED: return "Dependency Died";
            case ExitReason.OTHER: return "Other";
            default: return "Unknown (" + reason + ")";
        }
    }

    /**
     * Generate a summary report of all exit information
     * Unity-friendly method that returns a formatted string
     */
    public static String generateSummaryReport(Context context) {
        ExitInfoData[] exitInfoArray = getAllExitInfo(context);

        if (exitInfoArray.length == 0) {
            return "No exit information available";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== APPLICATION EXIT INFORMATION SUMMARY ===\n");
        report.append("Total exits recorded: ").append(exitInfoArray.length).append("\n\n");

        // Count exits by reason
        Map<Integer, Integer> reasonCounts = new HashMap<>();
        for (ExitInfoData info : exitInfoArray) {
            Integer x = reasonCounts.getOrDefault(info.reason, 0);
            reasonCounts.put(info.reason, x + 1);
        }

        report.append("Exit reasons breakdown:\n");
        for (Map.Entry<Integer, Integer> entry : reasonCounts.entrySet()) {
            report.append("- ").append(getReasonString(entry.getKey()))
                    .append(": ").append(entry.getValue()).append("\n");
        }

        report.append("\n=== RECENT EXITS (Last 5) ===\n");
        int count = Math.min(5, exitInfoArray.length);
        for (int i = 0; i < count; i++) {
            ExitInfoData info = exitInfoArray[i];
            report.append("Exit #").append(i + 1).append(":\n");
            report.append("  Timestamp: ").append(new java.util.Date(info.timestamp)).append("\n");
            report.append("  Reason: ").append(info.reasonString).append("\n");
            report.append("  Process: ").append(info.processName).append(" (PID: ").append(info.pid).append(")\n");
            if (info.description != null && !info.description.isEmpty()) {
                report.append("  Description: ").append(info.description).append("\n");
            }
            report.append("\n");
        }

        return report.toString();
    }

    /**
     * Unity-specific method to get exit info as JSON array string
     */
    public static String getAllExitInfoAsJson(Context context) {
        ExitInfoData[] exitInfoArray = getAllExitInfo(context);

        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < exitInfoArray.length; i++) {
            if (i > 0) json.append(",");
            json.append(exitInfoArray[i].toJsonString());
        }
        json.append("]");

        return json.toString();
    }
}