package com.example.equinoxkernelmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {
    public static final String KEY_APP_UPDATE_REQUIRED = "app_update_required";
    public static final String KEY_APP_UPDATE_VERSION = "app_update_version";
    public static final String KEY_APP_UPDATE_URL = "app_update_url";

    public static final String KEY_KERNEL_UPDATE_REQUIRED = "kernel_update_required";
    public static final String KEY_KERNEL_UPDATE_VERSION = "kernel_update_version";
    public static final String KEY_KERNEL_UPDATE_URL = "kernel_update_url";

    private Context context;
    private OnUpdateNeededListner onUpdateNeededListner;


    public interface OnUpdateNeededListner{
        void onUpdateNeeded(String updateUrl);
    }

    public UpdateHelper(Context context, OnUpdateNeededListner onUpdateNeededListner) {
        this.context = context;
        this.onUpdateNeededListner = onUpdateNeededListner;
    }


    private String getAppVersion(Context context){
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            Log.v("shanu",e.toString());
        }
        return result;
    }

    private String getKernelVersion(){
        String kernel_version = System.getProperty("os.version");
        return kernel_version;
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    public void check(){
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if(remoteConfig.getBoolean(KEY_APP_UPDATE_REQUIRED)){
            String updateVersion = remoteConfig.getString(KEY_APP_UPDATE_VERSION);
            String updateUrl = remoteConfig.getString(KEY_APP_UPDATE_URL);

            String appVersion = getAppVersion(context);
            Log.v("shanu","AppVersion="+appVersion+" updateVersion="+updateVersion);
            if(!TextUtils.equals(appVersion,updateVersion) && onUpdateNeededListner!=null){
                onUpdateNeededListner.onUpdateNeeded(updateUrl);
            }
        }

        if(remoteConfig.getBoolean(KEY_KERNEL_UPDATE_REQUIRED)){
            String kernel_update_version = remoteConfig.getString(KEY_KERNEL_UPDATE_VERSION);
            String kernel_update_url = remoteConfig.getString(KEY_KERNEL_UPDATE_URL);

            String kernel_version = getKernelVersion();
            Log.v("shanu","Kernel Version="+kernel_version+" update version="+kernel_update_version);
            if(!TextUtils.equals(kernel_version,kernel_update_version) && onUpdateNeededListner!=null){
                onUpdateNeededListner.onUpdateNeeded(kernel_update_url);
            }
        }
    }


    public static class Builder{
        private Context context;
        private OnUpdateNeededListner onUpdateNeededListner;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListner onUpdateNeededListner){
            this.onUpdateNeededListner = onUpdateNeededListner;
            return  this;
        }


        public UpdateHelper build(){
            return new UpdateHelper(context,onUpdateNeededListner);
        }

        public UpdateHelper check(){
            UpdateHelper updateHelper = build();
            updateHelper.check();
            return updateHelper;
        }

    }
}
