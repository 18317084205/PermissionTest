package com.liang.permission;

public abstract class ResultHelper {
    public abstract void onPermissionUntreated(String[] permissions);

    public abstract void onPermissionGranted(String[] permissions);

    public abstract void onPermissionDenied(String[] permissions);

    public abstract void onPermissionBanned(String[] permissions);
}
