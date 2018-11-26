package com.liang.permission;

import android.app.Activity;
import android.content.Context;

public abstract class PermissionHelper {
    public static final String TAG = PermissionHelper.class.getSimpleName();

    public abstract void checkPermissions(Context activity, String[] permissions, Request listener);

    public abstract void requestPermissions(Activity activity, String[] permissions);

    public abstract void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults);
}
