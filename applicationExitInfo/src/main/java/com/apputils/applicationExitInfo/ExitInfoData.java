package com.apputils.applicationExitInfo;

import android.app.ApplicationExitInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Data class to hold exit information in a Unity-friendly format
 */
public class ExitInfoData {
    private static final String TAG = "ExitInfoUtil";

    public long timestamp;
    public int pid;
    public int realUid;
    public int packageUid;
    public String processName;
    public int reason;
    public String reasonString;
    public int importance;
    public long pss;
    public long rss;
    public String description;
    public String traceData;
    public int status;
    public int definingUid;

    public ExitInfoData() {}

    public ExitInfoData(ApplicationExitInfo info) {
        this.timestamp = info.getTimestamp();
        this.pid = info.getPid();
        this.realUid = info.getRealUid();
        this.packageUid = info.getPackageUid();
        this.processName = info.getProcessName();
        this.reason = info.getReason();
        this.reasonString = getReasonString(info.getReason());
        this.importance = info.getImportance();
        this.pss = info.getPss();
        this.rss = info.getRss();
        this.description = info.getDescription();
        this.status = info.getStatus();
        this.definingUid = info.getDefiningUid();
        this.traceData = extractTraceData(info);
    }

    // Convert to JSON-like string for Unity
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"timestamp\":").append(timestamp).append(",");
        sb.append("\"pid\":").append(pid).append(",");
        sb.append("\"realUid\":").append(realUid).append(",");
        sb.append("\"packageName\":\"").append(packageUid).append("\",");
        sb.append("\"processName\":\"").append(processName).append("\",");
        sb.append("\"reason\":").append(reason).append(",");
        sb.append("\"reasonString\":\"").append(reasonString).append("\",");
        sb.append("\"importance\":").append(importance).append(",");
        sb.append("\"pss\":").append(pss).append(",");
        sb.append("\"rss\":").append(rss).append(",");
        sb.append("\"description\":\"").append(escapeJson(description)).append("\",");
        sb.append("\"status\":").append(status).append(",");
        sb.append("\"definingUid\":").append(definingUid);
        if (traceData != null && !traceData.isEmpty()) {
            sb.append(",\"traceData\":\"").append(escapeJson(traceData)).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
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
     * Extract trace data from ApplicationExitInfo
     */
    private static String extractTraceData(ApplicationExitInfo info) {
        try (InputStream traceInputStream = info.getTraceInputStream()) {
            if (traceInputStream == null) {
                return null;
            }

            StringBuilder traceData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(traceInputStream));
            String line;
            int lineCount = 0;
            final int MAX_LINES = 100; // Limit trace data to prevent memory issues

            while ((line = reader.readLine()) != null && lineCount < MAX_LINES) {
                traceData.append(line).append("\n");
                lineCount++;
            }

            return traceData.toString();
        } catch (IOException e) {
            Log.w(TAG, "Failed to read trace data", e);
            return null;
        }
    }
}
