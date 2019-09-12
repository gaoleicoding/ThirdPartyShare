package com.kingdowin.gosu.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.application.MyApplication;
import com.kingdowin.gosu.object.ShareAppInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static CommonUtils util;


    public static CommonUtils getUtilInstance() {
        if (util == null) {

            util = new CommonUtils();
        }
        return util;
    }


    public CommonUtils() {
    }


    public void showToast(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showLongToast(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
        toast.show();
    }


    private List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    /**
     * 锟矫碉拷应锟斤拷锟叫憋拷
     *
     * @return
     */
    public List<ShareAppInfo> getShareAppList(Context context) {
        List<ShareAppInfo> shareAppInfos = new ArrayList<ShareAppInfo>();
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = getShareApps(context);
        if (null == resolveInfos) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfos) {
                ShareAppInfo appInfo = new ShareAppInfo();
                appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
                appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
                appInfo.setAppName(resolveInfo.loadLabel(packageManager)
                        .toString());
                appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
                shareAppInfos.add(appInfo);
            }
        }
        return shareAppInfos;
    }


    public void copyResToSdcard(Context context) {// name涓簊d鍗′笅鍒跺畾鐨勮矾寰�
        Field[] raw = R.raw.class.getFields();
        for (Field r : raw) {
            try {
                // System.out.println("R.raw." + r.getName());
                int id = context.getResources().getIdentifier(r.getName(),
                        "raw", context.getPackageName());
                String path = MyApplication.photo_path + r.getName() + ".png";
                if (new File(path).exists()) {
                    return;
                }
                BufferedOutputStream outStream = new BufferedOutputStream(
                        (new FileOutputStream(new File(path))));
                BufferedInputStream inStream = new BufferedInputStream(context
                        .getResources().openRawResource(id));
                byte[] buff = new byte[20 * 1024];
                int len;
                while ((len = inStream.read(buff)) > 0) {
                    outStream.write(buff, 0, len);
                }
                outStream.flush();
                outStream.close();
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}