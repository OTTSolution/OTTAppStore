package com.xugaoxiang.ott.appstore.util;

import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.widget.Toast;

import com.xugaoxiang.ott.appstore.MyApplication;

import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.util.log.Log;
import com.lzy.okserver.download.DownloadInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2016/9/19.
 */
public class ApkManagerUtil {

    /**
     * 打开app
     */
    public static void openApp(String packageName){
        try{
            Intent intent = MyApplication.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            MyApplication.getContext().startActivity(intent);
        }catch(Exception e){
            Toast.makeText(MyApplication.getContext(), "没有安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 正常安装app
     * @param filePath
     */
    public static void installApp(String filePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        MyApplication.getContext().startActivity(intent);
    }

    /**
     * 正常卸载app
     * @param packageName
     */
    public static void uninstallApp(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    /**
     * 静默安装
     * @param downloadInfo
     * @param packageName
     */
    public static void installAppSlient(final DownloadInfo downloadInfo, String packageName, final int appId, final GsonAppInfo gson) {
        PackageManager pm = MyApplication.getContext().getPackageManager();
        Uri fileUri = Uri.fromFile(new File(downloadInfo.getTargetPath()));
        int installFlags = 0;
        try {
            Class<?> b = Class.forName("android.content.pm.PackageManager");
            Field[] fields = b.getDeclaredFields();
            int install_replace_existing;
            for(int i=0;i <fields.length;i++){
                //获取数组中对应的属性
                Field field=fields[i];
                String fieldName=field.getName();
                if(fieldName.equals("INSTALL_REPLACE_EXISTING")){
                    install_replace_existing = (Integer) field.get(pm);
                    if(isAppInstalled(packageName))
                        installFlags |= install_replace_existing;
                }
            }
            Method getInstallMethod = pm.getClass().getDeclaredMethod("installPackage", Uri.class, IPackageInstallObserver.class, int.class, String.class);
            getInstallMethod.invoke(pm, fileUri, new IPackageInstallObserver.Stub(){

                @Override
                public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                    Intent intent = new Intent();
                    intent.setAction("action.install.result");
                    intent.putExtra("taskTag", downloadInfo.getTaskKey());
                    intent.putExtra("appId", appId);
                    intent.putExtra("gson", gson);
                    if (returnCode == 1)
                        intent.putExtra("result", 1);
                    else
                        intent.putExtra("result", -1);
                    MyApplication.getContext().sendBroadcast(intent);
                    Log.i("发送安装结果广播" + intent.getIntExtra("result", 0));
                    Log.i("安装returnCode：" + returnCode);
                }
            }, installFlags, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 静默卸载
     * @param packageName
     */
    public static void uninstallAppSlient(String packageName) {
        PackageManager pm = MyApplication.getContext().getPackageManager();
        try {
            Class<?> b = Class.forName("android.content.pm.PackageManager");
            Field[] fields = b.getDeclaredFields();
            int dont_delete_data = 0;
            for(int i=0;i <fields.length;i++){
                //获取数组中对应的属性
                Field field=fields[i];
                String fieldName=field.getName();
                if(fieldName.equals("DONT_DELETE_DATA")){
                    dont_delete_data = (Integer) field.get(pm);
                }
            }
            Method getDeleteInfo = pm.getClass().getDeclaredMethod("deletePackage", String.class, IPackageDeleteObserver.class, int.class);
            getDeleteInfo.invoke(pm, packageName, new IPackageDeleteObserver.Stub(){
                @Override
                public void packageDeleted(String packageName, int returnCode) throws RemoteException {
                    Intent intent = new Intent();
                    intent.setAction("action.uninstall.result");
                    if (returnCode == 1)
                        intent.putExtra("result", 1);
                    else
                        intent.putExtra("result", -1);
                    MyApplication.getContext().sendBroadcast(intent);
                    Log.i("发送卸载结果广播" + intent.getIntExtra("result", 0));
                    Log.i("卸载returnCode：" + returnCode);
                }
            }, dont_delete_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断app是否安装
     * @param packagename
     * @return
     */
    private static boolean isAppInstalled(String packagename) {
        try {
            MyApplication.getContext().getPackageManager().getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断是否需要隐藏此app
     * @param packageName
     * @return
     */
    public static boolean AppIsNeedToHide(String packageName) {
        List<String> packageList = new ArrayList<>();
//        packageList.add("com.longjingtech.ott.appstore");
//        packageList.add("com.longjing.ott.setting");
        packageList.add("net.sunniwell.app.swsettings.chinamobile");
        packageList.add("com.longjingtech.luffa_factorytest");
        packageList.add("org.videolan.vlc.debug");
        packageList.add("org.videolan.vlc.debugs");
        packageList.add("com.longjingtech.luffa_factorytest");
        for (String name : packageList) {
            if (packageName.equals(name))
                return true;
        }
        return false;
    }

}
