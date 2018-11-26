package com.liang.permission;

public interface PermissionListener {
    void onPermissionGranted(String[] permissions);

    void onPermissionDenied();
}
