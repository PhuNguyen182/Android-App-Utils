package com.apputils.applicationExitInfo;

import android.app.ApplicationExitInfo;

public class ExitReason {
    public static final int UNKNOWN = ApplicationExitInfo.REASON_UNKNOWN;
    public static final int EXIT_SELF = ApplicationExitInfo.REASON_EXIT_SELF;
    public static final int SIGNALED = ApplicationExitInfo.REASON_SIGNALED;
    public static final int LOW_MEMORY = ApplicationExitInfo.REASON_LOW_MEMORY;
    public static final int CRASH = ApplicationExitInfo.REASON_CRASH;
    public static final int CRASH_NATIVE = ApplicationExitInfo.REASON_CRASH_NATIVE;
    public static final int ANR = ApplicationExitInfo.REASON_ANR;
    public static final int INITIALIZATION_FAILURE = ApplicationExitInfo.REASON_INITIALIZATION_FAILURE;
    public static final int PERMISSION_CHANGE = ApplicationExitInfo.REASON_PERMISSION_CHANGE;
    public static final int EXCESSIVE_RESOURCE_USAGE = ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE;
    public static final int USER_REQUESTED = ApplicationExitInfo.REASON_USER_REQUESTED;
    public static final int USER_STOPPED = ApplicationExitInfo.REASON_USER_STOPPED;
    public static final int DEPENDENCY_DIED = ApplicationExitInfo.REASON_DEPENDENCY_DIED;
    public static final int OTHER = ApplicationExitInfo.REASON_OTHER;
}
