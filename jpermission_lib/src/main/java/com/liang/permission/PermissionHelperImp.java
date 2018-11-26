package com.liang.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelperImp extends PermissionHelper {

    private static final int REQUEST_CODE = 0x200;
    private static final int PERMISSION_UNTREATED = 1;
    private static final int PERMISSION_GRANTED = 2;
    private static final int PERMISSION_DENIED = 3;
    private static final int PERMISSION_BANNED = 4;

    private ResultHelper resultHelper;

    @Override
    public void checkPermissions(Context activity, String[] permissions, ResultHelper listener) {
        resultHelper = listener;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackRequestResult(PERMISSION_GRANTED, permissions);
            return;
        }
        List<String> denied = new ArrayList<>();
        List<String> banned = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, permission) &&
                        SharedPreHelper.getBoolean(activity, TAG)) {
                    banned.add(permission);
                } else {
                    denied.add(permission);
                }
            }
        }

        if (!banned.isEmpty()) {
            callbackRequestResult(PERMISSION_BANNED, banned.toArray(new String[banned.size()]));
            return;
        }

        if (!denied.isEmpty()) {
            callbackRequestResult(PERMISSION_UNTREATED, denied.toArray(new String[denied.size()]));
            return;
        }

        callbackRequestResult(PERMISSION_GRANTED, permissions);
    }

    @Override
    public void requestPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (grantResults.length > 0) {
            List<String> denied = new ArrayList<>();
            List<String> banned = new ArrayList<>();
            for (int j = 0; j < grantResults.length; j++) {
                if (grantResults[j] != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[j])) {
                        banned.add(permissions[j]);
                    } else {
                        denied.add(permissions[j]);
                    }
                }
            }

            SharedPreHelper.putBoolean(activity, TAG, true);

            if (!banned.isEmpty()) {
                callbackRequestResult(PERMISSION_BANNED, banned.toArray(new String[banned.size()]));
                return;
            }

            if (!denied.isEmpty()) {
                callbackRequestResult(PERMISSION_DENIED, denied.toArray(new String[denied.size()]));
                return;
            }

            callbackRequestResult(PERMISSION_GRANTED, permissions);
        }
    }


    private void callbackRequestResult(int permissionType, String... permissions) {
        if (resultHelper == null) {
            return;
        }
        switch (permissionType) {
            case PERMISSION_UNTREATED:
                resultHelper.onPermissionUntreated(permissions);
                break;
            case PERMISSION_GRANTED:
                resultHelper.onPermissionGranted(permissions);
                break;
            case PERMISSION_DENIED:
                resultHelper.onPermissionDenied(permissions);
                break;
            case PERMISSION_BANNED:
                resultHelper.onPermissionBanned(permissions);
                break;
        }
    }
}
