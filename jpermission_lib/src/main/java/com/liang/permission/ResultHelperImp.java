package com.liang.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

public class ResultHelperImp extends ResultHelper {
    private PermissionListener listener;
    private Context context;
    private boolean isOpenedSettings;
    private String[] permissions;
    private PermissionHelper permissionHelper;

    public ResultHelperImp(Context context, PermissionListener listener) {
        this.context = context;
        this.listener = listener;
        permissionHelper = new PermissionHelperImp();
    }

    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }

    public void checkPermissions(String... permissions) {
        this.permissions = permissions;
        permissionHelper.checkPermissions(context, this.permissions, this);
    }

    public void onResume() {
        if (isOpenedSettings) {
            permissionHelper.checkPermissions(context, this.permissions, this);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult((Activity) context, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {
        permissionHelper.requestPermissions((Activity) context, permissions);
    }

    @Override
    public void onPermissionGranted(String[] permissions) {
        if (listener != null) {
            listener.onPermissionGranted(permissions);
        }
    }

    @Override
    public void onPermissionDenied(final String[] permissions) {
        isOpenedSettings = false;
        if (permissions.length > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            try {
                dialog.setIcon(context.getPackageManager().getApplicationIcon(context.getPackageName()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle(R.string.app_system_tip);
            dialog.setMessage(R.string.app_permission_denied);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    permissionHelper.requestPermissions((Activity) context, permissions);
                }
            });

            dialog.setNegativeButton(R.string.app_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != listener) {
                        listener.onPermissionDenied();
                    }
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
        if (permissions.length > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            try {
                dialog.setIcon(context.getPackageManager().getApplicationIcon(context.getPackageName()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle(R.string.app_system_tip);
            dialog.setMessage(R.string.app_permission_banned);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.app_setting, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isOpenedSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            });

            dialog.setNegativeButton(R.string.app_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != listener) {
                        listener.onPermissionDenied();
                    }
                }
            });
            dialog.show();
        }
    }

    public void unBind() {
        listener = null;
    }
}
