package com.apputils.applicationExitInfo;

import android.app.Activity;
import com.google.gson.Gson;

public class ApplicationExitInfoUnityUsage {
    private final Gson jsonConverter;

    private Activity currentUnityActivity;

    public ApplicationExitInfoUnityUsage() {
        jsonConverter = new Gson();
    }

    public void setCurrentUnityActivity(Activity activity) {
        currentUnityActivity = activity;
    }

    public void setCurrentAppPackageName(String packageName) {
        ApplicationExitInfoUtil.setAppPackageName(packageName);
    }

    public String getAllApplicationExitInfo() {
        return currentUnityActivity != null ? ApplicationExitInfoUtil.getAllExitInfoAsJson(currentUnityActivity) : null;
    }

    public String getLatestApplicationExitInfo() {
        if (currentUnityActivity == null)
            return null;

        ExitInfoData latestExitInfo = ApplicationExitInfoUtil.getLatestExitInfo(currentUnityActivity);
        return jsonConverter.toJson(latestExitInfo);
    }

    public String getSummaryReport() {
        return currentUnityActivity != null ? ApplicationExitInfoUtil.generateSummaryReport(currentUnityActivity) : null;
    }

    public String getANRExitInfo() {
        if (currentUnityActivity == null)
            return null;

        ExitInfoData[] exitInfos = ApplicationExitInfoUtil.getANRExitInfo(currentUnityActivity);
        return jsonConverter.toJson(exitInfos);
    }

    public String getCrashExitInfo() {
        if (currentUnityActivity == null)
            return null;

        ExitInfoData[] exitInfos = ApplicationExitInfoUtil.getCrashExitInfo(currentUnityActivity);
        return jsonConverter.toJson(exitInfos);
    }
}
