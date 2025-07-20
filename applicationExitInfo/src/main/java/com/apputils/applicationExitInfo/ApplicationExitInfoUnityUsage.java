package com.apputils.applicationExitInfo;

import android.app.Activity;

public class ApplicationExitInfoUnityUsage {
    private Activity currentUnityActivity;

    public void setCurrentUnityActivity(Activity activity) {
        currentUnityActivity = activity;
    }

    public void setCurrentAppPackageName(String packageName) {
        ApplicationExitInfoUtil.setAppPackageName(packageName);
    }

    public String getAllApplicationExitInfo() {
        return ApplicationExitInfoUtil.getAllExitInfoAsJson(currentUnityActivity);
    }
}
