package com.liang.permission;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PermissionActivity extends AppCompatActivity implements Request {

    public static final String PERMISSION_KEY = "permission_key";
    private PermissionHelper permissionHelper;
    private ResultHelper resultHelper;
    private String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionHelper = new PermissionHelperImp();
        resultHelper = new ResultHelperImp();
        permissionHelper.checkPermissions(this, permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionHelper.requestPermissions(this, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        resultHelper.onPermissionGranted(permissions);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
        resultHelper.onPermissionDenied(permissions);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        resultHelper.onPermissionBanned(permissions);
    }
}
