package com.liang.permission;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.liang.permission.annotation.Permission;
import com.liang.permission.annotation.PermissionBanned;
import com.liang.permission.annotation.PermissionDenied;
import com.liang.permission.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Aspect
public class ResultHelperImp extends ResultHelper {

    private ProceedingJoinPoint proceedingJoinPoint;

    @Pointcut("execution(@com.liang.permission.annotation.Permission * *(..))")//方法切入点
    public void requestPermissionMethod(Permission permission) {
        Log.e("ResultHelperImp", "requestPermissionMethod: " + permission.value().length);
    }

    @Around("requestPermissionMethod(permission)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Permission permission) {
        Log.e("ResultHelperImp", "AroundJoinPoint: " + permission.value().length);
//        Context context = null;
//        proceedingJoinPoint = joinPoint;
//        final Object object = joinPoint.getThis();
//        if (object instanceof Context) {
//            context = (Context) object;
//        } else if (object instanceof Fragment) {
//            context = ((Fragment) object).getActivity();
//        } else if (object instanceof android.support.v4.app.Fragment) {
//            context = ((android.support.v4.app.Fragment) object).getActivity();
//        }
//        if (context == null || permission == null) {
//            return;
//        }
//
//        PermissionUtils.go2PermissionRequest(context, permission.value());
    }

    @Override
    public void onPermissionUntreated(String[] permissions) {

    }

    @Override
    public void onPermissionGranted(String[] permissions) {
//        try {
//            proceedingJoinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
    }

    @Override
    public void onPermissionDenied(String[] permissions) {
//        PermissionUtils.requestPermissionsResult(proceedingJoinPoint, permissions, PermissionDenied.class);
    }

    @Override
    public void onPermissionBanned(String[] permissions) {
//        PermissionUtils.requestPermissionsResult(proceedingJoinPoint, permissions, PermissionBanned.class);
    }
}
